package cs455.scaling.util;

import java.util.LinkedList;

public class HashCache {

    // thread safe list implementation
    public class ImprovedList<T> extends LinkedList<T> {

	private final LinkedList<T> list = new LinkedList<T>();

	public synchronized void addHash(T messageHash) {
	    list.add(messageHash);
	}

	public synchronized boolean checkAndRemoveHash(T messageHash) {
	    int hashIndex = list.indexOf(messageHash);
	    if (hashIndex != -1) {
		list.remove(hashIndex);
		return true;
	    }
	    return false;
	}
    }
        
    private final ImprovedList<String> packetHashCodes;
    private final ClientLogger logger;
    private final Boolean debug;
    
    public HashCache(ClientLogger logger, Boolean debug) {
	packetHashCodes = new ImprovedList<String>();
	this.logger = logger;
	this.debug = debug;
    }

    public void addHash(String messageHash) {
	packetHashCodes.addHash(messageHash);
	synchronized(packetHashCodes) {
	    packetHashCodes.notify();
	}
    }

    public void checkHash(String messageHash) {
	synchronized(packetHashCodes) {
	    if (packetHashCodes.size() == 0) {
		try {		    
		    packetHashCodes.wait();
		} catch (InterruptedException ie) {
		    System.out.println(ie.getMessage());
		}
	    }
	}
	if (packetHashCodes.checkAndRemoveHash(messageHash)) {
	    logger.addReceived();
	    return;
	} else {
	    if (debug) {
		System.out.println("Message Hash Not Found: " + messageHash);
	    }
	}
        
    }
    
}
