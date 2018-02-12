package cs455.scaling.threadpool;

import cs455.scaling.tasks.*;
import java.util.LinkedList;

public class ThreadPoolManager {
    
    private int threadPoolSize;
    private FixedThreadPool threadPool;
    private LinkedList<Runnable> taskQueue;
    private boolean debug;
    
    public ThreadPoolManager(int threadPoolSize, boolean debug) {
	this.threadPoolSize = threadPoolSize;
	this.debug = debug;
	this.taskQueue = new LinkedList<Runnable>();
	this.threadPool = new FixedThreadPool(threadPoolSize, debug);
    }
    
    public static void main(String[] args) {

    //TODO: partial implementation
    // Note: this component can be created and tested in isolation from the rest of the system
    // should allocate a given number of threads;
    // maintain a queue of pending tasks;
    // assign tasks to be handled by the threads;
	
	if (args.length < 1) {
            System.err.println(
                "Usage: java cs455.scaling.threadpool.ThreadPoolManager <thread-pool-size>");
            System.exit(1);
        }

	int threadPoolSize = Integer.parseInt(args[0]);
	boolean debug = false;
	
	if (args.length == 2)
	    debug = true;	

	ThreadPoolManager tpm = new ThreadPoolManager(threadPoolSize, debug);

	for (int i=0; i<10; i++) {
	    tpm.taskQueue.add(new TestTask(tpm.taskQueue, i));
	}

	System.out.println("Tasks have been added.");

	while(tpm.taskQueue.size() != 0) {
	    synchronized(tpm.taskQueue) {
		WorkerThread thread = tpm.threadPool.retrieveSpareWorker();
		Runnable task = tpm.taskQueue.pollFirst();
		if (task == null) {
		    if (debug)
			System.out.println("Task Queue is empty, waiting for tasks.");
		    try {
			tpm.taskQueue.wait();
			task = tpm.taskQueue.removeFirst();
		    } catch (InterruptedException ie) {
			System.out.println(ie.getMessage());
		    }
		}
		thread.assignTask(task);
	    }
	}
	
    }
    
}

