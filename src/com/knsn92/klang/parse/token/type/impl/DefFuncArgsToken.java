package com.knsn92.klang.parse.token.type.impl;

import java.util.Arrays;

import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.ITokenType;

public class DefFuncArgsToken implements ITokenType {
	
	public static final DefFuncArgsToken type = new DefFuncArgsToken();

	@Override
	public boolean mathes(String str) {
		return false;
	}

	@Override
	public Class<?> innerType() {
		return String[].class;
	}

	@Override
	public Token newToken(String str) {
		return null;
	}
	
	@Override
	public String overrideValueToString(Object value) {
		String toStrVal = ", value=";
		if(value != null) {
			String[] tokens = (String[])value;
			toStrVal += Arrays.deepToString(tokens);
		}
		return toStrVal;
	}

}
