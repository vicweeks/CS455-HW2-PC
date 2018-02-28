package cs455.scaling.util;

import java.util.LinkedList;

public class HashCache {

    private LinkedList<String> packetHashCodes;
    private ClientLogger logger;
    
    public HashCache(ClientLogger logger) {
	packetHashCodes = new LinkedList<String>();
	this.logger = logger;
    }

    public synchronized void addHash(String messageHash) {
	packetHashCodes.add(messageHash);
    }

    public synchronized void checkHash(String messageHash) {
	System.out.println(messageHash);
	int hashIndex = packetHashCodes.indexOf(messageHash);
	if (hashIndex != -1) { //found hash
	    packetHashCodes.remove(hashIndex);
	    logger.addReceived();
	}
	else {
	    //System.out.println("Could not find hash " + messageHash);
	}
    }
    
}
