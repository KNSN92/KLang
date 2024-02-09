package com.knsn92.klang.util;

import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.lang.type.impl.FloatType;
import com.knsn92.klang.lang.type.impl.IntType;

public class TypeUtils {
	
	public static boolean equals(IType t0, IType t1) {
		return t0 == t1;
	}
	
	public static boolean equalsAny(IType t, IType... types) {
		for(IType type : types) {
			if(equals(t, type)) return true;
		}
		return false;
	}
	
	public static boolean isNumber(IType type) {
		return type == IntType.type || type == FloatType.type;
	}

}
