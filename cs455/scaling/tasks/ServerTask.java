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
    private HashGenerator hashGen;
    private SocketChannel socketChannel;
    private ByteBuffer buf;
    private ThroughputLogger logger;
    private ServerLogger serverLogger;
    private boolean debug;
    
    public ServerTask(SelectionKey key, ServerLogger serverLogger, HashGenerator hashGen,
		      boolean debug) {
	this.hashGen = hashGen;
	socketChannel = (SocketChannel) key.channel();
	buf = ByteBuffer.allocate(16);
	logger = (ThroughputLogger) key.attachment();
	this.serverLogger = serverLogger;
	this.debug = debug;
	this.key = key;
	key.interestOps(SelectionKey.OP_WRITE);
    }

    private String byteArrayToString(byte[] in) {
	char out[] = new char[in.length * 2];
	for (int i = 0; i < in.length; i++) {
	    out[i * 2] = "0123456789ABCDEF".charAt((in[i] >> 4) & 15);
	    out[i * 2 + 1] = "0123456789ABCDEF".charAt(in[i] & 15);
	}
	return new String(out);
    }
    
    public void run() {
	try {	    
	    int bytesRead = 0;
	    
	    //System.out.println(buf.toString());
	    
	    while(buf.hasRemaining() && bytesRead != -1) {
		bytesRead = socketChannel.read(buf);
	    }

	    //System.out.println(buf.toString());
	    
	    if (bytesRead == -1) {
		System.out.println("Connection terminated by the client.");
		return;
	    }
	    
	    byte[] message = new byte[bytesRead];
	    buf.flip();
	    buf.get(message);
	    
	    String messageHash = getHash(message);

	    //System.out.println("Received Bytes: " + byteArrayToString(message));
	    //System.out.println("Received Message: " + message);
	    //System.out.println("Message Hash: " + messageHash);
	    
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
