package com.knsn92.klang.parse.token.type.impl;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.ValManager;
import com.knsn92.klang.lang.type.IParsableType;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.ITokenType;
import com.knsn92.klang.registry.TypeParserRegistry;
import com.knsn92.klang.util.ValueUtils;

public class ValueToken implements ITokenType {
	
	public static final ValueToken type = new ValueToken();

	@Override
	public boolean mathes(String str) {
		for(IParsableType type : TypeParserRegistry.getParsables1D()) {
			if(type.matches(str)) return true;
		}
		return false;
	}

	@Override
	public Class<?> innerType() {
		return int.class;
	}

	@Override
	public Token newToken(String str) {
		Val val = null;
		for(IParsableType type : TypeParserRegistry.getParsables1D()) {
			if(type.matches(str)) val = type.toVal(str);
		}
		Token token = ValueUtils.newValueToken(val);
		return token;
	}
	
	@Override
	public String overrideValueToString(Object value) {
		String toStrVal = ", value=";
		if(value != null) {
			Val val = ValManager.manager().get((int)value);
			toStrVal += val.toString();
		}
		toStrVal += ", id="+(int)value+"]";
		return toStrVal;
	}

}
