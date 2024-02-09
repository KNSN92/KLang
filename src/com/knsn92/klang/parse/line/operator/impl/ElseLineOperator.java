package com.knsn92.klang.parse.line.operator.impl;

import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.parse.line.LineApplyQueue;
import com.knsn92.klang.parse.line.operator.ILineOperator;
import com.knsn92.klang.parse.token.Block;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.type.impl.KeywordToken;
import com.knsn92.klang.util.TokenUtils;

public class ElseLineOperator implements ILineOperator {

	@Override
	public boolean matches(Line line, Block block, int lineidx) {
		if(line.tokens.length != 1) return false;
		if(!TokenUtils.equals(line.tokens[0], KeywordToken.type, "else")) return false;
		if((lineidx+1) >= block.length() || (!block.isBlock(lineidx+1))) {
			ErrorHandler.throwSyntaxError("The line after the else statement must be a block.");
			return false;
		}
		return true;
	}

	@Override
	public boolean apply(Line line, Block block, int lineidx, LineApplyQueue queue) {
		if(!(queue.queueSetter() instanceof IfLineOperator || queue.queueSetter() instanceof ElifLineOperator)) {
			ErrorHandler.throwSyntaxError("The else statement cannot be independent of the if or elif statement.");
			return false;
		}
		boolean bool = queue.additionalInfo() >= 1;
		if(bool) {
			queue.addNextLine(2, this);
		}else {
			queue.addNextLine(1, this);
		}
		
		line.tokens = TokenUtils.removeFromFirst(line.tokens, 1);
		
		return true;
	}

}
