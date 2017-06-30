package com.deckard.qlearning.space;

public interface IObservationSpace<T extends IStateSpace> extends Iterable<IObservation<?, ?>> {
	double[] encode();

	T getObservedStateSpace();
}
