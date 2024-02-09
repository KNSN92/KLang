package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.ITokenType;
import com.knsn92.klang.parse.token.type.impl.KeywordToken;
import com.knsn92.klang.parse.token.type.impl.NameToken;
import com.knsn92.klang.parse.token.type.impl.VarToken;
import com.knsn92.klang.util.TokenUtils;

public class ConstOperator implements ITokenOperator {
	
	private static final ITokenType[] pattern = {KeywordToken.type, NameToken.type};

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(!TokenUtils.matchPattern(tokens, pattern)) return false;
		if(!TokenUtils.equalsValue(tokens.get(0), "const")) return false;
		String name = (String)tokens.get(1).value();
		if(ApplyHandler.nowVars().hasVar(name)) {
			ErrorHandler.throwError("VariableError", "Variable "+name+" already exists.");
			return false;
		}
		ApplyHandler.nowVars().newVar(name);
		ApplyHandler.nowVars().setConst(name);
		TokenUtils.removeFromFirst(tokens, 2);
		Token var = Token.of(VarToken.type, name);
		tokens.add(0, var);
		return true;
	}

}
