package us.loadingpleasewait.barrier;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class monitorBarrier implements Barrier
{
	/*
	 *	Private members and methods here
	 */
	private int arrived;
	private int released;
	private int total;
	
	private final Lock lock;
	private final Condition available;
	private final Condition barrier;
	private final Condition done;
	
	public monitorBarrier(int N)
	{
		arrived = 0;
		released = 0;
		total = N;
		
		lock = new ReentrantLock();
		available = lock.newCondition();
		barrier = lock.newCondition();
		done = lock.newCondition();
	}
	
	public void arriveAndWait()
	{
		lock.lock();
		try {
			while (arrived == total) {
				available.await();
			}
			arrived++;
			
			if (arrived < total) {
				while (arrived < total) {
					barrier.await();
				}
				released++;
				done.signal();
			} else {
				while (released < total - 1) {
					barrier.signalAll();
					done.await();
				}
				arrived = 0;
				released = 0;
				available.signalAll();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}