package com.deckard.qlearning.deeplearning;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

import com.deckard.qlearning.policy.IPredictor;
import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IActionSpace;
import com.deckard.qlearning.space.IStateSpace;
import com.deckard.qlearning.universe.IAgent;
import com.deckard.qlearning.universe.IVirtualUniverse;

public class NeuralNetworkPredictor<T1 extends IStateSpace, T2 extends IStateSpace, B extends IActionSpace>
		implements IPredictor<T1, T2, B> {
	MultiLayerNetwork multiLayerNetwork;
	MultiLayerNetwork targetMultiLayerNetwork;

	NeuralNetworkPredictor(MultiLayerConfiguration conf) {
		multiLayerNetwork = new MultiLayerNetwork(conf);
		multiLayerNetwork.init();
		targetMultiLayerNetwork = new MultiLayerNetwork(conf);
		targetMultiLayerNetwork.init();
		targetMultiLayerNetwork.setParams(multiLayerNetwork.params());
	}

	@Override
	public IAction predictAction(IVirtualUniverse<T1> virtualUniverse, IAgent<T2, B> agent) {
		// TODO Auto-generated method stub
		return null;
	}
}
