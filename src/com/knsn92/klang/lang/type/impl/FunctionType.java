package com.knsn92.klang.lang.type.impl;

import com.knsn92.klang.lang.function.FunctionBase;
import com.knsn92.klang.lang.type.IType;

public class FunctionType implements IType {

	public static final FunctionType type = new FunctionType();

	@Override
	public Class<?> innerClass() {
		return FunctionBase.class;
	}
	
	@Override
	public String toStr(Object val) {
		return "func("+String.join(", ", ((FunctionBase)val).argNames())+")";
	}

}
