package com.knsn92.klang.lang.type.impl;

import com.knsn92.klang.lang.module.KModule;
import com.knsn92.klang.lang.type.IType;

public class ModuleType implements IType {
	
	@Override
	public String toStr(Object val) {
		return "module";
	}

	public static final ModuleType type = new ModuleType();

	@Override
	public Class<?> innerClass() {
		return KModule.class;
	}

}
