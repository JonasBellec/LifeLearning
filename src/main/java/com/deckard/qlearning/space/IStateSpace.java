package com.deckard.qlearning.space;

public interface IStateSpace<S extends IState> extends Iterable<S> {
	int size();
}
