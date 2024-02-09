package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.NameToken;
import com.knsn92.klang.util.TokenUtils;

public class VarNotFoundOperator implements ITokenOperator {

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(TokenUtils.equalsType(tokens.get(0), NameToken.type)) {
			ErrorHandler.throwError("VariableError", "Variable "+(String)tokens.get(0).value()+" is not found.");
		}
		return false;
	}

}
