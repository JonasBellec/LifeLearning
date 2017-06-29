package com.deckard.qlearning.universe;

import com.deckard.qlearning.space.IObservationSpace;
import com.deckard.qlearning.space.IStateSpace;

public interface IUniverse<T extends IStateSpace> {
	IObservationSpace<T> getObservationSpace();
}
