package com.knsn92.klang.util;

import java.util.Arrays;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Longs;
import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.ValManager;
import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.lang.type.impl.BoolType;
import com.knsn92.klang.lang.type.impl.FloatType;
import com.knsn92.klang.lang.type.impl.IntType;
import com.knsn92.klang.lang.type.impl.NoneType;
import com.knsn92.klang.lang.type.impl.StringType;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.ValueToken;

public class ValueUtils {
	
	public static boolean equalsType(Val val, IType type) {
		return TypeUtils.equals(val.type, type);
	}
	
	
	public static boolean equals(Val valX, Val valY) {
		if(!TypeUtils.equals(valX.type, valY.type))
			return false;
		if(equalsType(valX, NoneType.type))
			return true;
		return valX.val.equals(valY.val);
	}
	
	
	public static Val getVal(int id) {
		return ValManager.manager().get(id);
	}
	
	public static void delVal(int id) {
		ValManager.manager().discard(id);
	}
	
	public static Token newValueToken(Val val) {
		int id = ValManager.manager().new_(val);
		return Token.of(ValueToken.type, id);
	}
	
	public static IType idToType(int id) {
		return getVal(id).type;
	}
	
	public static Val[] idArrayToValArray(Integer[] ids) {
		Val[] vals = new Val[ids.length];
		for(int i = 0; i < ids.length; i++) {
			vals[i] = ValManager.manager().get(ids[i]);
		}
		return vals;
	}
	
	public static Val[] idArrayToValArray(int[] ids) {
		Val[] vals = new Val[ids.length];
		for(int i = 0; i < ids.length; i++) {
			vals[i] = ValManager.manager().get(ids[i]);
		}
		return vals;
	}
	
	public static Object[] valArrayToObjArray(Val[] vals) {
		return Arrays.stream(vals).map(v->v.val).toArray();
	}

	
	public static String[] valArrayToStr(Val[] vals) {
		String[] strs = new String[vals.length];
		for(int i = 0; i < vals.length; i++) {
			strs[i] = vals[i].toStr();
		}
		return strs;
	}
	
	public static Boolean toBool(Val val) {
		boolean bool = false;
		if(equalsType(val, BoolType.type)) {
			bool = (Boolean)val.val;
		}else if(equalsType(val, IntType.type)) {
			bool = ((Long)val.val).equals(0L);
			bool = !bool;
		}else if(equalsType(val, FloatType.type)) {
			bool = ((Double)val.val).equals(0D);
			bool = !bool;
		}else {
			return null;
		}
		return bool;
	}
	
	public static boolean canToBool(Val val) {
		if(equalsType(val, BoolType.type)) {
		}else if(equalsType(val, IntType.type)) {
		}else if(equalsType(val, FloatType.type)) {
		}else {
			return false;
		}
		return true;
	}
	
	public static Long toInt(Val val) {
		Long num = null;
		if(equalsType(val, IntType.type)) {
			num = (Long)val.val;
		}else if(equalsType(val, FloatType.type)) {
			num = (long)Math.floor((Double)val.val);
		}else if(equalsType(val, StringType.type)) {
			num = Longs.tryParse((String)val.val);
		}else {
			return null;
		}
		return num;
	}
	
	public static Double toFloat(Val val) {
		Double num = null;
		if(equalsType(val, IntType.type)) {
			num = (double)(Long)val.val;
		}else if(equalsType(val, FloatType.type)) {
			num = (Double)val.val;
		}else if(equalsType(val, StringType.type)) {
			num = Doubles.tryParse((String)val.val);
		}else {
			return null;
		}
		return num;
	}

}
