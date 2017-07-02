package com.deckard.qlearning.universe;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;

public interface IRealUniverse<S extends Enum<S> & IState<?>, A extends Enum<A> & IAction> extends IUniverse<S, A> {
	IVirtualUniverse<S, A> virtualize(IAgent<S, A> agent);
}
