package cs455.scaling.tasks;

import cs455.scaling.util.HashGenerator;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.io.IOException;

public class ServerTask implements Runnable {

    private final HashGenerator hashGen;
    private SocketChannel socketChannel;
    private ByteBuffer buf;
    
    public ServerTask(SelectionKey key) {
	hashGen = new HashGenerator();
	socketChannel = (SocketChannel) key.channel();
	buf = ByteBuffer.allocate(8000);
    }

    public void run() {
	try {
	    int bytesRead = socketChannel.read(buf);
	    byte[] message = new byte[bytesRead];
	    buf.flip();
	    buf.get(message);
	    String messageHash = getHash(message);
	    System.out.println(messageHash);
	    buf.clear();
	    //replyWithHash(messageHash);
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	}       
    }

    private String getHash(byte[] message) {
	return hashGen.SHA1FromBytes(message);
    }

    private void replyWithHash(String messageHash) throws IOException {
	byte[] replyMessage = messageHash.getBytes();
	buf.put(replyMessage);
	buf.flip();
	
	while(buf.hasRemaining())
	    socketChannel.write(buf);
    }
    
}
