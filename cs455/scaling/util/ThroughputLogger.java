package cs455.scaling.util;

public class ThroughputLogger {

    private int messagesProcessed;
    
    public ThroughputLogger() {
	messagesProcessed = 0;
    }

    public synchronized void processMessage() {
	messagesProcessed += 1;
    }
    
    public synchronized double getThroughput() {
	// get throughput for last 20 seconds and reset counter
	double throughput =  messagesProcessed/20;
	resetLogger();
	return throughput;
    }

    private void resetLogger() {
	messagesProcessed = 0;
    }
    
}
