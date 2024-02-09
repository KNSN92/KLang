package com.knsn92.klang.parse.token.type.impl;

import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.ITokenType;
import com.knsn92.klang.util.FuncInfo;

public class DefFuncToken implements ITokenType {
	
	public static final DefFuncToken type = new DefFuncToken();

	@Override
	public boolean mathes(String str) {
		return false;
	}

	@Override
	public Class<?> innerType() {
		return FuncInfo.class;
	}

	@Override
	public Token newToken(String str) {
		return null;
	}

}
