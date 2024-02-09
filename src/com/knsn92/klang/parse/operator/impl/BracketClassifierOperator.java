package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.google.common.collect.Lists;
import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.type.impl.TupleType;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.BracketToken;
import com.knsn92.klang.parse.token.type.impl.DefFuncArgsToken;
import com.knsn92.klang.parse.token.type.impl.NameToken;
import com.knsn92.klang.parse.token.type.impl.OperatorToken;
import com.knsn92.klang.util.TokenUtils;
import com.knsn92.klang.util.ValueUtils;

public class BracketClassifierOperator implements ITokenOperator {

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		
		if (tokens.isEmpty()) return false;
		if (!TokenUtils.equalsType(tokens.get(0), BracketToken.type)) return false;
		
		List<Token> bracket = Lists.newArrayList((Token[]) tokens.get(0).value());
		
		if (!TokenUtils.contains(bracket, Token.of(OperatorToken.type, ","))) return false;
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
		List<Token> splittedBracket = Lists.newArrayList();
		
		for (List<Token> splitted : splitteds) {
			if (splitted.size() >= 2 || splitted.size() <= 0) {
				ErrorHandler.throwSyntaxError("Bracket Illegal syntax");
			}
			splittedBracket.add(splitted.get(0));
		}

		Token token = null;
		if (splittedBracket.stream().allMatch(t -> TokenUtils.equalsType(t, NameToken.type))) {
			String[] args = new String[splittedBracket.size()];
			for(int i = 0; i < splittedBracket.size(); i++) {
				args[i] = (String)splittedBracket.get(i).value();
			}
			token = Token.of(DefFuncArgsToken.type, args);
		} else if (splittedBracket.stream()
				.allMatch(t -> TokenUtils.isValueOrVarOrCollectionReferenceToken(t))) {
			List<Integer> idList = Lists.newArrayList();
			for (Token valToken : splittedBracket) {
				int id = TokenUtils.tokenToId(valToken);
				idList.add(id);
			}
			Val val = new Val(TupleType.type, idList.toArray(new Integer[0]));
			token = ValueUtils.newValueToken(val);
		}else if(splittedBracket.stream().anyMatch(t -> TokenUtils.isValueOrVarOrCollectionReferenceToken(t))
				&& splittedBracket.stream().anyMatch(t -> TokenUtils.equalsType(t, NameToken.type))) {
			ErrorHandler.throwSyntaxError("You have mixed values and names in the brackets.");
		} else {
			ErrorHandler.throwSyntaxError("Bracket Illegal syntax");
		}
		tokens.set(0, token);
		return false;
	}

}
