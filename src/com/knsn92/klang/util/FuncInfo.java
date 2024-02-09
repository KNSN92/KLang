package com.knsn92.klang.util;

import java.util.Arrays;

public class FuncInfo {
	
	public int argsLen;
	public String[] argNames;
	public String funcName;
	
	@Override
	public String toString() {
		return "FuncInfo [argsLen=" + argsLen + ", "
				+ (argNames != null ? "argNames=" + Arrays.toString(argNames) + ", " : "")
				+ (funcName != null ? "funcName=" + funcName : "") + "]";
	}

}
