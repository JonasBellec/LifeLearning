package com.deckard.qlearning.policy;

import com.deckard.qlearning.space.ActionSpace;
import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.StateSpace;

public class PolicyConfiguration<S extends Enum<S> & IState, A extends Enum<A> & IAction> {

	private StateSpace<S> stateSpace;
	private ActionSpace<A> actionSpace;

	public PolicyConfiguration(Class<S> classState, Class<A> classAction) {
		this.stateSpace = StateSpace.getInstance(classState);
		this.actionSpace = ActionSpace.getInstance(classAction);
	}

	public double getEpsilon() {
		return 0.2d;
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

}
