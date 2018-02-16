package cs455.scaling.client;

import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.util.Random;

public class Client {

    private static SocketChannel socketChannel;
    
    public static void main(String[] args) {

	Client c = new Client();
	
	if (args.length < 3) {
            System.err.println(
                "Usage: java cs455.scaling.client.Client <server-host> <server-port> <message-rate>");
            System.exit(1);
        }

	String serverHost = args[0];
	int serverPort = Integer.parseInt(args[1]);
	int messageRate = Integer.parseInt(args[2]);
	boolean debug = false;
	
	if (args.length == 4)
	    debug = true;

	try {
	    c.setUpChannel(serverHost, serverPort);
	    c.sendTestMessage();
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	} catch (InterruptedException ie) {
	    System.out.println(ie.getMessage());
	}
	
    }

    private void setUpChannel(String serverHost, int serverPort)
	throws IOException, InterruptedException{
	System.out.println("Setting up client...");
	socketChannel = SocketChannel.open();
	socketChannel.connect(new InetSocketAddress(serverHost, serverPort));
	while(!socketChannel.finishConnect())
	    Thread.sleep(100);
    }

    private byte[] generateRandomBytes() {
	byte[] rBytes = new byte[8000];
	new Random().nextBytes(rBytes);
	return rBytes;
    }
    
    private void sendTestMessage() throws IOException {
	String testMessage = "This test message was sent at " + System.currentTimeMillis()
	    + " and contains random bytes: " + generateRandomBytes();
	ByteBuffer buf = ByteBuffer.allocate(8000);
	buf.clear();
	buf.put(testMessage.getBytes());

	buf.flip();
	
	while(buf.hasRemaining())
	    socketChannel.write(buf);
    }
    
}
