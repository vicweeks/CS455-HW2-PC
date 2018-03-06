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

    private synchronized void reset() {
	serverThroughput = 0;
    }

    private double sum (ArrayList<Double> clients) {
	if (clients.size() > 0) {
	    Double sum = 0.0;
	    for (Double i : clients) {
		sum += i;
	    }
	    return sum;
	}
	return 0;
    }
    
    private double mean(ArrayList<Double> clients) {
	Double sum = sum(clients);
	double mean = 0.0;
	mean = sum / (clients.size());
	return mean; 
    }
    
    private double standardDev(ArrayList<Double> clients) {
	double sum = 0.0;
	double mean = mean(clients);
	for (Double i : clients)
	    sum += Math.pow((i - mean), 2);
	if (activeClients > 1)
	    return Math.sqrt(sum / (clients.size() - 1));
	return 0.0;
    }
    
    public void run() {
	if (activeClients != 0) {
	    ArrayList<Double> meanPerClient = new ArrayList<Double>();
	    for (ThroughputLogger logger : clientLoggers) {
		meanPerClient.add(logger.getThroughput());
	    }
	    Double localServerThroughput = serverThroughput/20.0;
	    reset();
	    double mean = mean(meanPerClient);
	    double std = standardDev(meanPerClient);
	    LocalTime time = LocalTime.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:m:s");
	    String timestamp = "[" + time.format(formatter) + "]";
	
	    System.out.printf(timestamp + " Server Throughput: %f messages/s, \n", localServerThroughput);
	    System.out.printf("Active Client Connections: %d, \n", activeClients);
	    System.out.printf("Mean Per-client Throughput: %f messages/s, \n", mean);
	    System.out.printf("Std. Dev. of Per-client Throughput: %f messages/s \n\n", std);
	}
    }
}
