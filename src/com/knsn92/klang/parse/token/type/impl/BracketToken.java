package com.knsn92.klang.parse.token.type.impl;

import java.util.Arrays;

import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.ITokenType;

public class BracketToken implements ITokenType {
	
	public static final BracketToken type = new BracketToken();

	@Override
	public boolean mathes(String str) {
		return false;
	}

	@Override
	public Class<?> innerType() {
		return Token[].class;
	}

	@Override
	public Token newToken(String str) {
		return null;
	}
	
	@Override
	public String overrideValueToString(Object value) {
		String toStrVal = ", value=";
		if(value != null) {
			Token[] tokens = (Token[])value;
			toStrVal += Arrays.deepToString(tokens);
		}
		return toStrVal;
	}

}
