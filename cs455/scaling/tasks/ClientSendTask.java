package cs455.scaling.tasks;

import cs455.scaling.util.HashGenerator;
import cs455.scaling.util.ClientLogger;
import cs455.scaling.util.HashCache;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.Random;
import java.util.LinkedList;

public class ClientSendTask extends Thread {

    private SocketChannel socketChannel;
    private int messageRate;
    private HashCache hashCache;
    private ClientLogger logger;
    private final HashGenerator hashGen;
    private boolean debug;
    
    public ClientSendTask(SocketChannel socketChannel, int messageRate,
			  ClientLogger logger, HashCache hashCache,  boolean debug) {
	this.socketChannel = socketChannel;
	this.messageRate = messageRate;
	this.hashCache = hashCache;
	hashGen = new HashGenerator();
	this.logger = logger;
	this.debug = debug;
    }

    public void run() {
	while(!isInterrupted()) {
	    try {
		sendMessage();
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

    private byte[] genTestBytes(int n) {
	byte[] testBytes = new byte[n*1024];
	new Random().nextBytes(testBytes);
	return testBytes;
    }
    
    private void sendMessage() throws IOException {
	byte[] message = generateRandomBytes();
	//String messageString = "hello world";
	//byte[] message = messageString.getBytes();
       
	//byte[] message = genTestBytes(2);
        byte[] hashBytes = hashGen.SHA1FromBytes(message);
	String messageHash = hashGen.convertToString(hashBytes);

	//System.out.println("Sent Message: " + new String(message));
	if(debug) {
	    System.out.println("Sent Hash: " + messageHash);
	}
	
	hashCache.addHash(messageHash);

	ByteBuffer buffer = ByteBuffer.allocate(8*1024);
	buffer.clear();
	buffer.put(message);
	buffer.flip();

	while(buffer.hasRemaining()) {
	    socketChannel.write(buffer);
	}

	logger.addSent();
    }
   
}
