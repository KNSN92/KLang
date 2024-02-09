package com.knsn92.klang.lang.module;

import com.knsn92.klang.lang.variable.Vars;

public class KModule {
	
	@Override
	public String toString() {
		return "KModule [" + (vars != null ? "vals=" + vars : "") + "]";
	}

	public Vars vars;
	public String name;

}
