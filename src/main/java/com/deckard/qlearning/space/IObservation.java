package com.deckard.qlearning.space;

public interface IObservation<S extends IState> extends Iterable<S> {
	int size();
}
