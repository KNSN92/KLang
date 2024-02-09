package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.SeparatorToken;

public class ClearSeparatorOperator implements ITokenOperator {

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(stepped) return false;
		tokens.removeIf(t -> t.equalsType(SeparatorToken.type));
		return false;
	}

}
