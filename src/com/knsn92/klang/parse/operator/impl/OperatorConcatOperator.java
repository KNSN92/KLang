package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.OperatorToken;
import com.knsn92.klang.registry.MulticharOperatorRegistry;
import com.knsn92.klang.util.TokenUtils;

public class OperatorConcatOperator implements ITokenOperator {

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		String operator = "";
		for(int i = 0; i < tokens.size(); i++) {
			if(!TokenUtils.equalsType(tokens.get(i), OperatorToken.type)) break;
			String opr = ((String)tokens.get(i).value());
			if(opr.length() != 1) break;
			operator += opr;
		}
		if(operator.isEmpty()) return false;
		if(!MulticharOperatorRegistry.isRegistered(operator)) return false;
		TokenUtils.removeFromFirst(tokens, operator.length());
		Token token = Token.of(OperatorToken.type, operator);
		tokens.add(0, token);
		return false;
	}

}
