package com.knsn92.klang.parse.line.operator;

import com.knsn92.klang.parse.line.operator.impl.ContinueLineOperator;
import com.knsn92.klang.parse.line.operator.impl.DefLineOperator;
import com.knsn92.klang.parse.line.operator.impl.DoLineOperator;
import com.knsn92.klang.parse.line.operator.impl.ElifLineOperator;
import com.knsn92.klang.parse.line.operator.impl.ElseLineOperator;
import com.knsn92.klang.parse.line.operator.impl.IfLineOperator;
import com.knsn92.klang.parse.line.operator.impl.ImportLineOperator;
import com.knsn92.klang.parse.line.operator.impl.ModuleLineOperator;
import com.knsn92.klang.parse.line.operator.impl.ReturnLineOperator;
import com.knsn92.klang.parse.line.operator.impl.WhileLineOperator;
import com.knsn92.klang.registry.LineOperatorRegistry;

public class LineOperators {
	
	public static final ContinueLineOperator CONTINUE;
	public static final DefLineOperator      DEF;
	public static final DoLineOperator       DO;
	public static final ElifLineOperator     ELIF;
	public static final ElseLineOperator     ELSE;
	public static final IfLineOperator       IF;
	public static final ImportLineOperator   IMPORT;
	public static final ModuleLineOperator   MODULE;
	public static final ReturnLineOperator   RETURN;
	public static final WhileLineOperator    WHILE;
	
	public static final ILineOperator[] OPERATORS;
	public static final ILineOperator[] CONDITIONS;
	public static final ILineOperator[] LOOPS;
	
	static {
		CONTINUE   =  new ContinueLineOperator();
		DEF        =  new DefLineOperator();
		DO         =  new DoLineOperator();
		ELIF       =  new ElifLineOperator();
		ELSE       =  new ElseLineOperator();
		IF         =  new IfLineOperator();
		IMPORT     =  new ImportLineOperator();
		MODULE     =  new ModuleLineOperator();
		RETURN     =  new ReturnLineOperator();
		WHILE      =  new WhileLineOperator();
		OPERATORS  =  new ILineOperator[] {CONTINUE, DEF, DO, ELIF, ELSE, IF, IMPORT, MODULE, RETURN, WHILE};
		CONDITIONS =  new ILineOperator[] {IF, ELIF, ELSE};
		LOOPS      =  new ILineOperator[] {WHILE, DO};
	}
	
	public static void register() {
		for(ILineOperator operator : OPERATORS) {
			LineOperatorRegistry.register(operator);
		}
	}
}
