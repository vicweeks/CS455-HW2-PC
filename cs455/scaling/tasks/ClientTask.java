package cs455.scaling.tasks;

import cs455.scaling.util.HashGenerator;
import cs455.scaling.util.ClientLogger;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.Random;
import java.util.LinkedList;

public class ClientTask extends Thread {

    private SocketChannel socketChannel;
    private ByteBuffer buf;
    private int messageRate;
    private LinkedList<String> packetHashcodes;
    private ClientLogger logger;
    private final HashGenerator hashGen;
    private boolean debug;
    
    public ClientTask(SocketChannel socketChannel, int messageRate,
		      ClientLogger logger, boolean debug) {
	this.socketChannel = socketChannel;
	buf = ByteBuffer.allocate(8*1024);
	this.messageRate = messageRate;
	packetHashcodes = new LinkedList<String>();
	hashGen = new HashGenerator();
	this.logger = logger;
	this.debug = debug;
    }

    public void run() {
	while(!isInterrupted()) {
	    try {
		sendMessage();
		receiveMessage();
		Thread.sleep(1000 / messageRate);		
	    } catch (IOException ioe) {
		System.out.println(ioe.getMessage());
		System.exit(0);
	    } catch (InterruptedException ie) {
		System.out.println(ie.getMessage());	        
	    }
	}
    }
    
    private byte[] generateRandomBytes() {
	byte[] rBytes = new byte[8*1024];
	new Random().nextBytes(rBytes);
	return rBytes;
    }

    private void sendMessage() throws IOException {
        byte[] message = generateRandomBytes();
	String messageHash = hashGen.SHA1FromBytes(message);
	packetHashcodes.add(messageHash);

	ByteBuffer buffer = ByteBuffer.wrap(message);
	socketChannel.write(buffer);

	logger.addSent();
    }

    private void receiveMessage() {
        try {
	    int bytesRead = socketChannel.read(buf);
	    if (bytesRead <= 0) { // nothing has been read
		if (debug)
		    System.out.println("Nothing was read. " + bytesRead);
		return;
	    }
	    byte[] message = new byte[bytesRead];
	    buf.flip();
	    buf.get(message);
	    checkHash(message);
	    buf.clear();
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	}
    }

    private void checkHash(byte[] message) {
	String messageHash = new String(message);
	int hashIndex = packetHashcodes.indexOf(messageHash);
	if (hashIndex != -1) { // found hash
	    packetHashcodes.remove(hashIndex);
	    logger.addReceived();
	    if (debug)
		System.out.println("Found matching hash: " + messageHash);
	}
    }
}
