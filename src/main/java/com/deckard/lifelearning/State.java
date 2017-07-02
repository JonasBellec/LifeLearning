package com.deckard.lifelearning;

import com.deckard.qlearning.space.IState;

public enum State implements IState {

	NEED1("need1"), NEED2("need1"), NEED3("need1"), HAPPINESS("happiness"), DAY("day"), HOUR("hour");

	private String name;

	private State(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int encode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Class getAssociatedValueClass() {
		// TODO Auto-generated method stub
		return null;
	}
}
