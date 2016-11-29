package org.xaguzman;

public interface BoardListener {
	public enum EventType{
		ScoreIncreased,
		BoardFull
	}
	void onEvent(EventType event);
}
