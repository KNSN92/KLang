package com.knsn92.klang.parse.operator;

import java.util.List;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.lang.type.impl.BoolType;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.util.TokenUtils;
import com.knsn92.klang.util.ValueUtils;

public abstract class ConditionExpressionOperator implements ITokenOperator {
	
	public abstract boolean canCalc(IType typeX, IType typeY);
	public abstract String operator();
	public abstract boolean calculate(Val valX, Val valY);

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(tokens.size() < 3) {
			return false;
		}
		
		if(!TokenUtils.equalsValue(tokens.get(1), operator())) {
			return false;
		}
		
		Token tokenX = tokens.get(0);
		Token tokenY = tokens.get(2);
		
		if(TokenUtils.validNameToken(tokenX, tokenY)) return false;
		
		if(!TokenUtils.isValueOrVarOrCollectionReferenceToken(tokenX) || !TokenUtils.isValueOrVarOrCollectionReferenceToken(tokenY)) {
			if(TokenUtils.isNameToken(tokenX)) {
				ErrorHandler.throwError("VariableError", "Variable(Function) "+(String)tokenX.value()+" is not found.");
				return false;
			}
			if(TokenUtils.isNameToken(tokenY)) {
				ErrorHandler.throwError("VariableError", "Variable(Function) "+(String)tokenY.value()+" is not found.");
				return false;
			}
			ErrorHandler.throwInvalidSyntax();
			return false;
		}
		
		Val valX = TokenUtils.tokenToVal(tokenX);
		Val valY = TokenUtils.tokenToVal(tokenY);
		
		if(!canCalc(valX.type, valY.type)) {
			ErrorHandler.throwError("TypeError", "Types that are not operable with the "+operator()+" operator");
			return false;
		}
		
		TokenUtils.removeFromFirst(tokens, 3);
		
		Val val = new Val(BoolType.type, calculate(valX, valY));
		
		Token token = ValueUtils.newValueToken(val);
		
		tokens.add(0, token);
		
		return true;
	}

}
