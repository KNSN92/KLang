package com.knsn92.klang.parse.operator;

import java.util.List;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.lang.type.impl.FloatType;
import com.knsn92.klang.lang.type.impl.IntType;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.OperatorToken;
import com.knsn92.klang.util.TokenUtils;
import com.knsn92.klang.util.TypeUtils;
import com.knsn92.klang.util.ValueUtils;



public abstract class NumberExpressionOperator implements ITokenOperator {
	
	private static final IType[] canCalcTypes = {IntType.type, FloatType.type};
	
	public abstract String operator();
	public abstract double calculate(double x, double y);
	
	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(tokens.size() < 3) {
			return false;
		}
		
		if(!TokenUtils.equals(tokens.get(1), OperatorToken.type,operator())) {
			return false;
		}
		
		Token tokenX = tokens.get(0);
		Token tokenY = tokens.get(2);
		
		if(TokenUtils.validNameToken(tokenX, tokenY)) {
			return false;
		}
		
		if(!TokenUtils.isValueOrVarOrCollectionReferenceToken(tokenX) || !TokenUtils.isValueOrVarOrCollectionReferenceToken(tokenY)) {
			ErrorHandler.throwInvalidSyntax();
			return false;
		}
		
		Val valX = TokenUtils.tokenToVal(tokenX);
		Val valY = TokenUtils.tokenToVal(tokenY);
		
		if(!TypeUtils.equalsAny(valX.type, canCalcTypes)) {
			ErrorHandler.throwError("TypeError", "Types that are not operable with the "+operator()+" operator");
			return false;
		}
		if(!TypeUtils.equalsAny(valY.type, canCalcTypes)) {
			ErrorHandler.throwError("TypeError", "Types that are not operable with the "+operator()+" operator");
			return false;
		}
		
		double x = 0;
		double y = 0;
		
		if(ValueUtils.equalsType(valX, IntType.type)) {
			x = (Long)valX.val;
		}else {
			x = (Double)valX.val;
		}
		
		if(ValueUtils.equalsType(valY, IntType.type)) {
			y = (Long)valY.val;
		}else {
			y = (Double)valY.val;
		}
		
		
		TokenUtils.removeFromFirst(tokens, 3);
		
		double result = calculate(x, y);
		Val val;
		if((result - Math.round(result)) == 0) {
			val = new Val(IntType.type, (long)result);
		}else {
			val = new Val(FloatType.type, result); 
		}
		
		Token token = ValueUtils.newValueToken(val);
		
		tokens.add(0, token);
		
		return true;
	}

	
}
