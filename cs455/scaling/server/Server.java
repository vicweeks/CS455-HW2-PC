package cs455.scaling.server;

import cs455.scaling.threadpool.ThreadPoolManager;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.util.Set;

public class Server {

    private static boolean debug;
    private static ThreadPoolManager tpm;
    private static ServerSocketChannel ssChannel;
    private static Selector serverSelector;
    //private final int clientInterestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
    
    public static void main(String[] args) {

	Server s = new Server();
	
	if (args.length < 2) {
            System.err.println(
                "Usage: java cs455.scaling.server.Server <port-number> <thread-pool-size>");
            System.exit(1);
        }

	int portNumber = Integer.parseInt(args[0]);
	int threadPoolSize = Integer.parseInt(args[1]);
	debug = false;
	
	if (args.length == 3)
	    debug = true;	

	tpm = new ThreadPoolManager(threadPoolSize, debug);
	
	try {
	    s.setupServerSocket(portNumber);
	    serverSelector = Selector.open();
	    s.executeServerLoop();	    
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	}
			
    }

    private void setupServerSocket(int portNumber) throws IOException {
	System.out.println("Setting up server...");
	ssChannel = ServerSocketChannel.open();
	ssChannel.socket().bind(new InetSocketAddress(portNumber));
	ssChannel.configureBlocking(false);
    }
    
    private void executeServerLoop() throws IOException {
	while (true) {
	    // Get new sockets
	    SocketChannel socketChannel = ssChannel.accept();
	    // Register new sockets
	    if (socketChannel != null)
		registerIncomingKey(socketChannel);
	    // Find read ready channels
	    if (serverSelector.selectNow() > 0) {
		Set<SelectionKey> selectedKeys = serverSelector.selectedKeys();

		for (SelectionKey key : selectedKeys) {		    
		    if (key.isReadable()) {
			//TODO
			readTestMessage(key);
			// Enqueue a task to read message from client
		    }
		    selectedKeys.remove(key);
		}
		
	    }
	}
    }

    private void readTestMessage(SelectionKey key) throws IOException {
	SocketChannel socketChannel = (SocketChannel) key.channel();
	ByteBuffer buf = ByteBuffer.allocate(8000);
	int bytesRead = socketChannel.read(buf);
	String testMessage = "";
	if (bytesRead != -1) {
	    buf.flip();
	    while(buf.hasRemaining()) {
		testMessage += (char) buf.get();
	    }
	    System.out.println(testMessage);
	    buf.clear();
	}
    }
    
    private void registerIncomingKey(SocketChannel socketChannel) throws IOException {
	if (debug)
	    System.out.println("Registering new client...");
	try {
	    socketChannel.configureBlocking(false);
	    SelectionKey key = socketChannel.register(serverSelector, SelectionKey.OP_READ);
	} catch (ClosedChannelException cce) {
	    System.out.println(cce.getMessage());
	}
    }
    
}
