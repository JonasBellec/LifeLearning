package com.deckard.qlearning.mdp;

import com.deckard.qlearning.space.IState;

public interface IRealUnivers<S extends IState> extends IUnivers<S> {
	IVirtualUnivers<S> virtualize();
}
