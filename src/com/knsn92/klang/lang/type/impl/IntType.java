package com.knsn92.klang.lang.type.impl;

import com.google.common.primitives.Longs;
import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.type.IParsableType;

public class IntType implements IParsableType {

	public static final IntType type = new IntType();

	@Override
	public Class<?> innerClass() {
		return Long.class;
	}

	@Override
	public boolean matches(String token) {
		return Longs.tryParse(token) != null;
	}

	@Override
	public Val toVal(String token) {
		return new Val(type, Long.parseLong(token));
	}

}
