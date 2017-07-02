package com.deckard.qlearning.policy;

import java.util.List;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.ObservationSpace;

public interface IPredictor<S extends Enum<S> & IState, A extends Enum<A> & IAction> {

	IAction predictAction(ObservationSpace<S> observationSpace);

	void train(List<Transition<S, A>> listTransition);
}
