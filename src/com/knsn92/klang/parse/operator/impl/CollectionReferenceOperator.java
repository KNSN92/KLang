package com.knsn92.klang.parse.operator.impl;

import java.util.List;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.type.ICollectionType;
import com.knsn92.klang.lang.type.impl.ListType;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.CollectionReferenceToken;
import com.knsn92.klang.util.CollectionIndex;
import com.knsn92.klang.util.TokenUtils;
import com.knsn92.klang.util.ValueUtils;

public class CollectionReferenceOperator implements ITokenOperator {

	@Override
	public boolean apply(List<Token> tokens, Line line, boolean stepped) {
		if(tokens.size() < 2) return false;
		if(!TokenUtils.isValueOrVarOrCollectionReferenceToken(tokens.get(0))) return false;
		if(!TokenUtils.equalsValType(tokens.get(0), ListType.type)) return false;
		if(!TokenUtils.isValueToken(tokens.get(1))) return false;
		
		Val collection = TokenUtils.tokenToVal(tokens.get(0));
		Val indexList = TokenUtils.tokenToVal(tokens.get(1));
		
		if(!(collection.type instanceof ICollectionType)) {
			ErrorHandler.throwError("TypeError", "Tried to refer to a non-collection value as a collection. type:"+collection.type.getClass().getSimpleName());
			return false;
		}
		
		
		ICollectionType type = (ICollectionType)collection.type;
		
		Integer[] list = (Integer[])indexList.val;
		if(list.length != 1 || !type.canIndexing(ValueUtils.getVal(list[0]).type)) {
			ErrorHandler.throwSyntaxError("Illegal collection index type.");
			return false;
		}
		
		CollectionIndex index = new CollectionIndex(); 
		index.listId = TokenUtils.tokenToId(tokens.get(0));
		index.indexId = list[0];
		Token token = Token.of(CollectionReferenceToken.type, index);
		TokenUtils.removeFromFirst(tokens, 2, false);
		tokens.add(0, token);
		return true;
	}

}
