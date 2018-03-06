package cs455.scaling.threadpool;

import java.util.LinkedList;

public class FixedThreadPool {

    private final LinkedList<WorkerThread> threadPool;
    private final boolean debug;
    
    public FixedThreadPool(int threadPoolSize, boolean debug) {
	this.debug = debug;
	threadPool = new LinkedList<WorkerThread>();
	for (int i=0; i<threadPoolSize; i++) {
	    threadPool.add(new WorkerThread(this, debug));
	}
    }

    public void initialize() {
	for (WorkerThread thread : threadPool) {
	    thread.start();
	}
    }
    
    public WorkerThread retrieveSpareWorker() {
	// allows a spare worker thread to be retrieved
	WorkerThread spareThread = null;
	synchronized(threadPool) {
	    spareThread = threadPool.pollFirst();
	    if (spareThread == null) {
		try {
		    if (debug)
			System.out.println("No threads ready, waiting for one to finish.");
		    threadPool.wait();
		    spareThread = threadPool.removeFirst();
		} catch (InterruptedException ie) {
		    System.out.println(ie.getMessage());
		}
	    }
	}
	return spareThread;
    }

    public void returnToPool(WorkerThread workerThread) {
	// allows a worker thread to return itself to the pool after it has finished its task
	synchronized(threadPool) {
	    threadPool.add(workerThread);
	    threadPool.notify();
	}
	
    }
    
}
