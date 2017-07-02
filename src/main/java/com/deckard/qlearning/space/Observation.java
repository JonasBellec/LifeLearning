package com.deckard.qlearning.space;

import java.io.Serializable;

public class Observation<V extends Serializable, S extends IState> {

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
