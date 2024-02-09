 package com.knsn92.klang.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Lists;
import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.ValManager;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.type.IType;
import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.parse.token.Block;
import com.knsn92.klang.parse.token.Line;
import com.knsn92.klang.parse.token.Token;
import com.knsn92.klang.parse.token.type.ITokenType;
import com.knsn92.klang.parse.token.type.impl.CollectionReferenceToken;
import com.knsn92.klang.parse.token.type.impl.NameToken;
import com.knsn92.klang.parse.token.type.impl.ValueToken;
import com.knsn92.klang.parse.token.type.impl.VarToken;

public class TokenUtils {
	
	
	
	public static final BiPredicate<Token, Token> equalType = (t0, t1) -> t0.equalsType(t1.type());
	public static final BiPredicate<Token, Token> equalValue = (t0, t1) -> t0.value().equals(t1.value());
	public static final BiPredicate<Token, Token> equalIndex = (t0, t1) -> t0.index == t1.index;
	public static final BiPredicate<Token, Token> equalLine = (t0, t1) -> t0.line == t1.line;
	
	public static BiPredicate<Token, Token> getDefaultTokenMatcher(){
		return getTokenMatcher(true, true, false, false);
	}
	
	public static BiPredicate<Token, Token> getTokenMatcher(boolean type, boolean value, boolean line, boolean index){
		BiPredicate<Token, Token> matcher = (t0, t1) -> true;
		if(type)  matcher = matcher.and(equalType);
		if(value) matcher = matcher.and(equalValue);
		if(index) matcher = matcher.and(equalIndex);
		if(line)  matcher = matcher.and(equalLine);
		return matcher;
	}
	
	
	public static List<Token> copyTokenList(List<Token> tokens) {
		return Lists.newArrayList(tokens);
	}
	
	public static Token[] toTokenArray(List<Token> tokens) {
		return tokens.toArray(new Token[0]);
	}
	
	public static List<Token> toTokenList(Token[] tokens) {
		return Lists.newArrayList(Arrays.asList(tokens));
	}
	
	
	public static Token copyToken(Token token, Line line) {
		if(isValueToken(token)) {
			Val val = tokenToVal(token);
			if(val == Val.NONE) {
				return ValueUtils.newValueToken(Val.NONE);
			}
			return ValueUtils.newValueToken(tokenToVal(token).copy());
		}
		return Token.of(token.type(), token.value());
	}
	
	public static Token copyTokenAll(Token token, Line line) {
		Token newToken = copyToken(token, line);
		newToken.line = token.line;
		newToken.index = token.index;
		return newToken;
	}
	
	public static Line copyLine(Line line) {
		Token[] copiedTokens = new Token[line.tokens.length];
		for(int i = 0; i < line.tokens.length; i++) {
			copiedTokens[i] = copyToken(line.tokens[i], line);
		}
		return new Line(copiedTokens);
	}
	
	public static Line copyLineAll(Line line) {
		Token[] copiedTokens = new Token[line.tokens.length];
		for(int i = 0; i < line.tokens.length; i++) {
			copiedTokens[i] = copyTokenAll(line.tokens[i], line);
		}
		Line newLine = new Line(copiedTokens);
		newLine.block = line.block;
		newLine.indent = line.indent;
		newLine.line = line.line;
		newLine.lineCode = line.lineCode;
		return newLine;
	}
	
	public static Block copyBlock(Block block) {
		Line[] lines = block.toLines();
		Line[] copiedLines = new Line[lines.length];
		for(int i = 0; i < lines.length; i++) {
			copiedLines[i] = copyLineAll(lines[i]);
		}
		Block newBlock = new Block(copiedLines, block.baseIndent());
		for(int i = 0; i < copiedLines.length; i++) {
			copiedLines[i].block = newBlock;
		}
		return newBlock;
	}
	
	
	public static boolean equalsAny(Token t, Token... tokens) {
		return equalsAny(t, getDefaultTokenMatcher(), tokens);
	}
	
	public static boolean equalsAny(Token t, BiPredicate<Token, Token> condition, Token... tokens) {
		for(Token token : tokens) {
			if(equals(t, token, condition)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean equals(Token t0, Token t1) {
		return equals(t0, t1, getDefaultTokenMatcher());
	}
	
	public static boolean equals(Token t0, Token t1, BiPredicate<Token, Token> condition) {
		return condition.test(t0, t1);
	}
	
	public static boolean equals(Token t, ITokenType type, Object val) {
		return equalsType(t, type) && equalsValue(t, val);
	}
	
	public static boolean equalsTypeAny(Token t, ITokenType... types) {
		for(ITokenType type : types) {
			if(equalsType(t, type)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean equalsType(Token t, ITokenType type) {
		if(t == null) return false;
		return t.equalsType(type);
	}
	
	public static boolean equalsValue(Token t, Object val) {
		if(t == null) return false;
		return t.value() == val || Objects.equals(t.value(), val);
	}
	
	public static int indexOf(List<Token> tokens, Token find) {
		return indexOf(tokens, find, getDefaultTokenMatcher());
	}
	
	public static int indexOf(List<Token> tokens, Token find, BiPredicate<Token, Token> condition) {
		if(Objects.isNull(tokens) || Objects.isNull(find) || Objects.isNull(condition)) {
			return -1;
		}
		for(int i = 0; i< tokens.size(); i++) {
			if(equals(tokens.get(i), find, condition)) return i; 
		}
		return -1;
	}
	
	public static int indexOfType(List<Token> tokens, ITokenType type) {
		if(Objects.isNull(tokens) || Objects.isNull(type)) {
			return -1;
		}
		for(int i = 0; i< tokens.size(); i++) {
			if(equalsType(tokens.get(i), type)) return i; 
		}
		return -1;
	}
	
	public static int[] indexesOf(List<Token> tokens, Token find) {
		return indexesOf(tokens, find, getDefaultTokenMatcher());
	}
	
	public static int[] indexesOf(List<Token> tokens, Token find, BiPredicate<Token, Token> condition) {
		if(Objects.isNull(tokens) || Objects.isNull(find) || Objects.isNull(condition)) {
			return new int[0];
		}
		int[] indexes = new int[0];
		for(int i = 0; i< tokens.size(); i++) {
			if(equals(tokens.get(i), find, condition)) indexes = ArrayUtils.add(indexes, i); 
		}
		return indexes;
	}
	
	public static int[] indexesOfType(List<Token> tokens, ITokenType type) {
		if(Objects.isNull(tokens) || Objects.isNull(type)) {
			return new int[0];
		}
		int[] indexes = new int[0];
		for(int i = 0; i< tokens.size(); i++) {
			if(equalsType(tokens.get(i), type)) indexes = ArrayUtils.add(indexes, i); 
		}
		return indexes;
	}
	
	public static boolean contains(List<Token> tokens, Token find) {
		return indexOf(tokens, find) != -1;
	}
	
	public static boolean contains(List<Token> tokens, Token find, BiPredicate<Token, Token> condition) {
		return indexOf(tokens, find, condition) != -1;
	}
	
	public static boolean containsType(List<Token> tokens, ITokenType type) {
		return indexOfType(tokens, type) != -1;
	}
	
	public static int count(List<Token> tokens, Token find) {
		return count(tokens, find, getDefaultTokenMatcher());
	}
	
	public static int count(List<Token> tokens, Token find, BiPredicate<Token, Token> condition) {
		if(Objects.isNull(tokens) || Objects.isNull(find) || Objects.isNull(condition)) {
			return 0;
		}
		int count = 0;
		for(Token token : tokens) {
			if(equals(token, find, condition)) ++count;
		}
		return count;
	}
	
	public static int countType(List<Token> tokens, ITokenType find) {
		if(Objects.isNull(tokens) || Objects.isNull(find)) {
			return 0;
		}
		int count = 0;
		for(Token token : tokens) {
			if(equalsType(token, find)) ++count;
		}
		return count;
	}
	
	public static Token[] filter(List<Token> tokens, Token find){
		return filter(tokens, find, getDefaultTokenMatcher());
	}
	
	public static Token[] filter(List<Token> tokens, Token find, BiPredicate<Token, Token> condition){
		if(Objects.isNull(tokens) || Objects.isNull(find) || Objects.isNull(condition)) {
			return new Token[0];
		}
		Token[] filterTokens = new Token[0];
		for(Token token : tokens) {
			if(equals(token, find, condition)) filterTokens = ArrayUtils.add(filterTokens, token); 
		}
		return filterTokens;
	}
	
	public static Token[] filterType(List<Token> tokens, ITokenType type) {
		if(Objects.isNull(tokens) || Objects.isNull(type)) {
			return new Token[0];
		}
		Token[] filterTokens = new Token[0];
		for(Token token : tokens) {
			if(equalsType(token, type)) filterTokens = ArrayUtils.add(filterTokens, token); 
		}
		return filterTokens;
	}
	
	
	public static boolean isCollectionReferenceToken(Token token) {
		return equalsType(token, CollectionReferenceToken.type);
	}
	
	public static boolean isVarToken(Token token) {
		return equalsType(token, VarToken.type);
	}
	
	public static boolean isValueOrVarOrCollectionReferenceToken(Token token) {
		return isValueToken(token) || isVarToken(token) || isCollectionReferenceToken(token);
	}
	
	public static boolean isValueOrVarToken(Token token) {
		return isValueToken(token) || isVarToken(token);
	}
	
	public static boolean isValueToken(Token token) {
		return equalsType(token, ValueToken.type);
	}
	
	public static boolean isNameToken(Token token) {
		return equalsType(token, NameToken.type);
	}
	
	public static boolean validNameToken(Token... tokens) {
		for(Token token:tokens) {
			if(isNameToken(token)) {
				ErrorHandler.throwError("VariableError", "Variable "+(String)token.value()+" is not found.");
				return true;
			}
		}
		return false;
	}
	
	private static void validIsValueOrVarOrCollectionReferenceToken(Token token) {
		if(isValueOrVarOrCollectionReferenceToken(token)) return;
		throw new ClassCastException("");
	}
	
	@SuppressWarnings("unused")
	private static void validIsValueOrVarToken(Token token) {
		if(isValueOrVarToken(token)) return;
		throw new ClassCastException("");
	}
	
	private static void validIsValueToken(Token token) {
		if(isValueToken(token)) return;
		throw new ClassCastException("");
	}
	
	public static Val tokenToVal(Token token) {
		validIsValueOrVarOrCollectionReferenceToken(token);
		if(isCollectionReferenceToken(token)) {
			CollectionIndex idx = (CollectionIndex)token.value();
			return idx.getVal();
		}else if(isVarToken(token)) {
			String name = (String)token.value();
			return ApplyHandler.nowVars().getVar(name);
		}else if(isValueToken(token)){
			int id = (int)token.value();
			return ValueUtils.getVal(id);
		}
		return Val.NONE;
	}
	
	public static void delValFromToken(Token token) {
		validIsValueToken(token);
		int id = (int)token.value();
		ValueUtils.delVal(id);
	}
	
	public static int tokenToId(Token token) {
		validIsValueOrVarOrCollectionReferenceToken(token);
		if(isCollectionReferenceToken(token)) {
			CollectionIndex idx = (CollectionIndex)token.value();
			return idx.get();
		}else if(isVarToken(token)) {
			return ApplyHandler.nowVars().findVarId((String)token.value());
		}else if(isValueToken(token)){
			return (int)token.value();
		}
		return -1;
	}
	
	public static boolean equalsValType(Token token, IType type) {
		validIsValueOrVarOrCollectionReferenceToken(token);
		return ValueUtils.equalsType(tokenToVal(token), type);
	}
	
	
	public static void removeFromFirst(List<Token> tokens, int removals) {
		removeFromFirst(tokens, removals, true);
	}
	
	public static Token[] removeFromFirst(Token[] tokens, int removals) {
		return removeFromFirst(tokens, removals, true);
	}
	
	public static void removeFromFirst(List<Token> tokens, int removals, boolean release) {
		removeFromFirst(tokens, removals, release, 0);
	}
	
	public static Token[] removeFromFirst(Token[] tokens, int removals, boolean release) {
		return removeFromFirst(tokens, removals, release, 0);
	}
	
	public static void removeFromFirst(List<Token> tokens, int removals, int offset) {
		removeFromFirst(tokens, removals, true, offset);
	}
	
	public static Token[] removeFromFirst(Token[] tokens, int removals, int offset) {
		return removeFromFirst(tokens, removals, true, offset);
	}
	
	public static void removeFromFirst(List<Token> tokens, int removals, boolean release, int offset) {
		if(removals+offset >= tokens.size()) {
			removals = tokens.size()-offset;
		}
		for(int i = 0; i < removals; i++) {
			Token token = tokens.get(offset);
			if(release && isValueToken(token) && ValManager.manager().has(tokenToId(token))) {
				delValFromToken(token);
			}
			tokens.remove(offset);
		}
	}
	
	public static Token[] removeFromFirst(Token[] tokens, int removals, boolean release, int offset) {
		if(removals+offset >= tokens.length) {
			removals = tokens.length-offset;
		}
		for(int i = 0; i < removals; i++) {
			Token token = tokens[offset];
			if(release && isValueToken(token) && ValManager.manager().has(tokenToId(token))) {
				delValFromToken(token);
			}
			tokens = ArrayUtils.remove(tokens, offset);
		}
		return tokens;
	}
	
	
	public static boolean matchPattern(List<Token> tokens, ITokenType[] pattern) {
		return matchPattern(tokens, pattern, 0);
	}
	
	public static boolean matchPattern(List<Token> tokens, ITokenType[] pattern, int offset) {
		if(tokens.size() < pattern.length+offset) return false;
		
		for(int i = 0; i < pattern.length; i++) {
			if(!tokens.get(i+offset).equalsType(pattern[i])) {
				return false;
			}
		}
		return true;
	}
	
	
	public static void deepBlockLineRemoveEmpty(Block block) {
		for(int i = 0; i < block.length(); i++) {
			if(block.isLine(i)) {
				if(block.getLine(i).tokens.length > 0) continue;
				block.removeElement(i);
				continue;
			} 
			if(block.isBlock(i)) {
				TokenUtils.deepBlockLineRemoveEmpty(block.getBlock(i));
				continue;
			}
		}
	}
	
	public static void blockLineRemoveEmpty(Block block) {
		for(int i = 0; i < block.length(); i++) {
			if(!block.isBlock(i)) continue;
			if(block.getLine(i).tokens.length > 0) continue;
			block.removeElement(i);
		}
	}
	
	public static void lineRemoveEmpty(List<Line> lines) {
		lines.removeIf(l -> l.tokens.length <= 0);
	}
	
	public static Line[] lineRemoveEmpty(Line[] lines) {
		List<Integer> emptyIndexes = Lists.newArrayList();
		for(int i = 0; i < lines.length; i++) {
			if(lines[i].tokens.length <= 0) emptyIndexes.add(0,i);
		}
		for(int emptyIndex : emptyIndexes) {
			lines = ArrayUtils.remove(lines, emptyIndex);
		}
		return lines;
	}
	
	
	public static String toStringTokenList(Token[] tokens) {
		if(tokens.length <= 0) return "";
		String str = "";
		for(Token token : tokens) {
			String tokenStr = token.toString();
			tokenStr = tokenStr.replace("\n", "\\n").replace("\t", "\\t");
			str += (tokenStr+"\n");
		}
		str = str.substring(0, str.length()-1);
		return str;
	}
	
	public static String toStringTokenList(List<Token> tokens) {
		return toStringTokenList(tokens.toArray(new Token[0]));
	}
	
	public static String toStringLineList(Line[] lines) {
		String str = "";
		for(Line line : lines) {
			str += line.toString();
			str += "\n\n";
		}
		return str;
	}
	
	public static String toStringLineList(List<Line> lines) {
		return toStringLineList(lines.toArray(new Line[0]));
	}
	
}
