package com.knsn92.klang.lang.function;

import java.util.Arrays;

import com.knsn92.klang.lang.Val;

public abstract class FunctionBase {
	
	protected int argsLen;
	protected String[] argNames;
	
	public FunctionBase(){}
	
	public abstract Val evalute(int[] args);

	public FunctionBase(int argsLen, String[] argNames) {
		this.argsLen = argsLen;
		this.argNames = argNames;
	}
	
	public String[] argNames() {
		return argNames;
	}
	
	public int argsLength() {
		return argsLen;
	}
	
	@Override
	public String toString() {
		return "FunctionBase [argsLen=" + argsLen + ", "
				+ (argNames != null ? "argNames=" + Arrays.toString(argNames) : "") + "]";
	}

}
