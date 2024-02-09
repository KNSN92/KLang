package com.knsn92.klang.parse.token.type;

import com.knsn92.klang.parse.token.Token;

public interface ITokenType {
	
	public boolean mathes(String str);
	public Class<?> innerType();
	
	default public boolean typeEquals(Class<? extends ITokenType> type) {
		return this.getClass() == type;
	}
	
	default public boolean canAsResult() {
		return false;
	}
	
	default public String overrideValueToString(Object value) {
		return ", value=\""+value.toString()+"\"";
	}
	
	public Token newToken(String str);

}
