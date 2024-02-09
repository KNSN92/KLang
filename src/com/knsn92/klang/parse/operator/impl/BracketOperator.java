package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.google.common.collect.Lists;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.BracketToken;
import com.knsn92.klang.parse.token.type.impl.OperatorToken;
import com.knsn92.klang.util.TokenUtils;

public class BracketOperator implements ITokenOperator {

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if (tokens.size() < 2)
			return false;
		if (!TokenUtils.equals(tokens.get(0), OperatorToken.type.newToken("(")))
			return false;
		int openBracketCount = TokenUtils.count(tokens, OperatorToken.type.newToken("("));
		int closeBracketCount = TokenUtils.count(tokens, OperatorToken.type.newToken(")"));
		if (openBracketCount != closeBracketCount) {
			bracketPairError(line);
			return false;
		}
		int bracketDepth = 1;
		int closeBracketIndex = -1;
		for (int i = 1; i < tokens.size(); i++) {
			if (TokenUtils.equals(tokens.get(i), OperatorToken.type.newToken("("))) {
				++bracketDepth;
			} else if (TokenUtils.equals(tokens.get(i), OperatorToken.type.newToken(")"))) {
				--bracketDepth;
			}
			if (bracketDepth == 0) {
				closeBracketIndex = i;
				break;
			}
		}
		if (closeBracketIndex == -1) {
			bracketPairError(line);
			return false;
		}
		List<Token> bracket = Lists.newArrayList(tokens.subList(1, closeBracketIndex));
		bracket = ApplyHandler.applyOperator(bracket, line);
		TokenUtils.removeFromFirst(tokens, closeBracketIndex + 1, false);
		Token token = Token.of(BracketToken.type, TokenUtils.toTokenArray(bracket));
		tokens.add(0, token);
		return true;
	}

	private void bracketPairError(Line line) {
		ErrorHandler.throwSyntaxError("Number or order of illegal brackets.");
	}

}
