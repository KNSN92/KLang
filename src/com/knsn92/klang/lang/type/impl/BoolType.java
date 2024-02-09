package com.knsn92.klang.lang.type.impl;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.type.IParsableType;

public class BoolType implements IParsableType {
	
	public static final BoolType type = new BoolType();

	@Override
	public Class<?> innerClass() {
		return Boolean.class;
	}

	@Override
	public boolean matches(String token) {
		return token.equals("True") || token.equals("False");
	}

	@Override
	public Val toVal(String token) {
		Boolean bool = token.equals("True");
		return Val.of(BoolType.type, bool);
	}

	@Override
	public String toStr(Object val) {
		boolean bool = (Boolean)val;
		return bool ? "True" : "False"; 
	}

}
