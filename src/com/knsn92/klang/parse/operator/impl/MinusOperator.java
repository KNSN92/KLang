package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.type.impl.FloatType;
import com.knsn92.klang.lang.type.impl.IntType;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.ITokenType;
import com.knsn92.klang.parse.token.type.impl.CollectionReferenceToken;
import com.knsn92.klang.parse.token.type.impl.OperatorToken;
import com.knsn92.klang.parse.token.type.impl.ValueToken;
import com.knsn92.klang.parse.token.type.impl.VarToken;
import com.knsn92.klang.util.TokenUtils;
import com.knsn92.klang.util.ValueUtils;

public class MinusOperator implements ITokenOperator {
	
	private static final ITokenType[] patternValue = new ITokenType[] {OperatorToken.type, ValueToken.type};
	private static final ITokenType[] patternVar = new ITokenType[] {OperatorToken.type, VarToken.type};
	private static final ITokenType[] patternCollectionIndex = new ITokenType[]{OperatorToken.type, CollectionReferenceToken.type};
	
	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		
		int idxOffset = 0;
		if(tokens.size() >= 2 && !stepped) {
			if(TokenUtils.matchPattern(tokens, patternValue)) {
			} else if(TokenUtils.matchPattern(tokens, patternVar)) {
			} else if(TokenUtils.matchPattern(tokens, patternCollectionIndex)) {
			}else { return false; }
		}else if(tokens.size() > 2){
			if(TokenUtils.equalsTypeAny(tokens.get(0), ValueToken.type, VarToken.type)) return false;
			if(TokenUtils.matchPattern(tokens, patternValue, 1)) {
			} else if(TokenUtils.matchPattern(tokens, patternVar, 1)) {
			} else if(TokenUtils.matchPattern(tokens, patternCollectionIndex, 1)) {
			}else { return false; }
			idxOffset = 1;
		}else {
			return false;
		}
		
		if(!TokenUtils.equalsValue(tokens.get(0+idxOffset), "-")) {
			return false;
		}
		
		Val val = TokenUtils.tokenToVal(tokens.get(1+idxOffset));
		
		if(ValueUtils.equalsType(val, IntType.type)) {
			Long num = (Long)val.val;
			val = Val.of(IntType.type, -num);
		}else if(ValueUtils.equalsType(val, FloatType.type)) {
			Double num = (Double)val.val;
			val = Val.of(FloatType.type, -num);
		}else {
			ErrorHandler.throwError("TypeError", "This type cannot invert the sign.");
			return false;
		}
		Token token = ValueUtils.newValueToken(val);
		TokenUtils.removeFromFirst(tokens, 2, idxOffset);
		tokens.add(idxOffset,token);
		return true;
	}

}
