package cs455.scaling.tasks;

public class TestTask implements Runnable {

    private int queuePosition;
    
    public TestTask(int queuePosition) {
	this.queuePosition = queuePosition;
    }

    public void run() {
	String name = Thread.currentThread().getName();
	try {
	    Thread.sleep(1000);
	} catch(InterruptedException ie) {
	    System.out.println(ie.getMessage());
	}
	System.out.println(name + " told this task to execute. Queue Position is: " + queuePosition);
    }
    
}
