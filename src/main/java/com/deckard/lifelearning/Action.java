package com.deckard.lifelearning;

import com.deckard.qlearning.space.IAction;

public enum Action implements IAction {
	ACTION1("ACTION1", Need.NEED1),
	ACTION2("ACTION2", Need.NEED2),
	ACTION3("ACTION3", Need.NEED3),
	ACTION4("ACTION4", Need.NEED4);

	private String name;
	private Need need;

	private Action(String name, Need need) {
		this.name = name;
		this.need = need;
	}

	public Need getNeed() {
		return need;
	}

	public void setNeed(Need need) {
		this.need = need;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int encode() {
		// TODO Auto-generated method stub
		return 0;
	}
}
