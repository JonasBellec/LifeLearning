package com.deckard.qlearning.policy;

import java.util.List;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IActionSpace;
import com.deckard.qlearning.space.IStateSpace;
import com.deckard.qlearning.universe.IAgent;
import com.deckard.qlearning.universe.IVirtualUniverse;

public interface IPredictor<T extends IStateSpace, B extends IActionSpace> {

	IAction predictAction(IVirtualUniverse<T> virtualUniverse, IAgent<T, B> agent);

	void train(List<Transition<T>> listTransition);
}
