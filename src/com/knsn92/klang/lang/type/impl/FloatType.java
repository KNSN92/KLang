package com.knsn92.klang.lang.type.impl;

import com.google.common.primitives.Doubles;
import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.type.IParsableType;

public class FloatType implements IParsableType {
	
	public static final FloatType type = new FloatType();

	@Override
	public Class<?> innerClass() {
		return Double.class;
	}

	@Override
	public boolean matches(String token) {
		return Doubles.tryParse(token) != null;
	}

	@Override
	public Val toVal(String token) {
		return new Val(type, Double.parseDouble(token));
	}


}
