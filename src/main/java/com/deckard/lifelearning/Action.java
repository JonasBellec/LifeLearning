package com.deckard.lifelearning;

public class Action {
	public static Action ACTION1 = new Action("ACTION1", Need.NEED1);
	public static Action ACTION2 = new Action("ACTION2", Need.NEED2);
	public static Action ACTION3 = new Action("ACTION3", Need.NEED3);
	public static Action ACTION4 = new Action("ACTION4", Need.NEED4);

	public static Action[] ARRAY_ACTION = new Action[] { ACTION1, ACTION2, ACTION3, ACTION4 };

	private String name;
	private Need need;

	public Action(String name, Need need) {
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
}
