package com.knsn92.klang.parse.token.type.impl;

import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.ITokenType;
import com.knsn92.klang.registry.KeywordRegistry;

public class KeywordToken implements ITokenType {
	
	public static final KeywordToken type = new KeywordToken();

	@Override
	public boolean mathes(String str) {
		return KeywordRegistry.isKeyword(str);
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
