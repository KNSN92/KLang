package com.knsn92.klang.parse.operator.impl.numexpression;

import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.parse.operator.NumberExpressionOperator;

public class RemainderOperator extends NumberExpressionOperator {

	@Override
	public String operator() {
		return "%";
	}

	@Override
	public double calculate(double x, double y) {
		if(y==0) {
			ErrorHandler.throwError("DivideByZeroError", "divide by zero.");
			return 0;
		}
		return x % y;
	}

}
