package cs455.scaling.util;

import java.util.LinkedList;

public class HashCache {

    private LinkedList<String> packetHashCodes;
    private ClientLogger logger;
    
    public HashCache(ClientLogger logger) {
	packetHashCodes = new LinkedList<String>();
	this.logger = logger;
    }

    public void addHash(String messageHash) {
	synchronized(packetHashCodes) {
	    packetHashCodes.add(messageHash);
	}
    }

    public void checkHash(String messageHash) {
	synchronized(packetHashCodes) {
	    //System.out.println(packetHashCodes);
	    int hashIndex = packetHashCodes.indexOf(messageHash);
	    //System.out.println("hashIndex is: " + hashIndex);
	    if (hashIndex != -1) { //found hash
		packetHashCodes.remove(hashIndex);
		logger.addReceived();
	    }
	    else {
		//System.out.println("Could not find hash " + messageHash);
	    }
	}
    }
    
}
