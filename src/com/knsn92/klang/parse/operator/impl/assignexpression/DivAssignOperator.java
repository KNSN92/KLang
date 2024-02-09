package com.knsn92.klang.parse.operator.impl.assignexpression;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.lang.type.impl.FloatType;
import com.knsn92.klang.lang.type.impl.IntType;
import com.knsn92.klang.parse.operator.AssignmentExpressionOperator;
import com.knsn92.klang.util.TypeUtils;
import com.knsn92.klang.util.ValueUtils;

public class DivAssignOperator extends AssignmentExpressionOperator {
	
	@Override
	public boolean canCalc(IType typeX, IType typeY) {
		return TypeUtils.isNumber(typeX) && TypeUtils.isNumber(typeY);
	}

	@Override
	public String operator() {
		return "/=";
	}

	@Override
	public Val calculate(Val valX, Val valY) {
		double x = ValueUtils.equalsType(valX, IntType.type) ? (Long)valX.val : (Double)valX.val;
		double y = ValueUtils.equalsType(valY, IntType.type) ? (Long)valY.val : (Double)valY.val;
		
		if(y==0) {
			ErrorHandler.throwError("DivideByZeroError", "divide by zero.");
			return Val.NONE;
		}
		
		double result = x / y;
		Val val;
		if((result - Math.round(result)) == 0) {
			val = new Val(IntType.type, (Long)(long)result);
		}else {
			val = new Val(FloatType.type, (Double)result); 
		}
		
		return val;
	}

}
