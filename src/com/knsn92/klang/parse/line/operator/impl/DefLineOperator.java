package com.knsn92.klang.parse.line.operator.impl;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.ValManager;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.function.DefinedFunction;
import com.knsn92.klang.lang.type.impl.FunctionType;
import com.knsn92.klang.lang.variable.Vars;
import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.parse.line.LineApplyQueue;
import com.knsn92.klang.parse.line.operator.ILineOperator;
import com.knsn92.klang.parse.token.Block;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.DefFuncToken;
import com.knsn92.klang.util.FuncInfo;
import com.knsn92.klang.util.TokenUtils;

public class DefLineOperator implements ILineOperator {

	@Override
	public boolean matches(Line line, Block block, int lineidx) {
		
		if(line.isEmpty()) return false;
		
		if(!TokenUtils.equalsType(line.tokenAt(0), DefFuncToken.type)) return false;
		
		if((lineidx+1) >= block.length() || (!block.isBlock(lineidx+1))) {
			ErrorHandler.throwSyntaxError("The line after the def statement must be a block.");
			return false;
		}
		return true;
	}

	@Override
	public boolean apply(Line line, Block block, int lineidx, LineApplyQueue queue) {
		
		Token funcToken = line.tokenAt(0);
		FuncInfo funcDef = (FuncInfo)funcToken.value();
		Block funcContents =  TokenUtils.copyBlock(block.getBlock(lineidx+1));
		funcContents.setFuncBlock();
		funcContents.setVars(new Vars());
		DefinedFunction definedfunc = new DefinedFunction(funcDef, funcContents, ApplyHandler.nowVars());
		
		if(ApplyHandler.nowVars().hasVar(funcDef.funcName)) {
			ErrorHandler.throwError("VariableError","This variable "+funcDef.funcName+" already exists.");
			return false;
		}
		
		Val fval = Val.of(FunctionType.type, definedfunc);
		int id = ValManager.manager().new_(fval);
		ApplyHandler.nowVars().newVar(funcDef.funcName);
		ApplyHandler.nowVars().setVar(funcDef.funcName, id);
		
		queue.addNextLine(2, this);
		
		line.tokens = TokenUtils.removeFromFirst(line.tokens, 1);
		
		return true;
	}

}
  