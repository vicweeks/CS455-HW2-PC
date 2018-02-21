package cs455.scaling.util;

import java.util.ArrayList;
import java.util.TimerTask;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ServerLogger extends TimerTask {

    private static ArrayList<ThroughputLogger> clientLoggers;
    private int activeClients;
    private int serverThroughput;
    
    public ServerLogger() {
	clientLoggers = new ArrayList<ThroughputLogger>();
	activeClients = 0;
	serverThroughput = 0;
    }

    public ThroughputLogger addClient() {
	activeClients += 1;
	ThroughputLogger logger = new ThroughputLogger();
	clientLoggers.add(logger);
	return logger;
    }

    public synchronized void processMessage() {
	serverThroughput += 1;
    }

    private void reset() {
	serverThroughput = 0;
    }
    
    public void run() {
	if (activeClients != 0) {
	    int meanPerClient = 0;
	    int stdDevPerClient = 0;
	    for (ThroughputLogger logger : clientLoggers) {
		meanPerClient += logger.getThroughput();
	    }
	    serverThroughput /= 20;
	    meanPerClient /= activeClients;
	    LocalTime time = LocalTime.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:m:s");
	    String timestamp = "[" + time.format(formatter) + "]";
	
	    System.out.printf(timestamp + " Server Throughput: %d messages/s, \n", serverThroughput);
	    System.out.printf("Active Client Connections: %d, \n", activeClients);
	    System.out.printf("Mean Per-client Throughput: %d messages/s, \n", meanPerClient);
	    System.out.printf("Std. Dev. of Per-client Throughput: %d messages/s \n\n",
			      stdDevPerClient);

	    reset();
	}
    }
}
