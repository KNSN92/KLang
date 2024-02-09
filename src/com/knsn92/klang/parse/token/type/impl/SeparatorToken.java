package com.knsn92.klang.parse.token.type.impl;

import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.Tokenizer;
import com.knsn92.klang.parse.token.type.ITokenType;

public class SeparatorToken implements ITokenType {
	
	public static final SeparatorToken type = new SeparatorToken();

	@Override
	public boolean mathes(String str) {
		return str.length() == 1 && Tokenizer.ignore.contains(str.charAt(0));
	}

	@Override
	public Class<?> innerType() {
		return Character.class;
	}

	@Override
	public Token newToken(String str) {
		return new Token(type, str.charAt(0));
	}

}
