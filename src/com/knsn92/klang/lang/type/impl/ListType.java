package com.knsn92.klang.lang.type.impl;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.ValManager;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.function.method.MethodFunction;
import com.knsn92.klang.lang.function.method.NativeMethodFunction;
import com.knsn92.klang.lang.type.ICollectionType;
import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.util.TypeUtils;
import com.knsn92.klang.util.ValueUtils;

public class ListType implements ICollectionType {

	public static final ListType type = new ListType();

	@Override
	public Class<?> innerClass() {
		return Integer[].class;
	}
	
	@Override
	public String toStr(Object val) {
		Val[] vals = ValueUtils.idArrayToValArray((Integer[])val);
		String[] strs = ValueUtils.valArrayToStr(vals);
		return "["+StringUtils.join(strs, ", ")+"]";
	}
	
	@Override
	public String overrideValToString(Object val) {
		Val[] vals = new Val[0];
		Integer[] Ids = (Integer[])val;
		for(int id:Ids) {
			Val value = ValManager.manager().get(id);
			vals = ArrayUtils.add(vals, value);
		}
		return ", value=" + Arrays.deepToString(vals);
	}
	
	public boolean canIndexing(IType type) {
		return TypeUtils.equals(type, IntType.type);
	}

	@Override
	public int get(Object collection, int indexId) {
		Integer[] list = (Integer[])collection;
		long longIndex = (Long)ValueUtils.getVal(indexId).val;
		int index = (int)longIndex;
		if(!ArrayUtils.isArrayIndexValid(list, index)) {
			ErrorHandler.throwError("IndexError", "List index out of bounds. size:"+list.length+" index:"+index);
			return 0;
		}
		return list[index];
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
			ErrorHandler.throwError("IndexError", "List index out of bounds. size:"+list.length+" index:"+index);
			return;
		}
		ValManager.manager().discard(list[index]);
		list[index] = id;
		ValManager.manager().use(id);
	}
	
	 @Override
	public MethodFunction getMethod(String name) {
		 if(name.equals("join")) {
			 return new NativeMethodFunction(1, new String[] {"separator"}, (Val self, int[] args) -> {
				 String separator = (String)ValueUtils.getVal(args[0]).val;
				 Val[] list = ValueUtils.idArrayToValArray((Integer[])self.val);
				 String[] strList = ValueUtils.valArrayToStr(list);
				 return Val.of(StringType.type, String.join(separator, strList));
			 });
		 }else if(name.equals("append")) {
			 return new NativeMethodFunction(1, new String[] {"value"}, (Val self, int[] args) -> {
				 Integer[] list = (Integer[])self.val;
				 self.val = ArrayUtils.add(list, args[0]);
				 return Val.NONE;
			 });
		 }else if(name.equals("pop")) {
			 return new NativeMethodFunction(0, ArrayUtils.EMPTY_STRING_ARRAY, (Val self, int[] args) -> {
				 Integer[] list = (Integer[])self.val;
				 if(list.length <= 0) return Val.NONE;
				 Integer popId = list[list.length-1];
				 self.val = ArrayUtils.remove(list, list.length-1);
				 return ValueUtils.getVal(popId);
			 });
		 }
		 return null;
	 }

	

}
