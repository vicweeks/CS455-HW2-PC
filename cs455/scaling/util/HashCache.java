package cs455.scaling.util;

import java.util.LinkedList;

public class HashCache {

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
        
    private ImprovedList<String> packetHashCodes;
    private ClientLogger logger;
    
    public HashCache(ClientLogger logger) {
	packetHashCodes = new ImprovedList<String>();
	this.logger = logger;
    }

    public void addHash(String messageHash) {
	packetHashCodes.addHash(messageHash);
	synchronized(packetHashCodes) {
	    packetHashCodes.notify();
	}
	/*
	synchronized(packetHashCodes) {
	    packetHashCodes.add(messageHash);
	    packetHashCodes.notify();
	}
	*/
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
	    System.out.println("Message Hash Not Found: " + messageHash);
	}
	
	/*
	synchronized(packetHashCodes) {
	    if ()
	    
	    System.out.println(packetHashCodes.size());
	    int hashIndex = packetHashCodes.indexOf(messageHash);
	    //System.out.println("hashIndex is: " + hashIndex);
	    for(int i=0; i<2; i++) {
		if (hashIndex == -1) {
		    try {
			packetHashCodes.wait();
		    } catch (InterruptedException ie) {
			System.out.println(ie.getMessage());
		    }		    
		}
	    }
	    if (hashIndex != -1) { //found hash
		packetHashCodes.remove(hashIndex);
		logger.addReceived();
	    }
	    else {
		System.out.println("Could not find hash " + messageHash);
	    }
	    
	}
	*/
    }
    
}
