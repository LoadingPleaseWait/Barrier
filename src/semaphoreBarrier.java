

import java.util.concurrent.Semaphore;

/**
 * This is a barrier data structure implemented using Java Semaphore
 * objects. The barrier is initialized to hold N threads. Whenever a
 * thread calls the arriveAndWait() method, it is blocked until
 * a total of N threads have called arriveAndWait(), at which time all the
 * blocked threads are allowed to continue. The barrier is reusable.
 */
public class semaphoreBarrier implements Barrier {
	private int count = 0;			// number of threads that have arrived
	private final int totalThreads; // number of threads the barrier takes

	private final Semaphore barrier = new Semaphore(0); // acts as barrier
	private final Semaphore exit = new Semaphore(0);	// indicates that thread has exited barrier
	private final Semaphore mutex = new Semaphore(1);	// provides mutual exclusion among threads

	/**
	 * Constructor that sets the total number of threads
	 * 
	 * @param threads - the number of threads that must call
	 * arriveAndWait() in order to release the barrier
	 * 
	 * @throws IllegalArgumentException if threads < 1
	 */
	public semaphoreBarrier(int threads) {
		if (threads < 1) {
			throw new IllegalArgumentException();
		}
		totalThreads = threads;
	}

	/**
	 * When a thread calls this method, it is blocked until
	 * the total number of threads required by the barrier
	 * also call this method.
	 */
	public void arriveAndWait() {
		try {
			mutex.acquire();
			count++;

			if (count < totalThreads) {	// if this is not the last thread
				mutex.release();		// allow new threads to enter barrier
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
