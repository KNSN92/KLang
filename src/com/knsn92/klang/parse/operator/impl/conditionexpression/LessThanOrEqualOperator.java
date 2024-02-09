package com.knsn92.klang.parse.operator.impl.conditionexpression;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.lang.type.impl.IntType;
import com.knsn92.klang.parse.operator.ConditionExpressionOperator;
import com.knsn92.klang.util.TypeUtils;
import com.knsn92.klang.util.ValueUtils;

public class LessThanOrEqualOperator extends ConditionExpressionOperator {

	@Override
	public boolean canCalc(IType typeX, IType typeY) {
		return TypeUtils.isNumber(typeX) && TypeUtils.isNumber(typeY);
	}

	@Override
	public String operator() {
		return "<=";
	}

	@Override
	public boolean calculate(Val valX, Val valY) {
		double x = ValueUtils.equalsType(valX, IntType.type) ? (Long)valX.val : (Double)valX.val;
		double y = ValueUtils.equalsType(valY, IntType.type) ? (Long)valY.val : (Double)valY.val;
		return x <= y;
	}

}
