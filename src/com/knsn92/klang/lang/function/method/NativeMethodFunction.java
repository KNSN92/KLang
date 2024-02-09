package com.knsn92.klang.lang.function.method;

import java.util.function.BiFunction;

import com.knsn92.klang.lang.Val;

public class NativeMethodFunction extends MethodFunction {

	private BiFunction<Val, int[], Val> process;

	public NativeMethodFunction(int argsLen, String[] argNames, Val instance, BiFunction<Val, int[], Val> process) {
		super(argsLen, argNames, instance);
		this.process = process;
	}
	
	public NativeMethodFunction(int argsLen, String[] argNames, BiFunction<Val, int[], Val> process) {
		super(argsLen, argNames);
		this.process = process;
	}

	@Override
	public Val evalute(int[] args) {
		return process.apply(this.instance, args);
	}

}
