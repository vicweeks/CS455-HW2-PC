package cs455.scaling.tasks;

import java.util.LinkedList;

public class TestTask implements Runnable {

    private LinkedList<Runnable> taskQueue;
    private int queuePosition;
    
    public TestTask(LinkedList<Runnable> taskQueue, int queuePosition) {
	this.taskQueue = taskQueue;
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
