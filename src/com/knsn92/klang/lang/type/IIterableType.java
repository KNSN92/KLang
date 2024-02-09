package com.knsn92.klang.lang.type;

import java.util.Iterator;

public interface IIterableType extends ICollectionType {
	
	public Iterator<Integer> getIterator();
	
}
