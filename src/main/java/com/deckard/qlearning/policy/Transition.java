package com.deckard.qlearning.policy;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IObservationSpace;
import com.deckard.qlearning.space.IStateSpace;

public class Transition<T extends IStateSpace> {
	IObservationSpace<T> observationSpaceSource;
	IObservationSpace<T> observationSpaceTarget;
	IAction action;
	double reward;

	/**
	 * @return the observationSpaceSource
	 */
	public IObservationSpace<T> getObservationSpaceSource() {
		return observationSpaceSource;
	}

	/**
	 * @param observationSpaceSource
	 *            the observationSpaceSource to set
	 */
	public void setObservationSpaceSource(IObservationSpace<T> observationSpaceSource) {
		this.observationSpaceSource = observationSpaceSource;
	}

	/**
	 * @return the observationSpaceTarget
	 */
	public IObservationSpace<T> getObservationSpaceTarget() {
		return observationSpaceTarget;
	}

	/**
	 * @param observationSpaceTarget
	 *            the observationSpaceTarget to set
	 */
	public void setObservationSpaceTarget(IObservationSpace<T> observationSpaceTarget) {
		this.observationSpaceTarget = observationSpaceTarget;
	}

	/**
	 * @return the action
	 */
	public IAction getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(IAction action) {
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
