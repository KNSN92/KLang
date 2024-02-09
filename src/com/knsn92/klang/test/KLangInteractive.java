package com.knsn92.klang.test;

import java.util.Scanner;

import com.knsn92.klang.KLangInterp;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.variable.Vars;
import com.knsn92.klang.parse.token.Block;
import com.knsn92.klang.parse.token.Tokenizer;

public class KLangInteractive {

	public static void main(String[] args) {
		
		KLangInterp.enableDebugMode();
		
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		
		Vars vars = new Vars();
		
		
		while(true) {
			ErrorHandler.resetError();
			System.out.print(">>> ");
			String code = sc.nextLine();
			if(code.equals("/m")) {
				code = "";
				while(true) {
					System.out.print("... ");
					String line = sc.nextLine();
					if(line.equals("/m")) break;
					code += line+"\n";
				}
			}
			Block block = Tokenizer.splitBlock(code);
			block.setVars(vars);
			KLangInterp.run(block);
		}
	}

}
