package com.deckard.lifelearning.universe;

import java.util.List;

import com.deckard.lifelearning.model.Action;
import com.deckard.lifelearning.model.State;
import com.deckard.qlearning.space.Observation;
import com.deckard.qlearning.space.ObservationSpace;
import com.deckard.qlearning.universe.IAgent;
import com.deckard.qlearning.universe.IVirtualUniverse;

public class VirtualUniverse implements IVirtualUniverse<State, Action> {

	private Time time;
	private IAgent<State, Action> owner;
	private IAgent<State, Action> virtualOwner;

	private ObservationSpace<State> observationSpace;

	public VirtualUniverse(RealUniverse realUniverse, IAgent<State, Action> owner) {
		this.owner = owner;
		this.time = new Time(realUniverse.getTime());
		this.virtualOwner = owner.virtualize();
		this.observationSpace = new ObservationSpace<>();
	}

	@Override
	public void step() {
		time.tick();
		virtualOwner.life();
	}

	@Override
	public ObservationSpace<State> getObservationSpace(List<State> states) {
		observationSpace.clear();
		observationSpace.add(new Observation<State, Integer>(State.DAY, time.getDay()));
		observationSpace.add(new Observation<State, Integer>(State.HOUR, time.getHour()));

		return observationSpace;
	}

	@Override
	public IAgent<State, Action> getOwner() {
		return owner;
	}

	/**
	 * @return the virtualOwner
	 */
	@Override
	public IAgent<State, Action> getVirtualOwner() {
		return virtualOwner;
	}
}
