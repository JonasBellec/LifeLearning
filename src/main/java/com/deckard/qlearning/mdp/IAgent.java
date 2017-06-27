package com.deckard.qlearning.mdp;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IActionSpace;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.IStateSpace;

public interface IAgent<S extends IState, A extends IAction> {
	IStateSpace<S> getObservableStateSpace();

	IActionSpace<A> getPossibleActionSpace();
}
