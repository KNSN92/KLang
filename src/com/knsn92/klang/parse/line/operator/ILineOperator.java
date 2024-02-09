package com.knsn92.klang.parse.line.operator;

import com.knsn92.klang.parse.line.LineApplyQueue;
import com.knsn92.klang.parse.token.Block;
import com.knsn92.klang.parse.token.Line;

public interface ILineOperator {
	
	public boolean matches(Line line, Block block, int lineidx);
	
	public boolean apply(Line line, Block block, int lineidx, LineApplyQueue queue);

}
