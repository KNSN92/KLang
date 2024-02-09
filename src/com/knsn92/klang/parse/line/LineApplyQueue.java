package com.knsn92.klang.parse.line;

import java.util.ArrayDeque;
import java.util.Deque;

import com.knsn92.klang.parse.line.operator.DummyLineOperator;
import com.knsn92.klang.parse.line.operator.ILineOperator;

public class LineApplyQueue {
	
	public static final DummyLineOperator DUMMY = DummyLineOperator.DUMMY;
	
	private Deque<Integer> nextLineQueue = new ArrayDeque<>();
	private Deque<Integer> nextLineAdditionalInformationQueue = new ArrayDeque<>();
	private Deque<ILineOperator> nextLineSetterLineOperatorQueue = new ArrayDeque<>();
	
	private int lineIdx = 0;
	private Integer latestAdditionalInformation = -1;
	private ILineOperator latestLineOperator = DummyLineOperator.DUMMY;
	
	
	public void addNextLine(int nextLineOffset, int additionalInfo, ILineOperator by) {
		nextLineQueue.offer(nextLineOffset);
		nextLineAdditionalInformationQueue.offer(additionalInfo);
		nextLineSetterLineOperatorQueue.offer(by);
	}
	
	public void addNextLine(int nextLineOffset, ILineOperator by) {
		nextLineQueue.offer(nextLineOffset);
		nextLineAdditionalInformationQueue.offer(0);
		nextLineSetterLineOperatorQueue.offer(by);
	}
	
	public void next() {
		if(nextLineQueue.isEmpty()) {
			lineIdx++;
			latestAdditionalInformation = 0;
			latestLineOperator = DummyLineOperator.DUMMY;
			return;
		}
		lineIdx += nextLineQueue.poll();
		latestAdditionalInformation = nextLineAdditionalInformationQueue.poll();
		latestLineOperator = nextLineSetterLineOperatorQueue.poll();
	}
	
	public int lineIdx() {
		return lineIdx;
	}
	
	public int additionalInfo() {
		return latestAdditionalInformation;
	}
	
	public ILineOperator queueSetter() {
		return latestLineOperator;
	}

}
