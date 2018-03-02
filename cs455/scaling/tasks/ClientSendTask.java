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
		//Thread.sleep(2000);
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

    private byte[] genTestBytes() {
	byte[] testBytes = new byte[16];
	new Random().nextBytes(testBytes);
	//System.out.println("Sending Bytes: " + byteArrayToString(testBytes));
	return testBytes;
    }

    private String byteArrayToString(byte[] in) {
	char out[] = new char[in.length * 2];
	for (int i = 0; i < in.length; i++) {
	    out[i * 2] = "0123456789ABCDEF".charAt((in[i] >> 4) & 15);
	    out[i * 2 + 1] = "0123456789ABCDEF".charAt(in[i] & 15);
	}
	return new String(out);
    }
    
    private void sendMessage() throws IOException {
	if(debug)
	    System.out.println("Sending message");
	//byte[] message = generateRandomBytes();

	byte[] message = genTestBytes();
	String messageHash = hashGen.SHA1FromBytes(message);
	
	//System.out.println("Sent Message: " + message);
	//System.out.println("Sent Hash: " + messageHash);

	hashCache.addHash(messageHash);

	ByteBuffer buffer = ByteBuffer.allocate(16);
	buffer.clear();
	buffer.put(message);
	buffer.flip();

	while(buffer.hasRemaining()) {
	    socketChannel.write(buffer);
	}


	//ByteBuffer buffer = ByteBuffer.wrap(message);

	//System.out.println(buffer.toString());
	//socketChannel.write(buffer);

	logger.addSent();
    }
   
}
