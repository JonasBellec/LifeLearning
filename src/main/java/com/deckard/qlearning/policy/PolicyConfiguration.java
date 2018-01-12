package com.deckard.qlearning.policy;

import com.deckard.qlearning.space.ActionSpace;
import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.StateSpace;

public class PolicyConfiguration<S extends Enum<S> & IState, A extends Enum<A> & IAction> {

	private StateSpace<S> stateSpace;
	private ActionSpace<A> actionSpace;

	private double epsilon;
	private double discount;

	public PolicyConfiguration(Class<S> classState, Class<A> classAction, double epsilon, double discount) {
		this.stateSpace = StateSpace.getInstance(classState);
		this.actionSpace = ActionSpace.getInstance(classAction);

		this.epsilon = epsilon;
		this.discount = discount;
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
	 * @return the discount
	 */
	public double getDiscount() {
		return discount;
	}

	/**
	 * @param discount
	 *            the discount to set
	 */
	public void setDiscount(double discount) {
		this.discount = discount;
	}
}
