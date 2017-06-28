package com.deckard.qlearning.space;

import java.io.Serializable;

public interface IState<V extends Serializable> {
	String getName();

	Class<V> getAssociatedValueClass();
}
