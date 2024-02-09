package com.knsn92.klang.registry;

import java.util.Set;

import com.google.common.collect.Sets;

public class MulticharOperatorRegistry {
	
	private static Set<String> operators = Sets.newHashSet();
	
	public static void register(String operator) {
		operators.add(operator);
	}
	
	public static boolean isRegistered(String operator) {
		return operators.contains(operator);
	}

}
