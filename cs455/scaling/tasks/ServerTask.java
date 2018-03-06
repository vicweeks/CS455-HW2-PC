package cs455.scaling.tasks;

import cs455.scaling.util.HashGenerator;
import cs455.scaling.util.ThroughputLogger;
import cs455.scaling.util.ServerLogger;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.io.IOException;

public class ServerTask implements Runnable {

    private final SelectionKey key;
    private final ServerLogger serverLogger;
    private final HashGenerator hashGen;
    private final boolean debug;
    private final SocketChannel socketChannel;
    private final ByteBuffer rBuf = ByteBuffer.allocate(8*1024);
    private final ThroughputLogger logger;
    
    
    public ServerTask(SelectionKey key, ServerLogger serverLogger, HashGenerator hashGen,
		      boolean debug) {
	this.key = key;
	this.serverLogger = serverLogger;
	this.hashGen = hashGen;
	this.debug = debug;
	socketChannel = (SocketChannel) key.channel();
	logger = (ThroughputLogger) key.attachment();
    }

    public void run() {
	try {	    
	    int bytesRead = 0;
	    rBuf.clear();
	    
	    while(rBuf.hasRemaining() && bytesRead != -1) {
		bytesRead = socketChannel.read(rBuf);
	    }
	    
	    if (bytesRead == -1) {
		System.out.println("Connection terminated by the client.");
		return;
	    }
	    
	    rBuf.flip();	    
	    byte[] messageHash = getHash(rBuf.array());    
	    
	    rBuf.clear();	    
	    replyWithHash(messageHash);
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	}       
    }
      
    private byte[] getHash(byte[] message) {
	return hashGen.SHA1FromBytes(message);
    }

    private void replyWithHash(byte[] messageHash) throws IOException {
	ByteBuffer sBuf = ByteBuffer.allocate(20);
	sBuf.clear();
	sBuf.put(messageHash);
	sBuf.flip();
	while(sBuf.hasRemaining()) {
	    socketChannel.write(sBuf);
	}
	
	String hashString = hashGen.convertToString(messageHash);
	
	if (debug) {
	    System.out.println("Wrote reply " + hashString + " to socketChannel");
	}
	
	logger.processMessage();
	serverLogger.processMessage();

	key.interestOps(SelectionKey.OP_READ);
    }
    
}
