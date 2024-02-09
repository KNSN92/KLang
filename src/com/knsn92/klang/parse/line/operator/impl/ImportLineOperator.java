package com.knsn92.klang.parse.line.operator.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.lang3.StringUtils;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.ValManager;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.module.KModule;
import com.knsn92.klang.lang.type.impl.ModuleType;
import com.knsn92.klang.lang.type.impl.StringType;
import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.parse.line.LineApplyQueue;
import com.knsn92.klang.parse.line.operator.ILineOperator;
import com.knsn92.klang.parse.token.Block;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Tokenizer;
import com.knsn92.klang.parse.token.type.impl.KeywordToken;
import com.knsn92.klang.util.TokenUtils;

public class ImportLineOperator implements ILineOperator {

	@Override
	public boolean matches(Line line, Block block, int lineidx) {
		if(line.length() != 2) return false;
		if(!TokenUtils.equals(line.tokenAt(0), KeywordToken.type, "import")) return false;
		if(!TokenUtils.isValueOrVarOrCollectionReferenceToken(line.tokenAt(1))) {
			ErrorHandler.throwInvalidSyntax();
			return false;
		}
		if(!TokenUtils.equalsValType(line.tokenAt(1), StringType.type)) {
			ErrorHandler.throwError("TypeError", "The import statement must be specified as a string type.");
			return false;
		}
		return true;
	}

	@Override
	public boolean apply(Line line, Block block, int lineidx, LineApplyQueue queue) {
		Val pathVal = TokenUtils.tokenToVal(line.tokenAt(1));
		String fileStr = (String)pathVal.val;
		File file = new File(fileStr);
		if(!file.exists()) {
			ErrorHandler.throwError("FileError", "File not found.");
			return false;
		}
		String code;
		try {
			code = StringUtils.join(Files.readAllLines(file.toPath()), "\n");
		} catch (IOException e) {
			ErrorHandler.throwError("FileError", e.getMessage());
			return false;
		}
		var stackTrace = ErrorHandler.getStackTrace();
		Block importblock = Tokenizer.splitBlock(code);
		TokenUtils.deepBlockLineRemoveEmpty(importblock);
		ErrorHandler.loadStackTrace(stackTrace);
		ErrorHandler.pushStackTrace("(import)", line);
		ApplyHandler.apply(importblock);
		ErrorHandler.popStackTrace();
		if(ErrorHandler.hasError()) return false;
		if(!importblock.isModuleBlock()) {
			ErrorHandler.throwError("ModuleError", "The module for this file is not defined.");
			return false;
		}
		String name = importblock.getModuleName();
		KModule module = new KModule();
		module.name = name;
		module.vars = importblock.vars();
		Val moduleVal = Val.of(ModuleType.type, module);
		int id = ValManager.manager().new_(moduleVal);
		ApplyHandler.nowVars().newVar(name);
		ApplyHandler.nowVars().setVar(name, id);
		ApplyHandler.nowVars().setConst(name);
		line.tokens = TokenUtils.removeFromFirst(line.tokens, 2);
		return true;
	}

}
