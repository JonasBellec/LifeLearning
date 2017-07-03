package com.deckard.qlearning.space;

import java.io.Serializable;

public class Observation<S extends IState, V extends Serializable> {

	private S state;
	private V value;

	public Observation(S state, V value) {
		this.state = state;
		this.value = value;
	}

	public S getObservedState() {
		return state;
	}

	public V getObservedValue() {
		return value;
	}
}
