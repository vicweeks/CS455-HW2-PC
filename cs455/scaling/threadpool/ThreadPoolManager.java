package cs455.scaling.threadpool;

import java.util.LinkedList;

public class ThreadPoolManager {
    
    private int threadPoolSize;
    private FixedThreadPool threadPool;
    private LinkedList taskQueue;
    
    public ThreadPoolManager(int threadPoolSize) {
	this.threadPoolSize = threadPoolSize;
	this.threadPool = new FixedThreadPool(threadPoolSize);
	this.taskQueue = new LinkedList();
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
	
    }
    
}

