package com.deckard.qlearning.universe;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IObservation;
import com.deckard.qlearning.space.IState;

public interface IUniverse<S extends IState, A extends IAction> {
	IObservation<S> getObservation();
}
