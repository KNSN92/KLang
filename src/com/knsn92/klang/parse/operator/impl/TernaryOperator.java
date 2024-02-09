package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.OperatorToken;
import com.knsn92.klang.util.TokenUtils;
import com.knsn92.klang.util.ValueUtils;

public class TernaryOperator implements ITokenOperator {

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		
		if(tokens.size() < 5) return false;
		
		if(!TokenUtils.isValueOrVarOrCollectionReferenceToken(tokens.get(0))) return false;
		if(!TokenUtils.equals(tokens.get(1), OperatorToken.type, "?")) return false;
		if(!TokenUtils.isValueOrVarOrCollectionReferenceToken(tokens.get(2))) return false;
		if(!TokenUtils.equals(tokens.get(3), OperatorToken.type, ":")) return false;
		if(!TokenUtils.isValueOrVarOrCollectionReferenceToken(tokens.get(4))) return false;
		
		Val boolVal = TokenUtils.tokenToVal(tokens.get(0));
		Val whenTrue = TokenUtils.tokenToVal(tokens.get(2));
		Val whenFalse = TokenUtils.tokenToVal(tokens.get(4));
		
		if(!TokenUtils.equalsValue(tokens.get(1), "?")) return false;
		if(!TokenUtils.equalsValue(tokens.get(3), ":")) return false;
		
		Boolean bool = ValueUtils.toBool(boolVal);
		if(bool == null) ErrorHandler.throwError("TypeError","Cannot convert to boolean value.");
		
		TokenUtils.removeFromFirst(tokens, 5);
		
		if(bool) {
			Token whenTrueToken = ValueUtils.newValueToken(whenTrue);
			tokens.add(0, whenTrueToken);
		}else {
			Token whenFalseToken = ValueUtils.newValueToken(whenFalse);
			tokens.add(0, whenFalseToken);
		}
		
		return true;
	}

}
