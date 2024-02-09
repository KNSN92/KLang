package com.knsn92.klang.parse.token.type.impl;

import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.ITokenType;

public class OperatorToken implements ITokenType {
	
	public static final OperatorToken type = new OperatorToken();

	@Override
	public boolean mathes(String str) {
		return true;
	}

	@Override
	public Class<?> innerType() {
		return String.class;
	}

	@Override
	public Token newToken(String str) {
		return new Token(type, str);
	}

}
