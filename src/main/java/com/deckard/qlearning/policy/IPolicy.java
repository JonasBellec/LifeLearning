package com.deckard.qlearning.policy;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.universe.IAgent;
import com.deckard.qlearning.universe.IRealUniverse;

public interface IPolicy<S extends Enum<S> & IState<?>, A extends Enum<A> & IAction> {

	IAction determineActionWithLearning(IRealUniverse<S, A> realUniverse, IAgent<S, A> agent);

	IAction determineActionWithoutLearning(IRealUniverse<S, A> realUniverse, IAgent<S, A> agent);
}
