package com.deckard.qlearning.predictor;

import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IState;
import com.deckard.qlearning.space.ObservationSpace;

public class Experience<S extends IState, A extends IAction> {
	private ObservationSpace<S> observationSpacePrevious;
	private ObservationSpace<S> observationSpaceNext;

	private A action;

	private Integer happinessBefore;
	private Integer happinessAfter;

	private Integer probability;

	public Experience() {
		super();
		this.probability = 1;
	}

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
	 * @return the happinessBefore
	 */
	public Integer getHappinessBefore() {
		return happinessBefore;
	}

	/**
	 * @param happinessBefore
	 *            the happinessBefore to set
	 */
	public void setHappinessBefore(Integer happinessBefore) {
		this.happinessBefore = happinessBefore;
	}

	/**
	 * @return the happinessAfter
	 */
	public Integer getHappinessAfter() {
		return happinessAfter;
	}

	/**
	 * @param happinessAfter
	 *            the happinessAfter to set
	 */
	public void setHappinessAfter(Integer happinessAfter) {
		this.happinessAfter = happinessAfter;
	}

	/**
	 * @return the probability
	 */
	public Integer getProbability() {
		return probability;
	}

	/**
	 * @param probability
	 *            the probability to set
	 */
	public void setProbability(Integer probability) {
		this.probability = probability;
	}
}
