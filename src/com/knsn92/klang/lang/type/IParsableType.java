package com.knsn92.klang.lang.type;

import com.knsn92.klang.lang.Val;

public interface IParsableType extends IType {
	
	public boolean matches(String token);
	public Val toVal(String token);
	
}
