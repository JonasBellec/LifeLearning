package com.deckard.qlearning.policy;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IActionSpace;
import com.deckard.qlearning.space.IObservation;
import com.deckard.qlearning.space.IState;

public interface IPolicy<S extends IState, O extends IObservation<S>, A extends IAction, AS extends IActionSpace<A>> {

	A computeBestAction(O observation);
}
