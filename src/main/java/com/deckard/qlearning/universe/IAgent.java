package com.deckard.qlearning.universe;

import java.util.List;

import com.deckard.qlearning.predictor.Experience;
import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.ObservationSpace;

public interface IAgent<S extends Enum<S> & IState, A extends Enum<A> & IAction> {
	ObservationSpace<S> getObservationSpace(List<S> states);

	void life();

	void act(A action);

	Integer computeHappiness();

	boolean isAlive();

	void addToMemory(Experience<S, A> experience);

	Experience<S, A> getLastExperience();
}
