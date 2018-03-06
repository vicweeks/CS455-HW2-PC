package cs455.scaling.server;

import cs455.scaling.threadpool.ThreadPoolManager;
import cs455.scaling.tasks.ServerTask;
import cs455.scaling.util.HashGenerator;
import cs455.scaling.util.ServerLogger;
import cs455.scaling.util.ThroughputLogger;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.util.Set;
import java.util.Timer;
import java.util.Iterator;

public class Server {

    private static boolean debug;
    private static ThreadPoolManager tpm;
    private static ServerSocketChannel ssChannel;
    private static Selector serverSelector;
    private static final ServerLogger serverLogger = new ServerLogger();
    private final HashGenerator hashGen = new HashGenerator();
       
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

	s.printStatus();
	
	tpm = new ThreadPoolManager(threadPoolSize, debug);
	
	try {
	    s.setupServerSocket(portNumber);
	    serverSelector = Selector.open();
	    s.executeServerLoop();	    
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	}
			
    }

    private void printStatus() {
	// log stats to the console every interval
	Timer timer = new Timer();
	int interval = 20000;
	timer.schedule(serverLogger, interval, interval);
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
		Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
		
	        while(keyIterator.hasNext()) {	    
		    SelectionKey key = keyIterator.next();
		    keyIterator.remove();    
		    		    
		    if (key.interestOps() == SelectionKey.OP_READ) {	  
			if (key.isReadable()) {
			    // create and enqueue a task to read message from client
			    key.interestOps(SelectionKey.OP_WRITE);
			    createTask(key);			
			    tpm.assignTask();
			}
		    }       	    
		}		
	    }
	}
    }
    
    private void createTask(SelectionKey key) throws IOException {
	ServerTask task = new ServerTask(key, serverLogger, hashGen, debug);
	tpm.addTaskToQueue(task);
    }
    
    private void registerIncomingKey(SocketChannel socketChannel) throws IOException {
	if (debug)
	    System.out.println("Registering new client...");
	try {
	    socketChannel.configureBlocking(false);
	    SelectionKey key = socketChannel.register(serverSelector, SelectionKey.OP_READ);
	    ThroughputLogger logger = serverLogger.addClient();	    
	    key.attach(logger);
	} catch (ClosedChannelException cce) {
	    System.out.println(cce.getMessage());
	}
    }
    
}
