package cs455.scaling.tasks;

import cs455.scaling.util.HashCache;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.Random;
import java.util.LinkedList;

public class ClientTask extends Thread {

    private SocketChannel socketChannel;
    private ByteBuffer buf;
    private HashCache hashCache;
    private boolean debug;
    
    public ClientTask(SocketChannel socketChannel, HashCache hashCache, boolean debug) {
	this.socketChannel = socketChannel;
	buf = ByteBuffer.allocate(40);
        this.hashCache = hashCache;
	this.debug = debug;
    }

    public void run() {
	try {
	    Thread.sleep(1000);
	    while(!isInterrupted()) {
		receiveMessage();		
	    }
	} catch (InterruptedException ie) {
	    System.out.println(ie.getMessage());
	}
    }

    // TODO: look for errors here
    private void receiveMessage() {
        try {
	    int bytesRead = socketChannel.read(buf);
	    if (bytesRead <= 0) { // nothing has been read
		if (debug)
		    //System.out.println("Nothing was read. " + bytesRead);
		return;
	    } else {
		byte[] message = new byte[bytesRead];
		buf.flip();
		buf.get(message);
		checkHash(message);
		buf.clear();
	    }
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	    System.exit(0);
	}
    }

    private void checkHash(byte[] message) {
	String messageHash = new String(message);
	//System.out.println("Received Hash: " + messageHash);
        hashCache.checkHash(messageHash);
    }
}
