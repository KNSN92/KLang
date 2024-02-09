package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.ValManager;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.function.method.MethodFunction;
import com.knsn92.klang.lang.module.KModule;
import com.knsn92.klang.lang.type.impl.FunctionType;
import com.knsn92.klang.lang.type.impl.ModuleType;
import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.NameToken;
import com.knsn92.klang.parse.token.type.impl.OperatorToken;
import com.knsn92.klang.parse.token.type.impl.VarToken;
import com.knsn92.klang.util.TokenUtils;
import com.knsn92.klang.util.ValueUtils;

public class DotOperator implements ITokenOperator {

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(tokens.size() < 3) return false;
		if(!TokenUtils.isValueOrVarOrCollectionReferenceToken(tokens.get(0))) return false;
		if(!TokenUtils.equals(tokens.get(1), OperatorToken.type, ".")) return false;
		if(!TokenUtils.equalsTypeAny(tokens.get(2), NameToken.type, VarToken.type)) {
			ErrorHandler.throwInvalidSyntax();
			return false;
		}
		if(TokenUtils.equalsValType(tokens.get(0), ModuleType.type)) {
			KModule module = (KModule)TokenUtils.tokenToVal(tokens.get(0)).val;
			String name = (String)tokens.get(2).value();
			if(!module.vars.hasVar(name)) {
				ErrorHandler.throwError("VariableError", "Variable(Function) "+name+" is not found in "+(String)tokens.get(0).value()+".");
				return false;
			}
			Val val = module.vars.getVar(name);
			TokenUtils.removeFromFirst(tokens, 3);
			Token token = ValueUtils.newValueToken(val);
			tokens.add(0, token);
		}else {
			Val instance = TokenUtils.tokenToVal(tokens.get(0));
			String name = (String)tokens.get(2).value();
			MethodFunction method = instance.type.getMethod(name);
			if(method == null) {
				ErrorHandler.throwError("VariableError", "Variable(Method) "+name+" is not found in "+instance.type.getClass().getSimpleName()+".");
				return false;
			}
			method.setInstance(instance);
			
			Val val = Val.of(FunctionType.type, method);
			int id = ValManager.manager().new_(val);
			ApplyHandler.nowVars().newVar(name);
			ApplyHandler.nowVars().setVar(name, id);
			TokenUtils.removeFromFirst(tokens, 3);
			Token token = ValueUtils.newValueToken(val);
			tokens.add(0, token);
		}
		return true;
	}

}
