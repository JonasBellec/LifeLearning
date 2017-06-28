package com.deckard.qlearning.universe;

import com.deckard.qlearning.space.IActionSpace;
import com.deckard.qlearning.space.IStateSpace;

public interface IRealUniverse<T extends IStateSpace, B extends IActionSpace> extends IUniverse<T, B> {
	IVirtualUniverse<T, B> virtualize(IAgent<T, B> agent);
}
