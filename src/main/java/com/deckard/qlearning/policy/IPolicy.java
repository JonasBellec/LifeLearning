package com.deckard.qlearning.policy;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.universe.IAgent;
import com.deckard.qlearning.universe.IUniverse;

public interface IPolicy<S extends Enum<S> & IState, A extends Enum<A> & IAction> {
	A determineAction(IUniverse<S, A> universe, IAgent<S, A> agent);

	public void learnFromCollectiveMemory();
}
