package cs455.scaling.tasks;

import cs455.scaling.util.HashGenerator;
import cs455.scaling.util.ThroughputLogger;
import cs455.scaling.util.ServerLogger;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.io.IOException;

public class ServerTask implements Runnable {

    private SelectionKey key;
    private final HashGenerator hashGen;
    private SocketChannel socketChannel;
    private ByteBuffer buf;
    private ThroughputLogger logger;
    private ServerLogger serverLogger;
    private boolean debug;
    
    public ServerTask(SelectionKey key, ServerLogger serverLogger, boolean debug) {
	hashGen = new HashGenerator();
	socketChannel = (SocketChannel) key.channel();
	buf = ByteBuffer.allocate(8000);
	logger = (ThroughputLogger) key.attachment();
	this.serverLogger = serverLogger;
	this.debug = debug;
	this.key = key;
	key.interestOps(SelectionKey.OP_WRITE);
    }

    public void run() {
	try {	    
	    int bytesRead = socketChannel.read(buf);
	    if (bytesRead == -1) {
		System.out.println("Connection terminated by the client.");
		return;
	    }
	    
	    byte[] message = new byte[bytesRead];
	    buf.flip();
	    buf.get(message);
	    
	    String messageHash = getHash(message);
	    buf.clear();	    
	    replyWithHash(messageHash);
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	}       
    }

    private String getHash(byte[] message) {
	return hashGen.SHA1FromBytes(message);
    }

    private void replyWithHash(String messageHash) throws IOException {
	byte[] replyMessage = messageHash.getBytes();
	
	ByteBuffer buffer = ByteBuffer.wrap(replyMessage);        
	socketChannel.write(buffer);
	
	if (debug)
	    System.out.println("Wrote reply " + messageHash + " to socketChannel");
	
	logger.processMessage();
	serverLogger.processMessage();

	key.interestOps(SelectionKey.OP_READ);
    }
    
}
