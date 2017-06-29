package com.deckard.qlearning.universe;

import com.deckard.qlearning.space.IStateSpace;

public interface IRealUniverse<T extends IStateSpace> extends IUniverse<T> {
	IVirtualUniverse<T> virtualize(IAgent<?, ?> agent);
}
