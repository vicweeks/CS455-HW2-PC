package cs455.scaling.util;

import java.util.TimerTask;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClientLogger extends TimerTask {

    private int totalSent;
    private int totalReceived;
    
    public ClientLogger() {
	totalSent = 0;
	totalReceived = 0;
    }

    public void addSent() {
	totalSent += 1;
    }

    public void addReceived() {
	totalReceived += 1;
    }

    private void reset() {
	totalSent = 0;
	totalReceived = 0;
    }
    
    public void run() {
	LocalTime time = LocalTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:m:s");
	String timestamp = "[" + time.format(formatter) + "]";
	
	System.out.printf(timestamp + "Total Sent Count: %d, \n", totalSent);
	System.out.printf("Total Received Count: %d \n\n", totalReceived);

	reset();
    }
}
