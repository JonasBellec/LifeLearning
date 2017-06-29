package com.deckard.qlearning.deeplearning;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

import com.deckard.qlearning.policy.IPredictor;
import com.deckard.qlearning.space.IAction;
import com.deckard.qlearning.space.IActionSpace;
import com.deckard.qlearning.space.IStateSpace;
import com.deckard.qlearning.universe.IAgent;
import com.deckard.qlearning.universe.IVirtualUniverse;

public class NeuralNetworkPredictor<T1 extends IStateSpace, T2 extends IStateSpace, B extends IActionSpace>
		implements IPredictor<T1, T2, B> {
	MultiLayerNetwork multiLayerNetworkSource;
	MultiLayerNetwork multiLayerNetworkTarget;

	NeuralNetworkPredictor(MultiLayerConfiguration conf) {
		multiLayerNetworkSource = new MultiLayerNetwork(conf);
		multiLayerNetworkSource.init();
		multiLayerNetworkTarget = new MultiLayerNetwork(conf);
		multiLayerNetworkTarget.init();
		multiLayerNetworkTarget.setParams(multiLayerNetworkSource.params());
	}

	@Override
	public IAction predictAction(IVirtualUniverse<T1> virtualUniverse, IAgent<T2, B> agent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void train() {
		// TODO Auto-generated method stub

		Replay replays[] = GetMiniBatch(BatchSize);
		INDArray CurrInputs = CombineInputs(replays);
		INDArray TargetInputs = CombineNextInputs(replays);

		float TotalError = 0;

		INDArray CurrOutputs = DeepQ.output(CurrInputs);
		INDArray TargetOutputs = TargetDeepQ.output(TargetInputs);
		float y[] = new float[replays.length];
		for (int i = 0; i < y.length; i++) {
			int ind[] = { i, replays[i].Action };
			float FutureReward = 0;
			if (replays[i].NextInput != null)
				FutureReward = FindMax(TargetOutputs.getRow(i), replays[i].NextActionMask);
			float TargetReward = replays[i].Reward + Discount * FutureReward;
			TotalError += (TargetReward - CurrOutputs.getFloat(ind)) * (TargetReward - CurrOutputs.getFloat(ind));
			CurrOutputs.putScalar(ind, TargetReward);
		}
		// System.out.println("Avgerage Error: " + (TotalError / y.length) );

		DeepQ.fit(CurrInputs, CurrOutputs);

		multiLayerNetworkSource.fit(data, labels);
	}
}
