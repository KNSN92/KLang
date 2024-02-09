package com.knsn92.klang.parse.line.operator.impl;

import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.parse.line.LineApplyQueue;
import com.knsn92.klang.parse.line.operator.ILineOperator;
import com.knsn92.klang.parse.token.Block;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.type.impl.KeywordToken;
import com.knsn92.klang.parse.token.type.impl.NameToken;
import com.knsn92.klang.util.TokenUtils;

public class ModuleLineOperator implements ILineOperator {

	@Override
	public boolean matches(Line line, Block block, int lineidx) {
		if(line.length() != 2) return false;
		if(!TokenUtils.equals(line.tokenAt(0), KeywordToken.type, "module")) return false;
		if(!TokenUtils.equalsType(line.tokenAt(1), NameToken.type)) {
			ErrorHandler.throwInvalidSyntax();
			return false;
		}
		return true;
	}

	@Override
	public boolean apply(Line line, Block block, int lineidx, LineApplyQueue queue) {
		String moduleName = (String)line.tokenAt(1).value();
		block.setModuleBlock(moduleName);
		line.tokens = TokenUtils.removeFromFirst(line.tokens, 2);
		return true;
	}

}
