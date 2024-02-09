package com.knsn92.klang.lang.function.method;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.function.FunctionBase;

public abstract class MethodFunction extends FunctionBase {
	
	protected Val instance;

	public MethodFunction(int argsLen, String[] argNames, Val instance) {
		super(argsLen, argNames);
		this.instance = instance;
	}
	
	public MethodFunction(int argsLen, String[] argNames) {
		super(argsLen, argNames);
	}
	
	public void setInstance(Val instance) {
		this.instance = instance;
	}

}
