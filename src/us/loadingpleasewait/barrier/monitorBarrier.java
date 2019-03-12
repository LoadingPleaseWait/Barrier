package us.loadingpleasewait.barrier;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class monitorBarrier implements Barrier {
	private int arrived = 0;		// number of threads that have arrived
	private int released = 0;		// number of threads that have been released
	private final int totalThreads;	// number of threads the barrier takes
	
	private final Lock lock = new ReentrantLock();			// lock for the monitor
	private final Condition available = lock.newCondition(); // indicates if barrier is available
	private final Condition barrier = lock.newCondition();	// prevents threads from being released until all threads arrive
	private final Condition done = lock.newCondition();		// indicates when a thread has been released from barrier
	
	public monitorBarrier(int threads) {
		totalThreads = threads;
	}
	
	public void arriveAndWait() {
		lock.lock();
		try {
			while (arrived == totalThreads) {
				available.await();		// put incoming threads to sleep if barrier is full
			}
			arrived++;
			
			if (arrived < totalThreads) {	// if this is not the last thread
				while (arrived < totalThreads) {
					barrier.await();		// put thread to sleep until all threads arrive
				}
				released++;
				done.signal();	// indicate that thread has been released
			} else {
				while (released < totalThreads - 1) {	// ensure all threads have been released
					barrier.signalAll();
					done.await();	// allow other threads to be released
				}
				arrived = 0;
				released = 0;
				available.signalAll();	// now that barrier is reset, incoming threads can enter
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}