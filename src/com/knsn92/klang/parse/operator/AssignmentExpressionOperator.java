package com.knsn92.klang.parse.operator;

import java.util.List;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.ValManager;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.OperatorToken;
import com.knsn92.klang.util.CollectionIndex;
import com.knsn92.klang.util.TokenUtils;

public abstract class AssignmentExpressionOperator implements ITokenOperator {
	
	public abstract boolean canCalc(IType typeX, IType typeY);
	public abstract String operator();
	public abstract Val calculate(Val valX, Val valY);
	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(tokens.size() < 3) return false;
		if (!TokenUtils.equals(tokens.get(1), OperatorToken.type, operator()))return false;
		if (TokenUtils.isVarToken(tokens.get(0))) {
			
			String name = (String) tokens.get(0).value();
			if(!ApplyHandler.nowVars().canAssign(name)) {
				ErrorHandler.throwError("VariableError", "Variable "+name+" is const variable.");
				return false;
			}
			Val ValX = ApplyHandler.nowVars().getVar(name);
			Val ValY = TokenUtils.tokenToVal(tokens.get(2));
			if(!canCalc(ValX.type, ValY.type)) {
				if(doThrowErrorWhenNotOperableType())
					ErrorHandler.throwError("TypeError", "Types that are not operable with the "+operator()+" operator.");
				return false;
			}
			
			Val resVal = calculate(ValX, ValY);
			int id = ValManager.manager().new_(resVal);
			ApplyHandler.nowVars().setVar(name, id);
		}else if(TokenUtils.isCollectionReferenceToken(tokens.get(0))) {
			CollectionIndex index = 
					(CollectionIndex) tokens.get(0).value();
			if(index.isImmutable()) {
				ErrorHandler.throwError("ValueError", "Immutable collections cannot be changed.");
				return false;
			}
			
			Val ValX = index.getVal();
			if(ErrorHandler.hasError()) return false;
			Val ValY = TokenUtils.tokenToVal(tokens.get(2));
			if(!canCalc(ValX.type, ValY.type)) {
				if(doThrowErrorWhenNotOperableType())
					ErrorHandler.throwError("TypeError", "Types that are not operable with the "+operator()+" operator.");
				return false;
			}
			
			Val resVal = calculate(ValX, ValY);
			int id = ValManager.manager().new_(resVal);
			index.set(id);
		}else {
			ErrorHandler.throwInvalidSyntax();
			return false;
		}
		TokenUtils.removeFromFirst(tokens, 3);
		return false;
	}
	
	public boolean doThrowErrorWhenNotOperableType() {
		return true;
	}
	
}
