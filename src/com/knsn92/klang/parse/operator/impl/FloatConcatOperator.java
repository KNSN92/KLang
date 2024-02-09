package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.type.impl.FloatType;
import com.knsn92.klang.lang.type.impl.IntType;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.ITokenType;
import com.knsn92.klang.parse.token.type.impl.OperatorToken;
import com.knsn92.klang.parse.token.type.impl.ValueToken;
import com.knsn92.klang.util.TokenUtils;
import com.knsn92.klang.util.ValueUtils;

public class FloatConcatOperator implements ITokenOperator {
	
	private static final ITokenType[] pattern = {ValueToken.type, OperatorToken.type, ValueToken.type};

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(!TokenUtils.matchPattern(tokens, pattern)) return false;
		if(!TokenUtils.equalsValType(tokens.get(0), IntType.type)) return false;
		if(!TokenUtils.equalsValue(tokens.get(1), ".")) return false;
		if(!TokenUtils.equalsValType(tokens.get(2), IntType.type)) return false;
		long decimal = (Long)TokenUtils.tokenToVal(tokens.get(0)).val;
		long fraction = (Long)TokenUtils.tokenToVal(tokens.get(2)).val;
		double fig = Long.toString(fraction).length();
		fig = Math.pow(10, fig);
		double res = decimal+(fraction/fig);
		TokenUtils.removeFromFirst(tokens, 3);
		Val val = Val.of(FloatType.type, (Double)res);
		Token token = ValueUtils.newValueToken(val);
		tokens.add(0, token);
		return true;
	}

}
