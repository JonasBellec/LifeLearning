package com.deckard.qlearning.predictor;

import java.util.List;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.ObservationSpace;

public interface IPredictor<S extends Enum<S> & IState, A extends Enum<A> & IAction> {

	public A predict(ObservationSpace<S> observationSpace);

	public void updateMultiLayerNetworkFrozen();

	public void train(List<Experience<S, A>> experiences, double discount);
}
