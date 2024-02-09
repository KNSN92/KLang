package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.NameToken;
import com.knsn92.klang.parse.token.type.impl.VarToken;
import com.knsn92.klang.util.TokenUtils;

public class NameToVarOperator implements ITokenOperator {

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(tokens.size()<1) return false;
		if(!TokenUtils.equalsType(tokens.get(0), NameToken.type)) return false;
		String name = (String)tokens.get(0).value();
		if(!ApplyHandler.nowVars().hasVar(name)) {
			return false;
		}
		Token token = Token.of(VarToken.type, name);
		tokens.set(0, token);
		return false;
	}

}
