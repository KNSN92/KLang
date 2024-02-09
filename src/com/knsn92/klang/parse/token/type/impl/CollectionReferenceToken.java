package com.knsn92.klang.parse.token.type.impl;

import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.ITokenType;
import com.knsn92.klang.util.CollectionIndex;

public class CollectionReferenceToken implements ITokenType {
	
	public static final CollectionReferenceToken type = new CollectionReferenceToken();

	@Override
	public boolean mathes(String str) {
		return false;
	}

	@Override
	public Class<?> innerType() {
		return CollectionIndex.class;
	}

	@Override
	public Token newToken(String str) {
		return null;
	}


}
