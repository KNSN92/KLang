package com.knsn92.klang.parse.token.type.impl;

import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.ITokenType;

public class NameToken implements ITokenType {
	
	public static final NameToken type = new NameToken();

	@Override
	public boolean mathes(String str) {
		return str.matches("[_$a-zA-Z]\\w*");
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
