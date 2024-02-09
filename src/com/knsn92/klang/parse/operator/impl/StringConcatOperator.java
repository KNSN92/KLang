package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.lang.type.impl.FloatType;
import com.knsn92.klang.lang.type.impl.IntType;
import com.knsn92.klang.lang.type.impl.StringType;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.util.TokenUtils;
import com.knsn92.klang.util.TypeUtils;
import com.knsn92.klang.util.ValueUtils;

public class StringConcatOperator implements ITokenOperator {
	
	private static final IType[] canCalcTypes = {IntType.type, FloatType.type};

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(tokens.size() < 3) {
			return false;
		}
		
		if(!TokenUtils.equalsValue(tokens.get(1), "+")) {
			return false;
		}
		
		Token tokenX = tokens.get(0);
		Token tokenY = tokens.get(2);
		
		if(TokenUtils.validNameToken(tokenX, tokenY)) return false;
		
		if(!TokenUtils.isValueOrVarOrCollectionReferenceToken(tokenX) || !TokenUtils.isValueOrVarOrCollectionReferenceToken(tokenY)) return false;
		
		Val valX = TokenUtils.tokenToVal(tokenX);
		Val valY = TokenUtils.tokenToVal(tokenY);
		
		if(TypeUtils.equalsAny(valX.type, canCalcTypes) && TypeUtils.equalsAny(valY.type, canCalcTypes)) return false;
		
		TokenUtils.removeFromFirst(tokens, 3);
		
		String result = valX.toStr() + valY.toStr();
		
		Val val = new Val(StringType.type, result);
		
		Token token = ValueUtils.newValueToken(val);
		
		tokens.add(0, token);
		
		return true;
	}

}
