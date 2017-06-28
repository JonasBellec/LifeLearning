package com.deckard.qlearning.universe;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IActionSpace;
import com.deckard.qlearning.space.IObservation;
import com.deckard.qlearning.space.IState;

public interface IAgent<S extends IState, A extends IAction> {
	IObservation<S> getObservedStateSpace();

	IActionSpace<A> getPossibleActionSpace();
}
