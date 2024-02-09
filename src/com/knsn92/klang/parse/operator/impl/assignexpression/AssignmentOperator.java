package com.knsn92.klang.parse.operator.impl.assignexpression;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.parse.operator.AssignmentExpressionOperator;

public class AssignmentOperator extends AssignmentExpressionOperator {

//	private static final ITokenType[] rightTypes = { ValueToken.type, VarToken.type};
	
	@Override
	public boolean canCalc(IType typeX, IType typeY) {
		return true;
	}

	@Override
	public String operator() {
		return "=";
	}

	@Override
	public Val calculate(Val valX, Val valY) {
		return valY;
	}
	/*@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(tokens.size() < 3) return false;
		if (!TokenUtils.equals(tokens.get(1), OperatorToken.type, "="))return false;
		if(!TokenUtils.equalsTypeAny(tokens.get(2), rightTypes)) {
			ErrorHandler.throwInvalidSyntax();
			return false;
		}
		int id = TokenUtils.tokenToId(tokens.get(2));
		if (TokenUtils.equalsType(tokens.get(0), VarToken.type)) {
			String namel = (String) tokens.get(0).value();
			ApplyHandler.nowVars().setVarReference(namel, id);
		}else if(TokenUtils.equalsType(tokens.get(0), CollectionReferenceToken.type)) {
			CollectionIndex index = 
					(CollectionIndex) tokens.get(0).value();
			if(index.isImmutable()) {
				ErrorHandler.throwError("ValueError", "Immutable collections cannot be changed.");
				System.exit(1);
			}
			index.set(id);
		}else {
			ErrorHandler.throwInvalidSyntax();
			return false;
		}
		TokenUtils.removeFromFirst(tokens, 3);
		return false;
	}*/

}
