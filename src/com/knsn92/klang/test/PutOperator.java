package com.knsn92.klang.test;

import java.util.List;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.OperatorToken;
import com.knsn92.klang.parse.token.type.impl.VarToken;
import com.knsn92.klang.util.TokenUtils;

public class PutOperator implements ITokenOperator {

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(tokens.size() < 2) return false;
		if(!TokenUtils.equals(tokens.get(0), OperatorToken.type, "!")) return false;
		Val val;
		if(TokenUtils.isValueToken(tokens.get(1))) {
			val = TokenUtils.tokenToVal(tokens.get(1));
			TokenUtils.delValFromToken(tokens.get(1));
		}else if(TokenUtils.equalsType(tokens.get(1), VarToken.type)) {
			int id = (int)tokens.get(1).value();
			String name = ApplyHandler.nowVars().varName(id);
			val = ApplyHandler.nowVars().getVar(name);
		}else {
			return false;
		}
		System.out.println("[d]:"+val.toString());
		TokenUtils.removeFromFirst(tokens, 2);
		return true;
	}

}
