package com.knsn92.klang.parse.token;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.parse.token.type.ITokenType;
import com.knsn92.klang.registry.TokenTypeRegistry;
import com.knsn92.klang.util.TokenUtils;

public class Tokenizer {

	public static final Set<Character> ignore = Sets.newHashSet();
	public static final Set<Character> operator = Sets.newHashSet();
	public static String lineSeparator = "";

	public static Block splitBlock(String code) {
		ErrorHandler.resetStackTrace("(parser)");
		Line[] lines = splitLines(code);
		lines = TokenUtils.lineRemoveEmpty(lines);
		Block block = new Block(lines, 0);
		return block;
	}

	public static Line[] splitLines(String code) {
		String[] lineStrs = code.split(lineSeparator);
		List<Line> lines = Lists.newArrayList();
		int l = 1;
		for (String lineStr : lineStrs) {
			Line dummy = new Line(new Token[0]);
			dummy.line = l;
			ErrorHandler.setLineStackTrace(dummy);
			Token[] tokens = splitWithToken(lineStr);
			if(ErrorHandler.hasError()) return new Line[0];
			Line line = new Line(tokens);
			line.indent = getIndent(lineStr);
			line.line = l;
			line.lineCode = lineStr;
			lines.add(line);
			++l;
		}
		return lines.toArray(new Line[0]);
	}
	
	private static int getIndent(String code) {
		int indent = 0;
		int spaceCount = 0;
		for(char codeChar : code.toCharArray()) {
			if(codeChar == ' ') {
				++spaceCount;
			}else if(codeChar == '\t') {
				++indent;
			}else {
				break;
			}
		}
		return indent+spaceCount/4;
	}

	public static Token[] splitWithToken(String code) {
		String[] splitted = split(code);
		Token[] tokens = new Token[0];
		for (int i = 0; i < splitted.length; i++) {
			Token token = toToken(splitted[i]);
			if(ErrorHandler.hasError()) return new Token[0];
			if (token == null) {
				ErrorHandler.throwInvalidSyntaxFromParser("non classification type \"" + splitted[i] + "\".");
				return new Token[0];
			}
			token.index = i;
			tokens = ArrayUtils.add(tokens, token);
		}
		return tokens;
	}
	
	private static String[] comment(String[] splitted) {
		int hashIdx = ArrayUtils.indexOf(splitted, "#");
		if(hashIdx == -1) return splitted;
		return ArrayUtils.subarray(splitted, 0, hashIdx);
	}

	private static String[] quote(String[] splitted) {
		
		int[] quoteIndexes = new int[0];
		for (int i = 0; i < splitted.length; i++) {
			if(splitted[i].equals("\\\"")) {
				splitted[i] = "\"";
				continue;
			}
			if (!splitted[i].equals("\"")) continue;
			quoteIndexes = ArrayUtils.add(quoteIndexes, i);
		}
		if (quoteIndexes.length <= 0)
			return splitted;
		if (quoteIndexes.length < 2 || quoteIndexes.length % 2 == 1) {
			ErrorHandler.throwInvalidSyntaxFromParser("Number of quarts that cannot be paired");
			return new String[0];
		}
		while (quoteIndexes.length > 0) {
			int start = quoteIndexes[quoteIndexes.length - 2];
			int end = quoteIndexes[quoteIndexes.length - 1];

			quoteIndexes = ArrayUtils.remove(quoteIndexes, quoteIndexes.length - 1);
			quoteIndexes = ArrayUtils.remove(quoteIndexes, quoteIndexes.length - 1);

			String[] innerQuoteElements = ArrayUtils.subarray(splitted, start, end + 1);
			String joinnedQuoteElements = StringUtils.join(innerQuoteElements);

			for (int j = start; j <= end; j++) {
				splitted = ArrayUtils.remove(splitted, start);
			}

			splitted = ArrayUtils.insert(start, splitted, joinnedQuoteElements);
		}
		
		splitted = quoteEscapeSequence(splitted);
		if(ErrorHandler.hasError()) return new String[0];
		
		return splitted;
	}

	private static String[] quoteEscapeSequence(String[] splitted) {
		for (int i = 0; i < splitted.length; i++) {
			
			if(splitted[i].isEmpty()) continue;
			
			if(i != 0 && splitted[i-1].equals("\\")) {
			}else {
				continue;
			}
			String s = "";
			
			switch(splitted[i].charAt(0)) {
			
			case 'b':
				s = "\b";
				break;
			case 't':
				s = "\t";
				break;
			case 'n':
				s = "\n";
				break;
			case 'r':
				s = "\r";
				break;
			case 'f':
				s = "\f";
				break;
			case '\\':
				s = "\\";
				break;
			case '"':
				s = "\"";
				break;
			default:
				ErrorHandler.throwInvalidSyntaxFromParser("A single backslash is not syntactically permitted.");
				return new String[0];
			}
			
			splitted[i-1] = "";
			
			splitted[i] = s;
			
			if(splitted[i].equals("\"")) {
				splitted[i] = "\\\"";
			}
			
			if(splitted[i].equals("\\")) {
				i++;
			}
		}
		return splitted;
	}

	public static String[] split(String code) {
		List<String> tokens = Lists.newArrayList(code);
		for (char ignoreChar : ignore) {
			List<String> copiedTokens = Lists.newArrayList(tokens);
			tokens.clear();
			for (String token : copiedTokens) {
				List<String> splitted = Lists.newArrayList(Arrays.asList(token.split(ignoreChar + "")));
				if (splitted.size() >= 2) {
					List<String> copiedSplitted = Lists.newArrayList(splitted);
					splitted.clear();
					for (String splittedEle : copiedSplitted) {
						splitted.add(splittedEle);
						splitted.add(ignoreChar + "");
					}
					splitted.remove(splitted.size() - 1);
				}
				tokens.addAll(splitted);
			}
		}
		while (tokens.contains("")) {
			tokens.remove(tokens.indexOf(""));
		}
		List<String> copiedTokens = Lists.newArrayList(tokens);
		tokens.clear();
		for (String token : copiedTokens) {
			tokens.addAll(Arrays.asList(splitNonIgnore(token)));
		}
		String[] splitted = tokens.toArray(new String[0]);
		splitted = quote(splitted);
		splitted = comment(splitted);
		return splitted;
	}

	private static String[] splitNonIgnore(String code) {
		String[] tokens = new String[0];
		String token = "";
		if (code.length() <= 1) {
			return new String[] { code };
		}
		token += code.charAt(0);
		for (int i = 1; i < code.length(); i++) {
			char before = code.charAt(i - 1);
			char after = code.charAt(i);
			if (canSplit(before, after)) {
				tokens = ArrayUtils.add(tokens, token);
				token = "";
			}
			token += code.charAt(i);
		}
		tokens = ArrayUtils.add(tokens, token);
		return tokens;
	}

	private static boolean canSplit(char before, char next) {
		return (operator.contains(before) || operator.contains(next))
				|| (Character.isDigit(before) && Character.isAlphabetic(next));
	}

	private static ITokenType getType(String token) {
		for (ITokenType type : TokenTypeRegistry.getTypes()) {
			if (type.mathes(token)) {
				return type;
			}
		}
		return null;
	}

	private static Token toToken(String token) {
		ITokenType type = getType(token);
		if (type == null) {
			return null;
		}
		return type.newToken(token);
	}

}
