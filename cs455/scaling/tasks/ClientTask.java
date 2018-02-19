package cs455.scaling.tasks;

import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.Random;

public class ClientTask {

    private SocketChannel socketChannel;
    private ByteBuffer buf;
    
    public ClientTask(SocketChannel socketChannel) {
	this.socketChannel = socketChannel;
	buf = ByteBuffer.allocate(8000);
    }

    public void startTask() throws IOException {
	for (int i=0; i<4; i++)	
	    sendMessage();
    }
    
    private byte[] generateRandomBytes() {
	byte[] rBytes = new byte[8000];
	new Random().nextBytes(rBytes);
	return rBytes;
    }

    private void sendMessage() throws IOException {
	String testMessage = "This test message was sent at " + System.currentTimeMillis()
	    + " and contains random bytes: " + generateRandomBytes();
	buf.clear();
	buf.put(testMessage.getBytes());

	buf.flip();
	
	while(buf.hasRemaining())
	    socketChannel.write(buf);
    }
}
