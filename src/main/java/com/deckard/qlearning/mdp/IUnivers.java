package com.deckard.qlearning.mdp;

import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.IStateSpace;

public interface IUnivers<S extends IState> {
	IStateSpace<S> getObservableStateSpace();
}
