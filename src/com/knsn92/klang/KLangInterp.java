package com.knsn92.klang;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.ValManager;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.function.NativeFunction;
import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.lang.type.impl.BoolType;
import com.knsn92.klang.lang.type.impl.FloatType;
import com.knsn92.klang.lang.type.impl.FunctionType;
import com.knsn92.klang.lang.type.impl.IntType;
import com.knsn92.klang.lang.type.impl.NoneType;
import com.knsn92.klang.lang.type.impl.StringType;
import com.knsn92.klang.lang.variable.Vars;
import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.parse.line.operator.LineOperators;
import com.knsn92.klang.parse.operator.impl.BracketClassifierOperator;
import com.knsn92.klang.parse.operator.impl.BracketOperator;
import com.knsn92.klang.parse.operator.impl.BracketToValueOperator;
import com.knsn92.klang.parse.operator.impl.ClearSeparatorOperator;
import com.knsn92.klang.parse.operator.impl.CollectionReferenceOperator;
import com.knsn92.klang.parse.operator.impl.ConstOperator;
import com.knsn92.klang.parse.operator.impl.DefFuncOperator;
import com.knsn92.klang.parse.operator.impl.DotOperator;
import com.knsn92.klang.parse.operator.impl.FloatConcatOperator;
import com.knsn92.klang.parse.operator.impl.FunctionOperator;
import com.knsn92.klang.parse.operator.impl.LetOperator;
import com.knsn92.klang.parse.operator.impl.ListOperator;
import com.knsn92.klang.parse.operator.impl.MinusOperator;
import com.knsn92.klang.parse.operator.impl.NameToVarOperator;
import com.knsn92.klang.parse.operator.impl.OperatorConcatOperator;
import com.knsn92.klang.parse.operator.impl.StringConcatOperator;
import com.knsn92.klang.parse.operator.impl.StringRepeatOperator;
import com.knsn92.klang.parse.operator.impl.TernaryOperator;
import com.knsn92.klang.parse.operator.impl.assignexpression.AddAssignOperator;
import com.knsn92.klang.parse.operator.impl.assignexpression.AssignmentOperator;
import com.knsn92.klang.parse.operator.impl.assignexpression.DivAssignOperator;
import com.knsn92.klang.parse.operator.impl.assignexpression.MultAssignOperator;
import com.knsn92.klang.parse.operator.impl.assignexpression.RemainderAssignOperator;
import com.knsn92.klang.parse.operator.impl.assignexpression.StringConcatAssignOperator;
import com.knsn92.klang.parse.operator.impl.assignexpression.StringRepeatAsignOperator;
import com.knsn92.klang.parse.operator.impl.assignexpression.SubAssignOperator;
import com.knsn92.klang.parse.operator.impl.conditionexpression.EqualsOperator;
import com.knsn92.klang.parse.operator.impl.conditionexpression.GreaterThanOperator;
import com.knsn92.klang.parse.operator.impl.conditionexpression.GreaterThanOrEqualOperator;
import com.knsn92.klang.parse.operator.impl.conditionexpression.LessThanOperator;
import com.knsn92.klang.parse.operator.impl.conditionexpression.LessThanOrEqualOperator;
import com.knsn92.klang.parse.operator.impl.conditionexpression.NotEqualsOperator;
import com.knsn92.klang.parse.operator.impl.numexpression.AddOperator;
import com.knsn92.klang.parse.operator.impl.numexpression.DivideOperator;
import com.knsn92.klang.parse.operator.impl.numexpression.MultiplyOperator;
import com.knsn92.klang.parse.operator.impl.numexpression.RemainderOperator;
import com.knsn92.klang.parse.operator.impl.numexpression.SubOperator;
import com.knsn92.klang.parse.token.Block;
import com.knsn92.klang.parse.token.Tokenizer;
import com.knsn92.klang.parse.token.type.impl.KeywordToken;
import com.knsn92.klang.parse.token.type.impl.NameToken;
import com.knsn92.klang.parse.token.type.impl.OperatorToken;
import com.knsn92.klang.parse.token.type.impl.SeparatorToken;
import com.knsn92.klang.parse.token.type.impl.ValueToken;
import com.knsn92.klang.registry.KeywordRegistry;
import com.knsn92.klang.registry.MulticharOperatorRegistry;
import com.knsn92.klang.registry.OperatorRegistry;
import com.knsn92.klang.registry.TokenTypeRegistry;
import com.knsn92.klang.registry.TypeParserRegistry;
import com.knsn92.klang.util.TokenUtils;
import com.knsn92.klang.util.TypeUtils;
import com.knsn92.klang.util.ValueUtils;

public class KLangInterp {
	
	private static boolean debug = false;
	
	private static boolean inited = false; 
	
	
	static {
		init();
	}
	
	private static void init() {
		if(inited) return;
		reset();
		setup();
		setupFunc();
		inited = true;
	}
	
	public static boolean inited() { return inited; }
	
	public static void run(File file) {
		String code = "";
		try {
			code = StringUtils.join(Files.readAllLines(file.toPath()), "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		run(code);
	}
	
	public static void run(String code) {
		init();
		Block block = Tokenizer.splitBlock(code);
		run(block);
	}
	
	public static void run(Block block) {
		TokenUtils.deepBlockLineRemoveEmpty(block);
		ErrorHandler.resetStackTrace("(code)");
		ApplyHandler.start();
		ApplyHandler.apply(block);
	}
	
	public static void reset() {
		ValManager.manager().init();
		Vars.global.clearVar();
		ApplyHandler.start();
	}
	
	public static void enableDebugMode() {
		debug = true;
	}
	
	public static boolean isDebugMode() {
		return debug;
	}
	
	private static void setup() {
		Tokenizer.ignore.add(' ');
		Tokenizer.ignore.add('\t');
		
		Tokenizer.operator.add('\n');
		Tokenizer.operator.add('#');
		Tokenizer.operator.add('.');
		Tokenizer.operator.add(',');
		Tokenizer.operator.add('+');
		Tokenizer.operator.add('-');
		Tokenizer.operator.add('*');
		Tokenizer.operator.add('/');
		Tokenizer.operator.add('%');
		Tokenizer.operator.add('=');
		Tokenizer.operator.add('>');
		Tokenizer.operator.add('<');
		Tokenizer.operator.add('\\');
		Tokenizer.operator.add('"');
		Tokenizer.operator.add('(');
		Tokenizer.operator.add(')');
		Tokenizer.operator.add('[');
		Tokenizer.operator.add(']');
		Tokenizer.operator.add('!');
		Tokenizer.operator.add('?');
		Tokenizer.operator.add(':');
		
		Tokenizer.lineSeparator = "\n";
		
		KeywordRegistry.register("if");
		KeywordRegistry.register("else");
		KeywordRegistry.register("elif");
		KeywordRegistry.register("while");
		KeywordRegistry.register("do");
		KeywordRegistry.register("break");
		KeywordRegistry.register("continue");
		KeywordRegistry.register("let");
		KeywordRegistry.register("const");
		KeywordRegistry.register("def");
		KeywordRegistry.register("return");
		KeywordRegistry.register("import");
		KeywordRegistry.register("as");
		KeywordRegistry.register("module");

		TokenTypeRegistry.register(KeywordToken.type);
		TokenTypeRegistry.register(ValueToken.type);
		TokenTypeRegistry.register(NameToken.type);
		TokenTypeRegistry.register(SeparatorToken.type);
		TokenTypeRegistry.register(OperatorToken.type);
		
		//OperatorRegistry.register(new VarNotFoundOperator(), 100);
		OperatorRegistry.register(new AssignmentOperator(),           900);
		OperatorRegistry.register(new StringConcatAssignOperator(),   900);
		OperatorRegistry.register(new AddAssignOperator(),            900);
		OperatorRegistry.register(new SubAssignOperator(),            900);
		OperatorRegistry.register(new StringRepeatAsignOperator(),    900);
		OperatorRegistry.register(new MultAssignOperator(),           900);
		OperatorRegistry.register(new DivAssignOperator(),            900);
		OperatorRegistry.register(new RemainderAssignOperator(),      900);
		OperatorRegistry.register(new TernaryOperator(),              950);
		OperatorRegistry.register(new NotEqualsOperator(),            975);
		OperatorRegistry.register(new LessThanOperator(),             975);
		OperatorRegistry.register(new GreaterThanOperator(),          975);
		OperatorRegistry.register(new LessThanOrEqualOperator(),      975);
		OperatorRegistry.register(new GreaterThanOrEqualOperator(),   975);
		OperatorRegistry.register(new EqualsOperator(),               975);
		OperatorRegistry.register(new StringConcatOperator(),        1000);
		OperatorRegistry.register(new AddOperator(),                 1000);
		OperatorRegistry.register(new SubOperator(),                 1000);
		OperatorRegistry.register(new StringRepeatOperator(),        1100);
		OperatorRegistry.register(new MultiplyOperator(),            1100);
		OperatorRegistry.register(new DivideOperator(),              1100);
		OperatorRegistry.register(new RemainderOperator(),           1100);
		OperatorRegistry.register(new MinusOperator(),               1115);
		OperatorRegistry.register(new BracketToValueOperator(),      1125);
		OperatorRegistry.register(new FunctionOperator(),            1150);
		OperatorRegistry.register(new DotOperator(),                 1175);
		OperatorRegistry.register(new CollectionReferenceOperator(), 1180);
		OperatorRegistry.register(new NameToVarOperator(),           1200);
		OperatorRegistry.register(new DefFuncOperator(),             1220);
		OperatorRegistry.register(new ConstOperator(),               1225);
		OperatorRegistry.register(new LetOperator(),                 1225);
		OperatorRegistry.register(new BracketClassifierOperator(),   1250);
		OperatorRegistry.register(new BracketOperator(),             1300);
		OperatorRegistry.register(new ListOperator(),                1400);
		OperatorRegistry.register(new ClearSeparatorOperator(),     10000);
		OperatorRegistry.register(new OperatorConcatOperator(),     15000);
		OperatorRegistry.register(new FloatConcatOperator(),        17500);
//		OperatorRegistry.register(new CommentOperator(),            20000);
		
		TypeParserRegistry.register(IntType.type, 100);
		TypeParserRegistry.register(FloatType.type, 200);
		TypeParserRegistry.register(StringType.type, 300);
		TypeParserRegistry.register(BoolType.type, 400);
		TypeParserRegistry.register(NoneType.type, 500);
		
		MulticharOperatorRegistry.register("==");
		MulticharOperatorRegistry.register("!=");
		MulticharOperatorRegistry.register(">=");
		MulticharOperatorRegistry.register("<=");
		
		MulticharOperatorRegistry.register("+=");
		MulticharOperatorRegistry.register("-=");
		MulticharOperatorRegistry.register("*=");
		MulticharOperatorRegistry.register("/=");
		MulticharOperatorRegistry.register("%=");
		
		LineOperators.register();
	}
	
	public static void setupFunc() {
		NativeFunction print = new NativeFunction(1, new String[] {"text"}, (int[] fargs) -> {
			Val text = ValManager.manager().get(fargs[0]);
			System.out.println(text.toStr());
			return Val.NONE;
		});
		Val fval = Val.of(FunctionType.type, print);
		int id = ValManager.manager().new_(fval);
		Vars.global.newVar("print");
		Vars.global.setVar("print", id);
		Vars.global.setConst("print", false);
		
		NativeFunction ftype = new NativeFunction(1, new String[] {"obj"}, (int[] fargs) -> {
			return Val.of(StringType.type, ValueUtils.idToType(fargs[0]).getClass().getSimpleName());
		});
		fval = Val.of(FunctionType.type, ftype);
		id = ValManager.manager().new_(fval);
		Vars.global.newVar("type");
		Vars.global.setVar("type", id);
		Vars.global.setConst("type", false);
		
		NativeFunction random = new NativeFunction(0, new String[] {}, (int[] fargs) -> {
			return Val.of(FloatType.type, Math.random());
		});
		fval = Val.of(FunctionType.type, random);
		id = ValManager.manager().new_(fval);
		Vars.global.newVar("random");
		Vars.global.setVar("random", id);
		Vars.global.setConst("random", false);
		
		NativeFunction hash = new NativeFunction(1, new String[] {"val"}, (int[] fargs) -> {
			return Val.of(IntType.type, (Integer)Objects.hashCode(ValueUtils.getVal(fargs[0]).val));
		});
		fval = Val.of(FunctionType.type, hash);
		id = ValManager.manager().new_(fval);
		Vars.global.newVar("hash");
		Vars.global.setVar("hash", id);
		Vars.global.setConst("hash");
		
		NativeFunction input = new NativeFunction(1, new String[] {"msg"}, (int[] fargs) -> {
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			Val text = ValManager.manager().get(fargs[0]);
			System.out.print(text.toStr());
			return Val.of(StringType.type, sc.nextLine());
		});
		fval = Val.of(FunctionType.type, input);
		id = ValManager.manager().new_(fval);
		Vars.global.newVar("input");
		Vars.global.setVar("input", id);
		Vars.global.setConst("input", false);
		
		NativeFunction _int = new NativeFunction(1, new String[] {"val"}, (int[] fargs) -> {
			Long res = ValueUtils.toInt(ValueUtils.getVal(fargs[0]));
			if(res == null) {
				IType type = ValueUtils.getVal(fargs[0]).type;
				if(TypeUtils.equals(type, StringType.type)) {
					ErrorHandler.throwError("ValueError", "The string \""+(String)ValueUtils.getVal(fargs[0]).val+"\" cannot be converted to an IntType.");
				}else {
					ErrorHandler.throwError("TypeError", "Cannot convert "+type.getClass().getSimpleName()+" to IntType.");
				}
				return Val.NONE;
			}
			return Val.of(IntType.type, res);
		});
		fval = Val.of(FunctionType.type, _int);
		id = ValManager.manager().new_(fval);
		Vars.global.newVar("int");
		Vars.global.setVar("int", id);
		Vars.global.setConst("int", false);
		
		NativeFunction _float = new NativeFunction(1, new String[] {"val"}, (int[] fargs) -> {
			Double res = ValueUtils.toFloat(ValueUtils.getVal(fargs[0]));
			if(res == null) {
				IType type = ValueUtils.getVal(fargs[0]).type;
				if(TypeUtils.equals(type, StringType.type)) {
					ErrorHandler.throwError("ValueError", "The string \""+(String)ValueUtils.getVal(fargs[0]).val+"\" cannot be converted to an FloatType.");
				}else {
					ErrorHandler.throwError("TypeError", "Cannot convert "+type.getClass().getSimpleName()+" to FloatType.");
				}
				return Val.NONE;
			}
			return Val.of(FloatType.type, res);
		});
		fval = Val.of(FunctionType.type, _float);
		id = ValManager.manager().new_(fval);
		Vars.global.newVar("float");
		Vars.global.setVar("float", id);
		Vars.global.setConst("float", false);
		
		NativeFunction _assert = new NativeFunction(2, new String[] {"current", "right"}, (int[] fargs) -> {
			if(!KLangInterp.isDebugMode())
				return Val.NONE;
			Val current = ValueUtils.getVal(fargs[0]);
			Val right = ValueUtils.getVal(fargs[1]);
			if(!ValueUtils.equals(current, right)) {
				ErrorHandler.throwError("AssertError", current.toStr()+"(current) != "+right.toStr()+"(right)");
			}
			return Val.NONE;
		});
		fval = Val.of(FunctionType.type, _assert);
		id = ValManager.manager().new_(fval);
		Vars.global.newVar("assert");
		Vars.global.setVar("assert", id);
		Vars.global.setConst("assert", false);
		
		
	}

}
