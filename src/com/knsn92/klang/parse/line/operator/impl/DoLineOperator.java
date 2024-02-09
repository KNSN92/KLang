package com.knsn92.klang.parse.line.operator.impl;

import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.parse.line.LineApplyQueue;
import com.knsn92.klang.parse.line.operator.ILineOperator;
import com.knsn92.klang.parse.token.Block;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.type.impl.KeywordToken;
import com.knsn92.klang.util.TokenUtils;

public class DoLineOperator implements ILineOperator {

	@Override
	public boolean matches(Line line, Block block, int lineidx) {
		if(line.tokens.length != 1) return false;
		if(!TokenUtils.equals(line.tokens[0], KeywordToken.type, "do")) return false;
		if((lineidx+1) >= block.length() || (!block.isBlock(lineidx+1))) {
			ErrorHandler.throwSyntaxError("The line after the do statement must be a block.");
			System.exit(1);
		}
		WhileLineOperator whileOp = new WhileLineOperator();
		Line whileLine = TokenUtils.copyLineAll(block.getLine(lineidx+2));
		whileLine.apply();
		if((lineidx+2) >= block.length() || (!block.isLine(lineidx+2)) || !whileOp.matches(whileLine, block, lineidx+2)) {
			ErrorHandler.throwSyntaxError("The do statement cannot be independent of the while statement.");
			System.exit(1);
		}
		return true;
	}

	@Override
	public boolean apply(Line line, Block block, int lineidx, LineApplyQueue queue) {
		queue.addNextLine(1, this);
		queue.addNextLine(1, 0, this);
		
		line.tokens = TokenUtils.removeFromFirst(line.tokens, 1);
		
		return true;
	}

}
