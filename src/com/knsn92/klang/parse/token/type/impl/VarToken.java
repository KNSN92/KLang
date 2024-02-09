package com.knsn92.klang.parse.token.type.impl;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.ITokenType;

public class VarToken implements ITokenType {
	
	public static final VarToken type = new VarToken();

	@Override
	public boolean mathes(String str) {
		return false;
	}

	@Override
	public Class<?> innerType() {
		return String.class;
	}

	@Override
	public Token newToken(String str) {
		return null;
	}
	
	@Override
	public String overrideValueToString(Object value) {
		String toStrVal = ", name=";
		toStrVal += (String)value;
		toStrVal += ", value=";
		if(value != null) {
			Val val = ApplyHandler.nowVars().getVar((String)value);
			toStrVal += val.toString();
		}
		toStrVal += ", id="+ApplyHandler.nowVars().findVarId((String)value)+"]";
		return toStrVal;
	}

}
