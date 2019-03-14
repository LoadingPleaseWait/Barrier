package us.loadingpleasewait.barrier;

import java.util.ArrayList;
import java.util.concurrent.atomic.DoubleAdder;
import java.lang.Math;

public class vectorNormalize implements Runnable
{
	ArrayList<Double> vector;
	Barrier b;
	int N;
	DoubleAdder mag_sq = new DoubleAdder();
	
	public vectorNormalize(ArrayList<Double> inputVector, Barrier barrier, int N)
	{
		this.N = N;
		b = barrier;
		vector = inputVector;
	}	

	public void run()
	{
		//id is 0 to N-1
		int id = Integer.parseInt(Thread.currentThread().getName());
		
		for(int i = id; i < vector.size(); i+= N)
		{
			double value = vector.get(i);
			mag_sq.add(value * value);
		}
		
		b.arriveAndWait();
		
		for(int i = id; i < vector.size(); i += N)
		{
			vector.set(i, vector.get(i)/Math.sqrt(mag_sq.sum()));
		}

		b.arriveAndWait();
	}
}
