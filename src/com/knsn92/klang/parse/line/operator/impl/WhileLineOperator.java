package com.knsn92.klang.parse.line.operator.impl;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.parse.line.LineApplyQueue;
import com.knsn92.klang.parse.line.operator.ILineOperator;
import com.knsn92.klang.parse.line.operator.LineOperators;
import com.knsn92.klang.parse.token.Block;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.type.impl.KeywordToken;
import com.knsn92.klang.util.TokenUtils;
import com.knsn92.klang.util.ValueUtils;

public class WhileLineOperator implements ILineOperator {

	@Override
	public boolean matches(Line line, Block block, int lineidx) {
		if(line.tokens.length != 2) return false;
		if(!TokenUtils.equals(line.tokens[0], KeywordToken.type, "while")) return false;
		if(!TokenUtils.isValueOrVarOrCollectionReferenceToken(line.tokens[1])) {
			ErrorHandler.throwInvalidSyntax();
			return false;
		}
		return true;
	}

	@Override
	public boolean apply(Line line, Block block, int lineidx, LineApplyQueue queue) {
		Val val = TokenUtils.tokenToVal(line.tokens[1]);
		Boolean bool = ValueUtils.toBool(val);
		if(bool == null) {
			ErrorHandler.throwError("TypeError", "Cannot convert to boolean value.");
			return false;
		}
		if(queue.queueSetter() instanceof DoLineOperator) {
			_dowhile(bool, queue);
		}else {
			if((lineidx+1) >= block.length() || (!block.isBlock(lineidx+1))) {
				ErrorHandler.throwSyntaxError("The line after the while statement must be a block.");
				return false;
			}
			_while(bool, queue);
		}
		
		line.tokens = TokenUtils.removeFromFirst(line.tokens, 2);
		
		return true;
	}
	
	private void _while(boolean bool, LineApplyQueue queue) {
		if(bool) {
			queue.addNextLine(1, this);
			queue.addNextLine(-1, this);
		}else {
			queue.addNextLine(2, this);
		}
	}
	
	private void _dowhile(boolean bool, LineApplyQueue queue) {
		if(bool) {
			queue.addNextLine(-1, this);
			queue.addNextLine(1, 0, LineOperators.DO);
		}
	}

}
