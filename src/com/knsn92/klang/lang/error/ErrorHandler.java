package com.knsn92.klang.lang.error;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Deque;

import com.knsn92.klang.KLangInterp;
import com.knsn92.klang.parse.ApplyHandler;
import com.knsn92.klang.parse.token.Line;

public class ErrorHandler {
	
	private static Deque<KStackTraceElement> stackTrace = new ArrayDeque<>();
	private static PrintStream defaultStream = System.err;
	private static boolean hasError = false;
	
	static {
		resetStackTrace("(code)");
		hasError = false;
	}
	
	static class KStackTraceElement {
		public String name;
		public Line lineObj;
		public int line;
		public KStackTraceElement(String name, Line lineObj, int line) {
			this.name = name;
			this.lineObj = lineObj;
			this.line = line;
		}
	}
	
	public static void throwInvalidSyntaxFromParser(String errorText) {
		defaultStream.println("Invalid Syntax: "+errorText);
		printStackTrace();
		if(KLangInterp.isDebugMode()) {
			defaultStream.println("\n[Debug Stack Trace]");
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
			for(StackTraceElement ele:stacktrace) {
				defaultStream.println(ele);
			}
		}
		hasError = true;
		ApplyHandler.exit();
	}
	
	public static void throwFatalErrror() {
		defaultStream.println("Fatal error. Most likely a bug. Please report it.");
		printStackTrace();
	}
	
	public static void throwInvalidSyntax() {
		throwSyntaxError("Invalid Syntax");
	}
	
	public static void throwSyntaxError(String errorText) {
		throwError("SyntaxError", errorText);
	}
	
	public static void throwError(String errorName, String errorText) {
		defaultStream.println(errorName+": "+errorText);
		printStackTrace();
		defaultStream.println("\n==========\n");
		defaultStream.println(stackTrace.peek().line+":"+stackTrace.peek().lineObj.lineCode);
		defaultStream.println("\n==========");
		if(KLangInterp.isDebugMode()) {
			defaultStream.println("\n[Debug Stack Trace]");
			StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
			for(StackTraceElement ele:stacktrace) {
				defaultStream.println(ele);
			}
		}
		hasError = true;
		ApplyHandler.exit();
	}
	
	public static void pushStackTrace(String name, Line line) {
		stackTrace.push(new KStackTraceElement(name, line, line.line));
	}
	
	public static void popStackTrace() {
		if(stackTrace.size() <= 1) return;
		stackTrace.pop();
	}
	
	public static void setLineStackTrace(Line nextline) {
		if(stackTrace.isEmpty()) return;
		KStackTraceElement ele = stackTrace.pop();
		ele.lineObj = nextline;
		ele.line = nextline.line;
		stackTrace.push(ele);
	}
	
	public static void resetStackTrace(String name) {
		stackTrace.clear();
		stackTrace.push(new KStackTraceElement(name,null, 0));
	}
	
	public static Deque<KStackTraceElement> getStackTrace() {
		return new ArrayDeque<KStackTraceElement>(stackTrace);
	}
	
	public static void loadStackTrace(Deque<KStackTraceElement> stacktrace) {
		stackTrace = stacktrace;
	}
	
	public static void printStackTrace() {
		for(KStackTraceElement ele:stackTrace) {
			defaultStream.println("line:"+ele.line+" in "+ele.name);
		}
	}
	
	public static void setDefaultStream(PrintStream stream) {
		defaultStream = stream;
	}
	
	public static PrintStream getDefaultStream() {
		return defaultStream;
	}
	
	public static boolean hasError() {
		return hasError;
	}
	
	public static void resetError() {
		hasError = false;
	}

}
