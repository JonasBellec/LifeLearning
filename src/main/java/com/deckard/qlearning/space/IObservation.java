package com.deckard.qlearning.space;

import java.io.Serializable;

public interface IObservation<V extends Serializable, S extends IState<V>> {
	S getObservedState();

	V getObservedValue();
}
