package com.knsn92.klang.registry;

import java.util.Set;

import com.google.common.collect.Sets;

public class KeywordRegistry {
	
	private static final Set<String> keywords = Sets.newHashSet();
	
	public static void register(String keyword) {
		keywords.add(keyword);
	}
	
	public static boolean isKeyword(String word) {
		return keywords.contains(word);
	}

}
