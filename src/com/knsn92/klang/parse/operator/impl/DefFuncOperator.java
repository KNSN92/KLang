package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.BracketToken;
import com.knsn92.klang.parse.token.type.impl.DefFuncArgsToken;
import com.knsn92.klang.parse.token.type.impl.DefFuncToken;
import com.knsn92.klang.parse.token.type.impl.KeywordToken;
import com.knsn92.klang.parse.token.type.impl.NameToken;
import com.knsn92.klang.parse.token.type.impl.VarToken;
import com.knsn92.klang.util.FuncInfo;
import com.knsn92.klang.util.TokenUtils;

public class DefFuncOperator implements ITokenOperator {

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(tokens.size() < 3) return false;
		if(!TokenUtils.equals(tokens.get(0), KeywordToken.type, "def")) return false;
		if(TokenUtils.equalsType(tokens.get(1), VarToken.type)) {
			String name = (String)tokens.get(0).value();
			ErrorHandler.throwError("VariableError","Variable "+name+" already exists.");
			System.exit(1);
		}
		if(!TokenUtils.equalsType(tokens.get(1), NameToken.type)) {
			ErrorHandler.throwInvalidSyntax();
			return false;
		}
		
		String[] argNames;
		
		if(TokenUtils.equalsType(tokens.get(2), DefFuncArgsToken.type)) {
			argNames = (String[])tokens.get(2).value();
		}else if(TokenUtils.equalsType(tokens.get(2), BracketToken.type)) {
			Token[] bracket = (Token[])tokens.get(2).value();
			String[] args = new String[bracket.length];
			for(int i = 0; i < bracket.length; i++) {
				args[i] = (String)bracket[i].value();
			}
			argNames = args; 
		}else {
			ErrorHandler.throwInvalidSyntax();
			return false;
		}
		FuncInfo funcDef = new FuncInfo();
		funcDef.funcName = (String)tokens.get(1).value();
		funcDef.argNames = argNames;
		funcDef.argsLen = argNames.length;
		
		TokenUtils.removeFromFirst(tokens, 3);
				
		Token token = Token.of(DefFuncToken.type, funcDef);
		tokens.add(0, token);
		return true;
	}

}
