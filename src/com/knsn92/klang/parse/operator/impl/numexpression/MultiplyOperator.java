package com.knsn92.klang.parse.operator.impl.numexpression;

import com.knsn92.klang.parse.operator.NumberExpressionOperator;

public class MultiplyOperator extends NumberExpressionOperator {

	@Override
	public String operator() {
		return "*";
	}

	@Override
	public double calculate(double x, double y) {
		return x*y;
	}

}
