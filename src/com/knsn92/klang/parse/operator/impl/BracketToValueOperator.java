package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.type.impl.TupleType;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.BracketToken;
import com.knsn92.klang.util.TokenUtils;
import com.knsn92.klang.util.ValueUtils;

public class BracketToValueOperator implements ITokenOperator {

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(tokens.isEmpty()) return false;
		if(!TokenUtils.equalsType(tokens.get(0), BracketToken.type)) return false;
		Token[] bracket = (Token[])tokens.get(0).value();
		if(bracket.length > 1) return false;
		Token token;
		if(bracket.length == 1) {
			token = bracket[0];
		}else {
			Val val = Val.of(TupleType.type, new Integer[0]);
			token = ValueUtils.newValueToken(val);
		}
		tokens.set(0, token);
		return true;
	}

}
