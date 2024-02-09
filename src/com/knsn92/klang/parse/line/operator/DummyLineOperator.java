package com.knsn92.klang.parse.line.operator;

import com.knsn92.klang.parse.line.LineApplyQueue;
import com.knsn92.klang.parse.token.Block;
import com.knsn92.klang.parse.token.Line;

public class DummyLineOperator implements ILineOperator {
		
	public static final DummyLineOperator DUMMY = new DummyLineOperator();

	@Override
	public boolean matches(Line line, Block block, int lineidx) {
		return false;
	}

	@Override
	public boolean apply(Line line, Block block, int lineidx, LineApplyQueue queue) {
		return true;
	}
	

}
