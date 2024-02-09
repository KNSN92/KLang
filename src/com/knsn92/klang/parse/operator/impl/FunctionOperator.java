	package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.function.DefinedFunction;
import com.knsn92.klang.lang.function.FunctionBase;
import com.knsn92.klang.lang.function.NativeFunction;
import com.knsn92.klang.lang.type.impl.FunctionType;
import com.knsn92.klang.lang.type.impl.TupleType;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.BracketToken;
import com.knsn92.klang.parse.token.type.impl.NameToken;
import com.knsn92.klang.parse.token.type.impl.ValueToken;
import com.knsn92.klang.parse.token.type.impl.VarToken;
import com.knsn92.klang.util.TokenUtils;
import com.knsn92.klang.util.ValueUtils;

public class FunctionOperator implements ITokenOperator {

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(tokens.size() < 2) return false;
		
		if(TokenUtils.equalsType(tokens.get(0), NameToken.type)) {
			Token idx1Token = tokens.get(1);
			if(!TokenUtils.equalsTypeAny(idx1Token, ValueToken.type, BracketToken.type)) return false;
			if(TokenUtils.equalsType(idx1Token, ValueToken.type) && !TokenUtils.equalsValType(idx1Token, TupleType.type)) return false;
			ErrorHandler.throwError("VariableError", "Variable(Function) "+(String)tokens.get(0).value()+" is not found.");
			return false;
		}
		
		Integer[] integerArgs = new Integer[0];
		if(TokenUtils.equalsType(tokens.get(1), ValueToken.type)) {
			if(!TokenUtils.equalsValType(tokens.get(1), TupleType.type)) return false;
			Val valArgs = TokenUtils.tokenToVal(tokens.get(1));
			integerArgs = (Integer[])valArgs.val;
		}else if (TokenUtils.equalsType(tokens.get(1), BracketToken.type)) {
			Token[] bracket = (Token[])tokens.get(1).value();
			if(bracket.length > 1) return false;
			if(bracket.length == 1) {
				if(!TokenUtils.isValueOrVarOrCollectionReferenceToken(bracket[0])) return false;
				int id = TokenUtils.tokenToId(bracket[0]);
				integerArgs = new Integer[]{id};
			}
		}else {
			return false;
		}
		
		if(ErrorHandler.hasError()) {
			return false;
		}
		
		if(!TokenUtils.isValueOrVarOrCollectionReferenceToken(tokens.get(0))) return false;
		
		Val valFunc = TokenUtils.tokenToVal(tokens.get(0));
		if(!ValueUtils.equalsType(valFunc, FunctionType.type)) {
			ErrorHandler.throwError("TypeError", "Tried to execute something as a function that is not a function.");
			return false;
		}
		FunctionBase func = (FunctionBase)valFunc.val;
		int[] args = ArrayUtils.toPrimitive(integerArgs);
		if(args.length != func.argsLength()) {
			String funcName;
			if(TokenUtils.equalsType(tokens.get(0), VarToken.type)) {
				funcName = (String)tokens.get(0).value();
			}else {
				funcName = "func$"+(Integer)tokens.get(0).value();
			}
			ErrorHandler.throwError("ArgsError", "Wrong argument length. by "+ funcName +" right:"+func.argsLength()+" current:"+args.length);
			return false;
		}
		if(func instanceof DefinedFunction) {
			ErrorHandler.pushStackTrace(((DefinedFunction)func).getFuncDef().funcName, line);
		}else if(func instanceof NativeFunction) {
			ErrorHandler.pushStackTrace("(native)", line);
		}else {
			ErrorHandler.pushStackTrace("(func)", line);
		}
		Val res = func.evalute(args);
		ErrorHandler.popStackTrace();
		Token resToken = ValueUtils.newValueToken(res);
		TokenUtils.removeFromFirst(tokens, 2);
		tokens.add(0,resToken);
		return true;
	}

}
