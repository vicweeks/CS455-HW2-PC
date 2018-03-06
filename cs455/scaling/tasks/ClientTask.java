package cs455.scaling.tasks;

import cs455.scaling.util.HashGenerator;
import cs455.scaling.util.HashCache;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.Random;
import java.util.LinkedList;

public class ClientTask extends Thread {

    private final SocketChannel socketChannel;
    private final ByteBuffer buf = ByteBuffer.allocate(20);
    private final HashCache hashCache;
    private final HashGenerator hashGen = new HashGenerator();
    private final boolean debug;
    
    public ClientTask(SocketChannel socketChannel, HashCache hashCache, boolean debug) {
	this.socketChannel = socketChannel;
        this.hashCache = hashCache;
	this.debug = debug;
    }

    public void run() {
	try {
	    Thread.sleep(100);
	    while(!isInterrupted()) {
		receiveMessage();		
	    }
	} catch (InterruptedException ie) {
	    System.out.println(ie.getMessage());
	}
    }

    private void receiveMessage() throws InterruptedException {
        try {
	    int bytesRead = 0;

	    while(buf.hasRemaining() && bytesRead != -1)
		bytesRead = socketChannel.read(buf);
	    
	    if (bytesRead <= 0) { // nothing has been read
		if (debug) {
		    System.out.println("Nothing was read. " + bytesRead);
		}
		Thread.sleep(100);
		return;
	    } else {
	        buf.flip();
		checkHash(buf.array());
		buf.clear();
	    }
	    
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	    System.exit(0);
	}
    }

    private void checkHash(byte[] hashBytes) {
	String messageHash = hashGen.convertToString(hashBytes);
	if (debug) {
	    System.out.println("Received Hash: " + messageHash);
	}
        hashCache.checkHash(messageHash);
    }
}
