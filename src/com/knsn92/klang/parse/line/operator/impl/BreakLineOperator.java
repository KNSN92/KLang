package com.knsn92.klang.parse.line.operator.impl;

import org.apache.commons.lang3.ArrayUtils;

import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.parse.line.LineApplyQueue;
import com.knsn92.klang.parse.line.operator.ILineOperator;
import com.knsn92.klang.parse.line.operator.LineOperators;
import com.knsn92.klang.parse.token.Block;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.type.impl.KeywordToken;
import com.knsn92.klang.util.TokenUtils;

public class BreakLineOperator implements ILineOperator {

	private static ILineOperator[] loops = {LineOperators.WHILE}; 

	@Override
	public boolean matches(Line line, Block block, int lineidx) {
		if(line.tokens.length != 1) return false;
		if(!TokenUtils.equals(line.tokens[0], KeywordToken.type, "break")) return false;
		if(!isInLoopBlock(block)) {
			ErrorHandler.throwSyntaxError("The break statement must always be written within a any loop statement.");
			return false;
		}
		return false;
	}

	@Override
	public boolean apply(Line line, Block block, int lineidx, LineApplyQueue queue) {
		ApplyHandler.addBreakQueue(getLoopBreakAmount(block));
		return false;
	}
	
	private static boolean isInLoopBlock(Block block) {
		if(ArrayUtils.contains(loops, block.calledBy())) {
			return true;
		}
		if(block.parent() == null) {
			return false;
		}
		return isInLoopBlock(block.parent());
	}
	
	private static int getLoopBreakAmount(Block block) {
		if(ArrayUtils.contains(loops, block.calledBy())) return 1;
		if(block.parent() == null) return -1;
		return 1 + getLoopBreakAmount(block.parent());
	}

}
