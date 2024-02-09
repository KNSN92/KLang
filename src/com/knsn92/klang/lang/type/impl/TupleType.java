package com.knsn92.klang.lang.type.impl;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.ValManager;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.type.ICollectionType;
import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.util.TypeUtils;
import com.knsn92.klang.util.ValueUtils;

public class TupleType implements ICollectionType {

	public static final TupleType type = new TupleType();

	@Override
	public Class<?> innerClass() {
		return Integer[].class;
	}
	
	@Override
	public String toStr(Object val) {
		Val[] vals = ValueUtils.idArrayToValArray((Integer[])val);
		String[] strs = ValueUtils.valArrayToStr(vals);
		return "("+StringUtils.join(strs, ", ")+")";
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
			ErrorHandler.throwError("IndexError", "Tuple index out of bounds. size:"+list.length+" index:"+index);
			System.exit(1);
		}
		return list[index];
	}

	@Override
	public boolean isImmutable() {
		return true;
	}

	@Override
	public void set(Object collection, int indexId, int id) {}
}
