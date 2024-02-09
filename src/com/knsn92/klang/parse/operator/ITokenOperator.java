package com.knsn92.klang.parse.operator;

import java.util.List;

import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;

public interface ITokenOperator {
	
	public boolean apply(List<Token> tokens, Line line, boolean stepped);
	
}
