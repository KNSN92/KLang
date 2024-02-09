package com.knsn92.klang.parse.operator.impl.conditionexpression;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.lang.type.impl.NoneType;
import com.knsn92.klang.parse.operator.ConditionExpressionOperator;
import com.knsn92.klang.util.ValueUtils;

public class EqualsOperator extends ConditionExpressionOperator {
	
	@Override
	public boolean canCalc(IType typeX, IType typeY) {
		return true;
	}

	@Override
	public String operator() {
		return "==";
	}

	@Override
	public boolean calculate(Val valX, Val valY) {
		if(ValueUtils.equalsType(valX, NoneType.type))
			return ValueUtils.equalsType(valY, NoneType.type); 
		return valX.val.equals(valY.val);
	}

}
