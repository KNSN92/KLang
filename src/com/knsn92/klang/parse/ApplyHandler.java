package com.knsn92.klang.parse;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.variable.Vars;
import com.knsn92.klang.parse.line.LineApplyQueue;
import com.knsn92.klang.parse.line.operator.DummyLineOperator;
import com.knsn92.klang.parse.line.operator.ILineOperator;
import com.knsn92.klang.parse.operator.ITokenOperator;
import com.knsn92.klang.parse.token.Block;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.impl.CollectionReferenceToken;
import com.knsn92.klang.parse.token.type.impl.DefFuncArgsToken;
import com.knsn92.klang.parse.token.type.impl.NameToken;
import com.knsn92.klang.parse.token.type.impl.ValueToken;
import com.knsn92.klang.parse.token.type.impl.VarToken;
import com.knsn92.klang.registry.LineOperatorRegistry;
import com.knsn92.klang.registry.OperatorRegistry;
import com.knsn92.klang.util.TokenUtils;

public class ApplyHandler {
	
	private static Vars nowVars = Vars.global;
	private static boolean running = false;
	@SuppressWarnings("unused")
	private static int recursiveCount = -1;
	private static int breakQueue = 0;
	
	public static void apply(Block block) {
		try {
			applyInner(block);
		}catch(Exception e) {
			ErrorHandler.throwFatalErrror();
			e.printStackTrace(ErrorHandler.getDefaultStream());
		}
	}
	
	private static void applyInner(Block block) {
		recursiveCount++;
		LineApplyQueue queue = new LineApplyQueue();
		while (queue.lineIdx() < block.length() && running && breakQueue <= 0) {
			int i = queue.lineIdx();
			if(block.isLine(i)) {
				Line line = block.getLine(i);
				ErrorHandler.setLineStackTrace(line);
				Line copiedLine = TokenUtils.copyLineAll(line);
				Vars saveNowVars = nowVars();
				nowVars = copiedLine.block.vars();
				copiedLine.apply();
				TokenUtils.lineRemoveEmpty(new Line[] {copiedLine});
				ApplyHandler.applyLine(copiedLine, block, i, queue);
				if(ErrorHandler.hasError()) break;
				checkError(copiedLine);
				TokenUtils.removeFromFirst(copiedLine.tokens, copiedLine.tokens.length);
				nowVars = saveNowVars;
			}else if(block.isBlock(i)) {
				Block child = block.getBlock(i);
				Vars saveNowVars = nowVars();
				nowVars = child.vars();
				child.setCalledBy(queue.queueSetter());
				ApplyHandler.apply(child);
				if(queue.queueSetter() == LineApplyQueue.DUMMY) {
					ErrorHandler.throwSyntaxError("Illegal Indent");
					break;
				}
				child.vars().clearVar();
				child.setCalledBy(DummyLineOperator.DUMMY);
				nowVars = saveNowVars;
			}
			queue.next();
		}
		breakQueue -= (breakQueue <= 0 ? 0 : 1);
		recursiveCount--;
	}
	
	private static void checkError(Line copiedLine) {
		int idxName = TokenUtils.indexOfType(TokenUtils.toTokenList(copiedLine.tokens), NameToken.type);
		int idxDefArgs = TokenUtils.indexOfType(TokenUtils.toTokenList(copiedLine.tokens), DefFuncArgsToken.type);
		if(idxName != -1) {
			ErrorHandler.throwError("VariableError", "Variable "+(String)copiedLine.tokenAt(idxName).value()+" is not found.");
		}else if(idxDefArgs != -1) {
			ErrorHandler.throwError("VariableError", "Variable "+((String[])copiedLine.tokenAt(idxDefArgs).value())[0]+" is not found.");
		}else if(!copiedLine.isEmpty()) {
			if(copiedLine.length() <= 1) {
				int count = TokenUtils.countType(TokenUtils.toTokenList(copiedLine.tokens), ValueToken.type)
						  + TokenUtils.countType(TokenUtils.toTokenList(copiedLine.tokens), VarToken.type)
						  + TokenUtils.countType(TokenUtils.toTokenList(copiedLine.tokens), CollectionReferenceToken.type);
				if(copiedLine.length() != count) {
					ErrorHandler.throwInvalidSyntax();
				}
			}
		}
	}
	
	private static void applyLine(Line line, Block block, int lineIdx, LineApplyQueue queue) {
		Collection<ILineOperator> operators = LineOperatorRegistry.get();
		for(ILineOperator lineOperator : operators) {
			if(!lineOperator.matches(line, block, lineIdx)) continue;
			lineOperator.apply(line, block, lineIdx, queue);
			if(!running) return;
			break;
		}
	}
	
	public static Token[] applyOperator(Token[] tokens, Line line) {
		List<Token> tokenList = Lists.newArrayList(Arrays.asList(tokens));
		tokenList = applyOperator(tokenList, line);
		return tokenList.toArray(new Token[0]);
	}

	public static List<Token> applyOperator(List<Token> tokens, Line line) {
		nowVars = line.block.vars();
		for (List<ITokenOperator> operators : OperatorRegistry.getOperators()) {
			for (int i = 0; i < tokens.size(); i++) {
				boolean doStay = false;
				for (ITokenOperator operator : operators) {
					doStay = doStay || operator.apply(tokens.subList(i, tokens.size()), line, i != 0);
 					if(!running) return tokens;
				}
				i -= doStay ? 1 : 0;
			}
		}
		return tokens;
	}
	
	public static void start() {
		running = true;
	}
	
	public static void exit() {
		running = false;
	}
	
	public static Vars nowVars() {
		return nowVars;
	}
	
	public static void addBreakQueue() {
		breakQueue++;
	}
	
	public static void addBreakQueue(int amount) {
		breakQueue += amount;
	}

}
