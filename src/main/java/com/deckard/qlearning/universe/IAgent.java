package com.deckard.qlearning.universe;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.ObservationSpace;

public interface IAgent<S extends Enum<S> & IState, A extends Enum<A> & IAction> {
	ObservationSpace<S> getObservationSpace();
}
