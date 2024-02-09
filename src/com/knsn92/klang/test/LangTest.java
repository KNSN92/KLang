package com.knsn92.klang.test;

import java.io.File;

import com.knsn92.klang.KLangInterp;

public class LangTest {

	public static void main(String[] args) {
		KLangInterp.enableDebugMode();
//		KLangInterp.run(new File("./test.txt"));
		KLangInterp.run(new File("./break_continue_test.txt"));
//		KLangInterp.run(new File("./tictactoe.txt"));
	}

}
