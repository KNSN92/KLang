package com.knsn92.klang.registry;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.knsn92.klang.parse.operator.ITokenOperator;

public class OperatorRegistry {
	
	private static final Multimap<Integer, ITokenOperator> operators = ArrayListMultimap.create();
	
	public static void register(ITokenOperator op, int precedence) {
		operators.put(precedence, op);
	}
	
	public static List<List<ITokenOperator>> getOperators() {
		List<List<ITokenOperator>> res = Lists.newArrayList();
		
		List<Integer> keys = Lists.newArrayList(operators.keySet());
		Collections.sort(keys, Collections.reverseOrder());
		for(int i : keys) {
			res.add(Lists.newArrayList(operators.get(i)));
		}
		return res;
	}

}
