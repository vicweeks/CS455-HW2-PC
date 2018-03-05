package cs455.scaling.tasks;

import cs455.scaling.util.ClientAttachment;
import cs455.scaling.util.HashGenerator;
import cs455.scaling.util.ThroughputLogger;
import cs455.scaling.util.ServerLogger;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.io.IOException;

public class ServerTask implements Runnable {

    private SelectionKey key;
    private HashGenerator hashGen;
    private SocketChannel socketChannel;
    private ByteBuffer rBuf;
    private ThroughputLogger logger;
    private ServerLogger serverLogger;
    private boolean debug;
    //private ByteBuffer buffer = ByteBuffer.allocate(20);

    private ByteBuffer sBuf;
    
    public ServerTask(SelectionKey key, ServerLogger serverLogger, HashGenerator hashGen,
		      boolean debug) {
	this.hashGen = hashGen;
	socketChannel = (SocketChannel) key.channel();
        rBuf = ByteBuffer.allocate(8*1024);
	ClientAttachment attachment = (ClientAttachment) key.attachment();
	logger = attachment.logger;
	//rBuf = attachment.rBuf;
	sBuf = attachment.sBuf;
	this.serverLogger = serverLogger;
	this.debug = debug;
	this.key = key;
	//key.interestOps(SelectionKey.OP_WRITE);
    }

    public void run() {
	try {	    
	    int bytesRead = 0;
	    rBuf.clear();
	    //System.out.println(rBuf.toString());
	    
	    while(rBuf.hasRemaining() && bytesRead != -1) {
		bytesRead = socketChannel.read(rBuf);
	    }

	    // System.out.println(new String(rBuf));
	    
	    if (bytesRead == -1) {
		System.out.println("Connection terminated by the client.");
		return;
	    }
	    
	    //byte[] message = new byte[bytesRead];
	    rBuf.flip();
	    //rBuf.get(message);
	    
	    //System.out.println(new String(message));
	    //byte[] messageHash = getHash(message);
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
	//ByteBuffer buffer = ByteBuffer.allocate(20);
	sBuf.clear();
	sBuf.put(messageHash);
	sBuf.flip();
	while(sBuf.hasRemaining()) {
	    socketChannel.write(sBuf);
	}
	
	String hashString = hashGen.convertToString(messageHash);
	
	if (debug)
	    System.out.println("Wrote reply " + hashString + " to socketChannel");
	
	logger.processMessage();
	serverLogger.processMessage();

	key.interestOps(SelectionKey.OP_READ);
    }
    
}
