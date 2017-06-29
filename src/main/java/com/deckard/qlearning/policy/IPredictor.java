package com.deckard.qlearning.policy;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IActionSpace;
import com.deckard.qlearning.space.IStateSpace;
import com.deckard.qlearning.universe.IAgent;
import com.deckard.qlearning.universe.IVirtualUniverse;

public interface IPredictor<T1 extends IStateSpace, T2 extends IStateSpace, B extends IActionSpace> {

	IAction predictAction(IVirtualUniverse<T1> virtualUniverse, IAgent<T2, B> agent);

	void train();
}
