package com.knsn92.klang.registry;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;
import com.knsn92.klang.parse.line.operator.ILineOperator;

public class LineOperatorRegistry {
	
	
	private static Set<ILineOperator> operators = Sets.newHashSet(); 
	
	
	public static void register(ILineOperator operator) {
		operators.add(operator);
	}
	
	public static Collection<ILineOperator> get(){
		return operators;
	}

}
