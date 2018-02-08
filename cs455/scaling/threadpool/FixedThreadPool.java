package cs455.scaling.threadpool;

import java.util.LinkedList;
import java.util.ArrayList;

public class FixedThreadPool {

    private ArrayList<WorkerThread> threadPool;
    private LinkedList<Runnable> taskQueue;
    
    public FixedThreadPool(LinkedList<Runnable> taskQueue, int threadPoolSize) {
	this.taskQueue = taskQueue;
	threadPool = new ArrayList<WorkerThread>(threadPoolSize);
	for (int i=0; i<threadPoolSize; i++) {
	    threadPool.add(new WorkerThread(this, taskQueue));
	    threadPool.get(i).start();
	}
    }

    public void retrieveSpareWorker() {
	// allows a spare worker thread to be retrieved
	synchronized(taskQueue) {
	    taskQueue.notify();
	}
    }

    public void returnToPool() {
	// allows a worker thread to return itself to the pool after it has finished its task
	
    }
    
}
