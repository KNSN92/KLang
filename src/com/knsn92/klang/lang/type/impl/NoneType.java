package com.knsn92.klang.lang.type.impl;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.type.IParsableType;

public class NoneType implements IParsableType {
	
	public static final NoneType type = new NoneType();

	@Override
	public Class<?> innerClass() {
		return null;
	}

	@Override
	public boolean matches(String token) {
		return token.equals("None");
	}

	@Override
	public Val toVal(String token) {
		return Val.NONE;
	}

}
