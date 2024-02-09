package com.knsn92.klang.parse.operator.impl.conditionexpression;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.parse.operator.ConditionExpressionOperator;

public class NotEqualsOperator extends ConditionExpressionOperator {
	
	@Override
	public boolean canCalc(IType typeX, IType typeY) {
		return true;
	}

	@Override
	public String operator() {
		return "!=";
	}

	@Override
	public boolean calculate(Val valX, Val valY) {
		return !valX.val.equals(valY.val);
	}

}
