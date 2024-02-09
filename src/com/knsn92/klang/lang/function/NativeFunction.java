package com.knsn92.klang.lang.function;

import java.util.function.Function;

import com.knsn92.klang.lang.Val;

public class NativeFunction extends FunctionBase {
	
	private Function<int[], Val> process;

	public NativeFunction(int argsLen, String[] argNames, Function<int[], Val> process) {
		super(argsLen, argNames);
		this.process = process;
	}

	@Override
	public Val evalute(int[] ids) {
		return process.apply(ids);
	}

}
