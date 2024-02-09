package com.knsn92.klang.util;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.type.ICollectionType;

public class CollectionIndex {
	public int listId;
	public int indexId;
	
	public int get() {
		Val val = ValueUtils.getVal(listId);
		ICollectionType type =
				(ICollectionType)val.type;
		return type.get(val.val, indexId);
	}
	
	public Val getVal() {
		Val val = ValueUtils.getVal(listId);
		ICollectionType type =
				(ICollectionType)val.type;
		return ValueUtils.getVal(type.get(val.val, indexId));
	}
	
	public void set(int id) {
		Val val = ValueUtils.getVal(listId);
		ICollectionType type =
				(ICollectionType)val.type;
		type.set(val.val, indexId, id);
	}
	
	public boolean isImmutable() {
		Val val = ValueUtils.getVal(listId);
		ICollectionType type =
				(ICollectionType)val.type;
		return type.isImmutable();
	}
}