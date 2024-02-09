package com.knsn92.klang.lang.type;

public interface ICollectionType extends IType {
	
	public boolean canIndexing(IType type);
	public int get(Object collection, int indexId);
	public boolean isImmutable();
	public void set(Object collection, int indexId, int id);
	
}
