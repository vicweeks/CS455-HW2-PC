package cs455.scaling.threadpool;

import cs455.scaling.tasks.*;
import java.util.LinkedList;

public class ThreadPoolManager {
    
    private int threadPoolSize;
    private FixedThreadPool threadPool;
    private LinkedList<Runnable> taskQueue;
    private boolean debug;
    
    public ThreadPoolManager(int threadPoolSize) {
	this.threadPoolSize = threadPoolSize;
	this.taskQueue = new LinkedList<Runnable>();
	this.threadPool = new FixedThreadPool(taskQueue, threadPoolSize);
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

	ThreadPoolManager tpm = new ThreadPoolManager(threadPoolSize);

	for (int i=0; i<50; i++) {
	    tpm.taskQueue.add(new TestTask(i));
	}

	System.out.println("Tasks have been added.");

	while (tpm.taskQueue.size() != 0)
	    tpm.threadPool.retrieveSpareWorker();
	
    }
    
}

