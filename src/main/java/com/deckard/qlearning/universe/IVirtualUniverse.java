package com.deckard.qlearning.universe;

import com.deckard.qlearning.space.IStateSpace;

public interface IVirtualUniverse<T extends IStateSpace> extends IUniverse<T> {

	IAgent<?, ?> getOwner();

	void step();

	void close();

	boolean isDone();
}
