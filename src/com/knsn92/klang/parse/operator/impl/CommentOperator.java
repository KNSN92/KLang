package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.OperatorToken;
import com.knsn92.klang.util.TokenUtils;

public class CommentOperator implements ITokenOperator {

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(tokens.size() <= 0) return false;
		if(!TokenUtils.equals(tokens.get(0), OperatorToken.type, "#")) return false;
		tokens.clear();
		return true;
	}

}
