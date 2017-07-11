package com.deckard.lifelearning.model;

import com.deckard.qlearning.space.IState;

public enum State implements IState {

	NEED1("need1", false),
	NEED2("need2", false),
	NEED3("need3", false),
	DAY("day", true),
	HOUR("hour", true);

	private String name;
	private boolean universeState;

	private State(String name, boolean universeState) {
		this.name = name;
		this.universeState = universeState;
	}

	@Override
	public int encode() {
		return ordinal();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isUniverseState() {
		return universeState;
	}

	@Override
	public boolean isAgentState() {
		return !universeState;
	}
}
