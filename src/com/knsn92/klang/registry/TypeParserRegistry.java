package com.knsn92.klang.registry;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.knsn92.klang.lang.type.IParsableType;

public class TypeParserRegistry {
	
	private static Multimap<Integer, IParsableType> parsePriority = ArrayListMultimap.create();
	private static List<List<IParsableType>> parsablesCache;
	private static List<IParsableType> oneDimensionParsablesCache;
	
	
	public static void register(IParsableType type, int priority) {
		parsePriority.put(priority, type);
		rebuildParsablesCache();
	}
	
	private static void rebuildParsablesCache() {
		List<List<IParsableType>> res = Lists.newArrayList();
		
		List<Integer> keys = Lists.newArrayList(parsePriority.keySet());
		Collections.sort(keys, Collections.reverseOrder());
		for(int i : keys) {
			res.add(Lists.newArrayList(parsePriority.get(i)));
		}
		parsablesCache = res;
		
		List<IParsableType> resOneDimension = Lists.newArrayList();
		for(List<IParsableType> types : res) {
			resOneDimension.addAll(types);
		}
		oneDimensionParsablesCache = resOneDimension;
	}
	
	public static List<List<IParsableType>> getParsables() {
		return parsablesCache;
	}
	
	public static List<IParsableType> getParsables1D(){
		return oneDimensionParsablesCache;
	}

}
