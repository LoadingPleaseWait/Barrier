import java.util.concurrent.atomic.AtomicInteger;

public class spinBarrier implements Barrier
{
	int N;
	AtomicInteger arrived = new AtomicInteger(0);
	AtomicInteger leaving = new AtomicInteger(0);
	Boolean signal = false;
	public spinBarrier(int N)
	{
		this.N = N;
	}

	public void arriveAndWait()
	{
		while(leaving.get() > 0);//wait for all processes to leave
		if(arrived.incrementAndGet() == N)
		{
			leaving.set(N);//set value so that porcesses can decrement while leaving
			arrived.set(0);//reset values for next use
		}
		else
		{
			while(arrived.get() > 0);//wait till arrived is set to 0 by last process
		}
		leaving.decrementAndGet();
	}
}
