package com.deckard.qlearning.policy;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IActionSpace;
import com.deckard.qlearning.space.IStateSpace;
import com.deckard.qlearning.universe.IAgent;
import com.deckard.qlearning.universe.IRealUniverse;

public interface IPolicy<T extends IStateSpace, B extends IActionSpace> {

	IAction determineActionWithLearning(IRealUniverse<T> realUniverse, IAgent<T, B> agent);

	IAction determineActionWithoutLearning(IRealUniverse<T> realUniverse, IAgent<T, B> agent);
}
