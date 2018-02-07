package cs455.scaling.threadpool;

public class FixedThreadPool {

    private int threadPoolSize;
    
    public FixedThreadPool(int threadPoolSize) {
	this.threadPoolSize = threadPoolSize;
    }

    public Thread retrieveSpareWorker() {
	// allows a spare worker thread to be retrieved
	return null;
    }

    public void returnToPool() {
	// allows a worker thread to return itself to the pool after it has finished its task
    }
    
}
