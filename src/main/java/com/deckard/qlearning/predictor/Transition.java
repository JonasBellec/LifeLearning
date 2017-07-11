package com.deckard.qlearning.predictor;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.ObservationSpace;

public class Transition<S extends IState, A extends IAction> {
	ObservationSpace<S> observationSpacePrevious;
	ObservationSpace<S> observationSpaceNext;
	A action;
	double reward;

	/**
	 * @return the observationSpacePrevious
	 */
	public ObservationSpace<S> getObservationSpacePrevious() {
		return observationSpacePrevious;
	}

	/**
	 * @param observationSpacePrevious
	 *            the observationSpacePrevious to set
	 */
	public void setObservationSpacePrevious(ObservationSpace<S> observationSpacePrevious) {
		this.observationSpacePrevious = observationSpacePrevious;
	}

	/**
	 * @return the observationSpaceNext
	 */
	public ObservationSpace<S> getObservationSpaceNext() {
		return observationSpaceNext;
	}

	/**
	 * @param observationSpaceNext
	 *            the observationSpaceNext to set
	 */
	public void setObservationSpaceNext(ObservationSpace<S> observationSpaceNext) {
		this.observationSpaceNext = observationSpaceNext;
	}

	/**
	 * @return the action
	 */
	public A getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(A action) {
		this.action = action;
	}

	/**
	 * @return the reward
	 */
	public double getReward() {
		return reward;
	}

	/**
	 * @param reward
	 *            the reward to set
	 */
	public void setReward(double reward) {
		this.reward = reward;
	}
}
