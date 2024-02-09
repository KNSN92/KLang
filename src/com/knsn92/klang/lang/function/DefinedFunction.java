package com.knsn92.klang.lang.function;

import java.util.Arrays;
import java.util.Map;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.ValManager;
import com.knsn92.klang.lang.type.impl.FunctionType;
import com.knsn92.klang.lang.variable.Vars;
import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.parse.token.Block;
import com.knsn92.klang.util.FuncInfo;
import com.knsn92.klang.util.TokenUtils;

public class DefinedFunction extends FunctionBase {
	
	protected FuncInfo funcInfo;
	protected Block funcContents;
	protected Vars usingVars;
	
	public DefinedFunction(FuncInfo funcInfo, Block block, Vars usingVars) {
		super(funcInfo.argsLen, funcInfo.argNames);
		this.funcInfo = funcInfo;
		this.funcContents = block;
		this.usingVars = usingVars;
	}
	
	public FuncInfo getFuncDef() {
		return funcInfo;
	}

	@Override
	public Val evalute(int[] args) {
		Block copiedBlock = TokenUtils.copyBlock(funcContents);
		copiedBlock.setFuncBlock();
		Vars vars = new Vars();
		for(int i = 0; i < funcInfo.argsLen; i++) {
			String name = funcInfo.argNames[i];
			vars.newVar(name);
			vars.setVar(name, args[i]);
		}
		for(Map.Entry<String, Integer> varEle : usingVars.asMap().entrySet()) {
			vars.newVar(varEle.getKey());
			vars.setVar(varEle.getKey(), varEle.getValue());
		}
		copiedBlock.setVars(vars);
		
		Val fval = Val.of(FunctionType.type, this);
		int id = ValManager.manager().new_(fval);
		vars.newVar(funcInfo.funcName);
		vars.setVar(funcInfo.funcName, id);
		
		ApplyHandler.apply(copiedBlock);
		Val retVal = copiedBlock.retVal();
		return retVal;
	}

	@Override
	public String toString() {
		return "DefinedFunction [" + (funcInfo != null ? "funcName=" + funcInfo.funcName + ", " : "")
				+ "argLength=" + argsLen + ", "
				+ (argNames != null ? "argNames=" + Arrays.toString(argNames) : "") + "]";
	}

}
