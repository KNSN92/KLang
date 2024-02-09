package com.knsn92.klang.parse.token;

import org.apache.commons.lang3.Validate;

import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.util.TokenUtils;

public class Line {
	
	public int line = -1;
	public Token[] tokens = new Token[0];
	public int indent = 0;
	public Block block = null;
	public String lineCode = "";
	
	public Line(Token[] tokens) {
		this.tokens = tokens;
	}
	
	public Token[] apply() {
		tokens = ApplyHandler.applyOperator(tokens, this);
		return tokens;
	}
	
	@Override
	public String toString() {
		return "[Line:"+line+"]\n"+"[indent="+indent+"]\n"+TokenUtils.toStringTokenList(tokens); 
	}
	
	public Token tokenAt(int idx) {
		Validate.validIndex(tokens, idx);
		return tokens[idx];
	}
	
	public int length() {
		return tokens.length;
	}
	
	public boolean isEmpty() {
		return length() <= 0;
	}

}
