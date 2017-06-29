package com.deckard.qlearning.policy;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IObservationSpace;
import com.deckard.qlearning.space.IStateSpace;

public class Transition<T1 extends IStateSpace, T2 extends IStateSpace> {
	IObservationSpace<T1> observationSpaceSource;
	IObservationSpace<T1> observationSpaceTarget;
	IObservationSpace<T2> observationSpaceSource;
	IObservationSpace<T2> observationSpaceTarget;
	IAction action;
	double reward;
}
