package com.knsn92.klang.registry;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.knsn92.klang.parse.token.type.ITokenType;

public class TokenTypeRegistry {
	
	private static final List<ITokenType> types = Lists.newArrayList();
	
	public static void register(ITokenType type) {
		types.add(type);
	}
	
	public static void registry(ITokenType type, int precedence) {
		types.add(precedence, type);
	}
	
	public static Collection<ITokenType> getTypes() {
		return Lists.newArrayList(types);
	}

}
