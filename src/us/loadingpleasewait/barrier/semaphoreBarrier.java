package us.loadingpleasewait.barrier;

import java.util.concurrent.Semaphore;

public class semaphoreBarrier implements Barrier {
	private int count = 0;			// number of threads that have arrived
	private final int totalThreads; // number of threads the barrier takes

	private final Semaphore barrier = new Semaphore(0); // acts as barrier
	private final Semaphore exit = new Semaphore(0);	// indicates that thread has exited barrier
	private final Semaphore mutex = new Semaphore(1);	// provides mutual exclusion among threads

	public semaphoreBarrier(int threads) {
		totalThreads = threads;
	}

	public void arriveAndWait() {
		try {
			mutex.acquire();
			count++;

			if (count < totalThreads) {	// if this is not the last thread
				mutex.release();
				barrier.acquire();		// wait for barrier to be released
				exit.release(); 	// indicate that thread has exited barrier
			} else { // if this is the last thread
				barrier.release(totalThreads - 1);	// release threads waiting on barrier
				exit.acquire(totalThreads - 1);	// ensure all threads have exited barrier
				count = 0;
				mutex.release(); // barrier has been reset, so we allow new threads to enter barrier
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}
