package com.deckard.qlearning.policy;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IActionSpace;
import com.deckard.qlearning.space.IStateSpace;
import com.deckard.qlearning.universe.IAgent;
import com.deckard.qlearning.universe.IRealUniverse;

public interface IPolicy<T extends IStateSpace, B extends IActionSpace> {

	IAction computeBestAction(IRealUniverse<T, B> realUniverse, IAgent<T, B> agent);
}
