package com.deckard.qlearning.policy;

import java.util.List;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IActionSpace;
import com.deckard.qlearning.space.IObservationSpace;
import com.deckard.qlearning.space.IStateSpace;

public interface IPredictor<T extends IStateSpace, B extends IActionSpace> {

	IAction predictAction(IObservationSpace<T> observationSpace);

	void train(List<Transition<T>> listTransition);
}
