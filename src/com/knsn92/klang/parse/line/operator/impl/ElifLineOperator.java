package com.knsn92.klang.parse.line.operator.impl;

import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.parse.line.LineApplyQueue;
import com.knsn92.klang.parse.line.operator.ILineOperator;
import com.knsn92.klang.parse.token.Block;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.type.impl.KeywordToken;
import com.knsn92.klang.util.TokenUtils;
import com.knsn92.klang.util.ValueUtils;

public class ElifLineOperator implements ILineOperator {

	@Override
	public boolean matches(Line line, Block block, int lineidx) {
		if(line.tokens.length != 2) return false;
		if(!TokenUtils.equals(line.tokens[0], KeywordToken.type, "elif")) return false;
		if(!TokenUtils.isValueOrVarOrCollectionReferenceToken(line.tokens[1])) {
			ErrorHandler.throwInvalidSyntax();
			return false;
		}
		if((lineidx+1) >= block.length() || (!block.isBlock(lineidx+1))) {
			ErrorHandler.throwSyntaxError("The line after the elif statement must be a block.");
			return false;
		}
		return true;
	}

	@Override
	public boolean apply(Line line, Block block, int lineidx, LineApplyQueue queue) {
		if(!(queue.queueSetter() instanceof IfLineOperator || queue.queueSetter() instanceof ElifLineOperator)) {
			ErrorHandler.throwSyntaxError("The elif statement cannot be independent of the if or elif statement.");
		}
		boolean beforeResBool = queue.additionalInfo() >= 1;
		if(beforeResBool) {
			queue.addNextLine(2, 1, this);
		}else {
			Boolean bool = ValueUtils.toBool(TokenUtils.tokenToVal(line.tokens[1]));
			if(bool == null) {
				ErrorHandler.throwError("TypeError","Cannot convert to boolean value.");
				return false;
			}
			if(bool) {
				queue.addNextLine(1, this);
				queue.addNextLine(1, 1, this);
			}else {
				queue.addNextLine(2, 0, this);
			}
		}
		
		line.tokens = TokenUtils.removeFromFirst(line.tokens, 2);
		
		return true;
	}

}
