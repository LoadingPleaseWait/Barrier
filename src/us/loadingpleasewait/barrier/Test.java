package us.loadingpleasewait.barrier;

import java.util.ArrayList;

public class Test
{
	static int N = 6;//# of threads
	static int m= 20;//# of elements in input array
	static ArrayList<Double> inputVector = new ArrayList<Double>();
	static ArrayList<Double> solutionVector = new ArrayList<Double>();

	static void threadedNormalize(Barrier b, ArrayList<Double> vector)
	{
		//System.out.println(" \nInput vector is: " + vector);
		vectorNormalize vn = new vectorNormalize(vector, b, N);

		ArrayList<Thread> threads = new ArrayList<Thread>();
		for(int i = 0; i < N; i++)
		{
			Thread t = new Thread(vn);
			t.setName(Integer.toString(i));//Thread name based on its -based index
			t.start();
			threads.add(t);
		}
		for(int i = 0; i < N; i++)
		{
			try
			{
				threads.get(i).join();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		//System.out.println("Output vector is: " + vector);
	}

	public static void main(String[] args)
	{
		int iterations = 30;

		Barrier spin = new spinBarrier(N);
		Barrier mb = new monitorBarrier(N);
		Barrier sb = new semaphoreBarrier(N);
		String barrier_type;
		for(int iteration = 1; iteration <= iterations; iteration++)
		{
			inputVector.clear();
			solutionVector.clear();
			for(int i = 0; i < m; i++)
			{
				   inputVector.add(Double.valueOf(i + iteration + 1));
				solutionVector.add(Double.valueOf(i + iteration + 1));
			}
			//alternately run monitor and semaphore barriers
			if(iteration % 2 == 0)
			{
				barrier_type = "Monitor Barrier";
				threadedNormalize(mb, inputVector);
			}
			else
			{
				barrier_type = "Semaphore Barrier";
				threadedNormalize(sb, inputVector);
			}
			threadedNormalize(spin, solutionVector); 
			if(solutionVector.toString().equals(inputVector.toString()))
			{
				System.out.println("Iteration #" + iteration + ": " + barrier_type + " passed test");
			}
			else
			{
				System.out.println("Iteration #" + iteration + ": " + barrier_type + " failed test");
			}
		}
	}
}
