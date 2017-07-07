package com.deckard.qlearning.policy;

import com.deckard.qlearning.space.ActionSpace;
import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.StateSpace;

public class PolicyConfiguration<S extends Enum<S> & IState, A extends Enum<A> & IAction> {

	private StateSpace<S> stateSpace;
	private ActionSpace<A> actionSpace;

	private int projectionDeep;
	private double epsilon;

	public PolicyConfiguration(Class<S> classState, Class<A> classAction, int projectionDeep, double epsilon) {
		this.stateSpace = StateSpace.getInstance(classState);
		this.actionSpace = ActionSpace.getInstance(classAction);

		this.projectionDeep = projectionDeep;
		this.epsilon = epsilon;
	}

	public double getEpsilon() {
		return epsilon;
	}

	/**
	 * @return the stateSpace
	 */
	public StateSpace<S> getStateSpace() {
		return stateSpace;
	}

	/**
	 * @return the actionSpace
	 */
	public ActionSpace<A> getActionSpace() {
		return actionSpace;
	}

	/**
	 * @return the projectionDeep
	 */
	public int getProjectionDeep() {
		return projectionDeep;
	}

}
