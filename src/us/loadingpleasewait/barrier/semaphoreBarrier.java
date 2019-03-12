package us.loadingpleasewait.barrier;

import java.util.concurrent.Semaphore;

public class semaphoreBarrier implements Barrier {
	private final Semaphore barrier = new Semaphore(0);
	private final Semaphore mutex = new Semaphore(1);
	private final Semaphore released = new Semaphore(0);

	private final int totalThreads; // the number of threads the barrier takes
	private int count = 0;

	public semaphoreBarrier(int threads) {
		totalThreads = threads;
	}

	public void arriveAndWait() {
		try {
			mutex.acquire();
			count++;

			if (count < totalThreads) {
				mutex.release();
				barrier.acquire();
				released.release(); // indicate that thread has escaped barrier
			} else { // if count == TOTAL, we don't signal the mutex until we are finished(to prevent more threads from entering the barrier prematurely)
				barrier.release(totalThreads - 1);
				released.acquire(totalThreads - 1);
				count = 0;
				mutex.release(); // we have finished, and barrier can now be reused,
				// so we allow new threads to enter the barrier
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}
