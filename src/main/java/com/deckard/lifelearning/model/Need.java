package com.deckard.lifelearning.model;

public class Need {
	public static Need NEED1 = new Need("NEED1");
	public static Need NEED2 = new Need("NEED2");
	public static Need NEED3 = new Need("NEED3");
	public static Need NEED4 = new Need("NEED4");

	private String name;

	private Need(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Need [name=" + name + "]";
	}
}
