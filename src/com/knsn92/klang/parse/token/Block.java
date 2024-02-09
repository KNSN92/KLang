package com.knsn92.klang.parse.token;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.collect.Lists;
import com.knsn92.klang.lang.Val;
import com.knsn92.klang.lang.error.ErrorHandler;
import com.knsn92.klang.lang.variable.Vars;
import com.knsn92.klang.parse.line.operator.DummyLineOperator;
import com.knsn92.klang.parse.line.operator.ILineOperator;

public class Block {
	
	public static final int ILLEGAL = -1;
	public static final int LINE = 0;
	public static final int BLOCK = 1;
	
	private List<Object> elements = Lists.newArrayList();
	private Block parent = null;
	private int baseIndent = 0;
	private Vars vars;
	private boolean isFuncBlock = false;
	private Val returnVal = Val.NONE;
	private boolean isModuleBlock = false;
	private String moduleName = null;
	private ILineOperator calledBy = DummyLineOperator.DUMMY;
	
	public Block(Line[] lines, int baseIndent) {
		this.baseIndent = baseIndent;
		vars = new Vars();
		setupElements(lines, baseIndent);
	}
	
	public Block(Line[] lines, Block parent, int baseIndent) {
		this.baseIndent = baseIndent;
		this.parent = parent;
		this.vars = new Vars(parent.vars());
		setupElements(lines, baseIndent);
	}
	
	private void setupElements(Line[] lines, int baseIndent) {
		List<Line> toBlockLineCapacitor = Lists.newArrayList();
		for(Line line : lines) {
			line.block = this;
			if(line.indent > baseIndent) {
				toBlockLineCapacitor.add(line);
				continue;
			}else if(line.indent < baseIndent) {
				ErrorHandler.throwSyntaxError("It includes the following lines of basic indentation line:"+line.line);
			}
			if(!toBlockLineCapacitor.isEmpty()) {
				Block child = new Block(toBlockLineCapacitor.toArray(new Line[0]), this, baseIndent+1);
				elements.add(child);
				toBlockLineCapacitor.clear();
			}
			elements.add(line);
		}
		if(!toBlockLineCapacitor.isEmpty()) {
			Block child = new Block(toBlockLineCapacitor.toArray(new Line[0]), this, baseIndent+1);
			elements.add(child);
			toBlockLineCapacitor.clear();
		}
	}
	
	public Vars vars() {
		return vars;
	}
	
	public void setVars(Vars vars) {
		this.vars = vars;
	}
	
	public Block parent() {
		return parent;
	}
	
	public int baseIndent() {
		return baseIndent;
	}
	
	public boolean isFuncBlock() {
		return isFuncBlock;
	}
	
	public void setFuncBlock() {
		isFuncBlock = true;
	}
	
	public void setModuleBlock(String moduleName) {
		isModuleBlock = true;
		this.moduleName = moduleName;
	}
	
	public boolean isModuleBlock() {
		return isModuleBlock;
	}
	
	public String getModuleName() {
		return moduleName;
	}
	
	public void ret(Val val) {
		returnVal = val;
	}
	
	public Val retVal() {
		return returnVal;
	}
	
	public void removeElement(int index) {
		Validate.validIndex(elements, index);
		elements.remove(index);
	}
	
	public int getLineOrBlock(int index) {
		Validate.validIndex(elements, index);
		Object obj = elements.get(index);
		if(obj instanceof Block) {
			return Block.BLOCK;
		}else if(obj instanceof Line) {
			return LINE;
		}
		return ILLEGAL;
	}
	
	public boolean isLine(int index) {
		return getLineOrBlock(index) == Block.LINE;
	}
	
	public boolean isBlock(int index) {
		return getLineOrBlock(index) == Block.BLOCK;
	}
	
	public Line getLine(int index) {
		Validate.validIndex(elements, index);
		if(isLine(index)) {
			return (Line)elements.get(index);
		}
		return null;
	}
	
	public Block getBlock(int index) {
		Validate.validIndex(elements, index);
		if(isBlock(index)) {
			return (Block)elements.get(index);
		}
		return null;
	}
	
	public int length() {
		return elements.size();
	}
	
	public int deepLength() {
		int len = 0;
		for(int i = 0; i < length(); i++) {
			if(isBlock(i)) {
				len += getBlock(i).deepLength();
			}else {
				len++;
			}
		}
		return len;
	}
	
	public Line[] toLines() {
		Line[] lines = new Line[0];
		for(int i = 0; i < length(); i++) {
			if(isBlock(i)) {
				lines = ArrayUtils.addAll(lines, getBlock(i).toLines());
			}else {
				lines = ArrayUtils.add(lines, getLine(i));
			}
		}
		return lines;
	}
	
	public int indexOf(Line line) {
		for (int i = 0; i < length(); i++) {
			if(!isLine(i)) continue;
			if(getLine(i) != line) continue;
			return i;
		}
		return -1;
	}

	public void setCalledBy(ILineOperator calledBy) {
		this.calledBy = calledBy;
	}
	
	public ILineOperator calledBy() {
		return calledBy;
	}

}
