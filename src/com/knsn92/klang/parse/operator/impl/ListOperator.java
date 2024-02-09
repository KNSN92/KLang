package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.google.common.collect.Lists;
import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.ValManager;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.type.impl.ListType;
import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.OperatorToken;
import com.knsn92.klang.parse.token.type.impl.VarToken;
import com.knsn92.klang.util.TokenUtils;
import com.knsn92.klang.util.ValueUtils;

public class ListOperator implements ITokenOperator {

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if (tokens.size() < 2)
			return false;
		if (!TokenUtils.equals(tokens.get(0), OperatorToken.type.newToken("[")))
			return false;
		int openBracketCount = TokenUtils.count(tokens, OperatorToken.type.newToken("["));
		int closeBracketCount = TokenUtils.count(tokens, OperatorToken.type.newToken("]"));
		if (openBracketCount != closeBracketCount) {
			listPairError();
			return false;
		}
		int bracketDepth = 1;
		int closeBracketIndex = -1;
		for (int i = 1; i < tokens.size(); i++) {
			if (TokenUtils.equals(tokens.get(i), OperatorToken.type.newToken("["))) {
				++bracketDepth;
			} else if (TokenUtils.equals(tokens.get(i), OperatorToken.type.newToken("]"))) {
				--bracketDepth;
			}
			if (bracketDepth == 0) {
				closeBracketIndex = i;
				break;
			}
		}
		if (closeBracketIndex == -1) {
			listPairError();
		}
		List<Token> bracket = Lists.newArrayList(tokens.subList(1, closeBracketIndex));
		bracket = ApplyHandler.applyOperator(bracket, line);
		TokenUtils.removeFromFirst(tokens, closeBracketIndex + 1, false);
		
		if(bracket.size()<=0) {
			Val val = Val.of(ListType.type, new Integer[0]);
			Token token = ValueUtils.newValueToken(val);
			tokens.add(0, token);
			return true;
		}

		if (TokenUtils.equals(bracket.get(bracket.size() - 1), Token.of(OperatorToken.type, ",")))
			bracket.remove(bracket.size() - 1);
		List<List<Token>> splitteds = Lists.newArrayList();
		List<Token> capacitor = Lists.newArrayList();
		for (int i = 0; i < bracket.size(); i++) {
			if (TokenUtils.equals(bracket.get(i), Token.of(OperatorToken.type, ","))) {
				splitteds.add(capacitor);
				capacitor = Lists.newArrayList();
			} else {
				capacitor.add(bracket.get(i));
			}
		}
		splitteds.add(capacitor);
		List<Token> splittedList = Lists.newArrayList();
		for (List<Token> splitted : splitteds) {
			if (splitted.size() > 1 || splitted.size() < 1) {
				ErrorHandler.throwSyntaxError("Illegal syntax");
				System.exit(1);
			}
			splittedList.add(splitted.get(0));
		}

		Token token = null;
		if (splittedList.stream().allMatch(t -> TokenUtils.isValueToken(t) || TokenUtils.isVarToken(t))) {
			List<Integer> idList = Lists.newArrayList();
			for (Token valToken : splittedList) {
				int id;
				if (TokenUtils.equalsType(valToken, VarToken.type)) {
					id = ApplyHandler.nowVars().findVarId((String) valToken.value());
					ValManager.manager().use(id);
				} else {
					id = (int) valToken.value();
				}
				idList.add(id);
			}
			Val val = new Val(ListType.type, idList.toArray(new Integer[0]));
			token = ValueUtils.newValueToken(val);
		} else {
			ErrorHandler.throwInvalidSyntax();;
			System.exit(1);
		}
		tokens.add(0, token);

		return true;
	}

	private void listPairError() {
		ErrorHandler.throwSyntaxError("Number or order of illegal lists.");
	}

}
