package com.knsn92.klang.parse.line.operator.impl;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.parse.line.LineApplyQueue;
import com.knsn92.klang.parse.line.operator.ILineOperator;
import com.knsn92.klang.parse.token.Block;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.type.impl.KeywordToken;
import com.knsn92.klang.util.TokenUtils;

public class ReturnLineOperator implements ILineOperator {

	@Override
	public boolean matches(Line line, Block block, int lineidx) {
		if(line.length() < 1 || line.length() > 2) return false;
		
		if(!TokenUtils.equals(line.tokenAt(0), KeywordToken.type, "return")) return false;
		if(line.length() != 1 && !TokenUtils.isValueOrVarOrCollectionReferenceToken(line.tokenAt(1))) {
			ErrorHandler.throwInvalidSyntax();
			return false;
		}
		
		if(!isInFuncBlock(block)) {
			ErrorHandler.throwSyntaxError("The return statement must always be written within a def statement.");
			return false;
		}
		
		return true;
	}

	@Override
	public boolean apply(Line line, Block block, int lineidx, LineApplyQueue queue) {
		Block funcBlock = findFuncBlock(block);
		if(line.length() == 1) {
			funcBlock.ret(Val.NONE);
		}else {
			Val val = TokenUtils.tokenToVal(line.tokenAt(1));
			funcBlock.ret(val);
		}
		
		int breakAmount = getFuncBreakAmount(block);
		ApplyHandler.addBreakQueue(breakAmount);
		
		line.tokens = TokenUtils.removeFromFirst(line.tokens, 2);
		
		return true;
	}
	
	private static int getFuncBreakAmount(Block block) {
		if(block.isFuncBlock()) return 1;
		if(block.parent() == null) return -1;
		return 1 + getFuncBreakAmount(block.parent());
	}
	
	private static Block findFuncBlock(Block block) {
		if(block.isFuncBlock()) {
			return block;
		}
		if(block.parent() == null) {
			return null;
		}
		return findFuncBlock(block.parent());
	}
	
	private static boolean isInFuncBlock(Block block) {
		if(block.isFuncBlock()) {
			return true;
		}
		if(block.parent() == null) {
			return false;
		}
		return isInFuncBlock(block.parent());
	}

}
