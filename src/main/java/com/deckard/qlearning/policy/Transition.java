package com.deckard.qlearning.policy;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.ObservationSpace;

public class Transition<S extends IState, A extends IAction> {
	ObservationSpace<S> observationSpaceSource;
	ObservationSpace<S> observationSpaceTarget;
	A action;
	double reward;

	/**
	 * @return the observationSpaceSource
	 */
	public ObservationSpace<S> getObservationSpaceSource() {
		return observationSpaceSource;
	}

	/**
	 * @param observationSpaceSource
	 *            the observationSpaceSource to set
	 */
	public void setObservationSpaceSource(ObservationSpace<S> observationSpaceSource) {
		this.observationSpaceSource = observationSpaceSource;
	}

	/**
	 * @return the observationSpaceTarget
	 */
	public ObservationSpace<S> getObservationSpaceTarget() {
		return observationSpaceTarget;
	}

	/**
	 * @param observationSpaceTarget
	 *            the observationSpaceTarget to set
	 */
	public void setObservationSpaceTarget(ObservationSpace<S> observationSpaceTarget) {
		this.observationSpaceTarget = observationSpaceTarget;
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
