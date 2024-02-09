package com.knsn92.klang.parse.operator.impl.assignexpression;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.lang.type.impl.StringType;
import com.knsn92.klang.parse.operator.AssignmentExpressionOperator;
import com.knsn92.klang.util.TypeUtils;

public class StringConcatAssignOperator extends AssignmentExpressionOperator {

	@Override
	public boolean canCalc(IType typeX, IType typeY) {
		return TypeUtils.equals(typeX, StringType.type);
	}

	@Override
	public String operator() {
		return "+=";
	}

	@Override
	public Val calculate(Val valX, Val valY) {
		return Val.of(StringType.type, valX.toStr()+valY.toStr());
	}
	
	@Override
	public boolean doThrowErrorWhenNotOperableType() {
		return false;
	}

}
