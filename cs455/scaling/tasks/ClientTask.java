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
    
    public ClientTask(SocketChannel socketChannel, int messageRate, ClientLogger logger) {
	this.socketChannel = socketChannel;
	buf = ByteBuffer.allocate(8000);
	this.messageRate = messageRate;
	packetHashcodes = new LinkedList<String>();
	hashGen = new HashGenerator();
	this.logger = logger;
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
	byte[] rBytes = new byte[8000];
	new Random().nextBytes(rBytes);
	return rBytes;
    }

    private void sendMessage() throws IOException {
        byte[] message = generateRandomBytes();
	String messageHash = hashGen.SHA1FromBytes(message);
	packetHashcodes.add(messageHash);
	buf.clear();
	buf.put(message);
	buf.flip();
	
	while(buf.hasRemaining())
	    socketChannel.write(buf);

	logger.addSent();
    }
}
