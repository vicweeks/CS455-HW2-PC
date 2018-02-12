package cs455.scaling.threadpool;

public class WorkerThread extends Thread {

    private FixedThreadPool threadPool;
    private boolean done = true;
    private Runnable task;
    private boolean debug;
    
    public WorkerThread(FixedThreadPool threadPool, boolean debug) {
	this.threadPool = threadPool;
	this.debug = debug;
    }

    public void run() {
	while (!isInterrupted()) {
	    try {
		synchronized(this) {
		    if (done) {
			if (debug)
			    System.out.println("Thread " + Thread.currentThread().getName()
					       + " is waiting to be assigned a task.");
			wait();
		    }
		    else {			
			task.run();
			done = true;
			threadPool.returnToPool(this);
		    }
		}
	    } catch (InterruptedException ie) {
		System.out.println(ie.getMessage());
	    }
	}
    }

    public synchronized void assignTask(Runnable task) {
	this.task = task;
	done = false;
	notify();
    }
    
}
