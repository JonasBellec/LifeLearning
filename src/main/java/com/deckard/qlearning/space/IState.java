package com.deckard.qlearning.space;

public interface IState extends IEncodable {
	String getName();

	boolean isUniverseState();

	boolean isAgentState();
}
