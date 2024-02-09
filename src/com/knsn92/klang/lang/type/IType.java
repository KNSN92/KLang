package com.knsn92.klang.lang.type;

import com.knsn92.klang.lang.function.method.MethodFunction;

public interface IType {

	public Class<?> innerClass();
	
	default public String toStr(Object val) {
		if(val == null) return "";
		return val.toString();
	}
	
	default public String overrideValToString(Object val) {
		return ", value=\""+(val == null ? "(null)" : val.toString())+"\"";
	}
	
	default public MethodFunction getMethod(String name) {
		return null;
	}
}
