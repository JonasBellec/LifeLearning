package com.deckard.qlearning.universe;

import com.deckard.qlearning.space.IActionSpace;
import com.deckard.qlearning.space.IStateSpace;

public interface IVirtualUniverse<T extends IStateSpace, B extends IActionSpace> extends IUniverse<T, B> {

	IAgent<T, B> getOwner();

	void step();

	void close();

	boolean isDone();
}
