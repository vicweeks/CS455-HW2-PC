package cs455.scaling.threadpool;

import java.util.LinkedList;
import java.util.ArrayList;

public class WorkerThread extends Thread {

    private FixedThreadPool threadPool;
    private LinkedList<Runnable> taskQueue;
    private Runnable task;
    
    public WorkerThread(FixedThreadPool threadPool, LinkedList<Runnable> taskQueue) {
	this.threadPool = threadPool;
	this.taskQueue = taskQueue;
    }

    public void run() {
	while (!isInterrupted()) {
	    //TODO
	    try {
		// taskQueue to pop the next task
		synchronized(taskQueue) {
		    taskQueue.wait();
		    if (taskQueue.size() != 0) {
			task = taskQueue.remove();
			task.run();
		    } else
			return;
		}
	    } catch (InterruptedException ie) {
		System.out.println(ie.getMessage());
	    }
	    // Execute task
	    //task.run();
	    // Task is completed, restart loop
	}
    }
    
}
