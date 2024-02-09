package com.knsn92.klang.lang.type.impl;

import org.apache.commons.lang3.ArrayUtils;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.ValManager;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.function.method.MethodFunction;
import com.knsn92.klang.lang.function.method.NativeMethodFunction;
import com.knsn92.klang.lang.type.ICollectionType;
import com.knsn92.klang.lang.type.IParsableType;
import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.util.TypeUtils;
import com.knsn92.klang.util.ValueUtils;

public class StringType implements IParsableType, ICollectionType {
	
	public static final StringType type = new StringType();

	@Override
	public Class<?> innerClass() {
		return String.class;
	}

	@Override
	public boolean matches(String token) {
		return token.startsWith("\"") && token.endsWith("\"") && token.length() >= 2;
	}

	@Override
	public Val toVal(String token) {
		return new Val(type, token.substring(1, token.length()-1));
	}
	
	public boolean canIndexing(IType type) {
		return TypeUtils.equals(type, IntType.type);
	}

	@Override
	public int get(Object collection, int indexId) {
		String str = (String)collection;
		long longIndex = (Long)ValueUtils.getVal(indexId).val;
		int index = (int)longIndex;
		if(index < 0 || index >= str.length()) {
			ErrorHandler.throwError("IndexError", "String index out of bounds. size:"+str.length()+" index:"+index);
			return 0;
		}
		Val val = Val.of(StringType.type, str.charAt(index)+"");
		int id = ValManager.manager().new_(val);
		return id;
	}

	@Override
	public boolean isImmutable() {
		return false;
	}

	@Override
	public void set(Object collection, int indexId, int id) {
		Integer[] list = (Integer[])collection;
		long longIndex = (long)ValueUtils.getVal(indexId).val;
		int index = (int)longIndex;
		if(!ArrayUtils.isArrayIndexValid(list, index)) {
			ErrorHandler.throwError("IndexError", "String index out of bounds. size:"+list.length+" index:"+index);
			return;
		}
		ValManager.manager().discard(list[index]);
		list[index] = id;
		ValManager.manager().use(id);
	}
	
	@Override
	public MethodFunction getMethod(String name) {
		 if(name.equals("split")) {
			 return new NativeMethodFunction(1, new String[] {"separator"}, (Val self, int[] args) -> {
				 String separator = (String)ValueUtils.getVal(args[0]).val;
				 String text = (String)self.val;
				 String[] splitteds = text.split(separator);
				
				 int[] ids = new int[0];
				 for(String splitted : splitteds) {
					Val val = Val.of(StringType.type, splitted);
					ids = ArrayUtils.add(ids, ValManager.manager().new_(val));
				 }
				 Integer[] iids = ArrayUtils.toObject(ids);
				 
				 return Val.of(ListType.type, iids);
			 });
		 }
		 return null;
	 }

}
