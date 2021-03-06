package cs455.scaling.client;

import cs455.scaling.tasks.ClientTask;
import cs455.scaling.tasks.ClientSendTask;
import cs455.scaling.util.ClientLogger;
import cs455.scaling.util.HashCache;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.util.Timer;

public class Client {
    
    private static SocketChannel socketChannel;
    private static final ClientLogger clientLogger = new ClientLogger();
    
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

	c.printStatus();
	
	try {
	    c.setUpChannel(serverHost, serverPort);
	    HashCache hashCache = new HashCache(clientLogger, debug);
	    ClientTask clientTask = new ClientTask(socketChannel, hashCache, debug);
	    clientTask.start();
	    ClientSendTask clientSendTask = new ClientSendTask(socketChannel, messageRate, clientLogger, hashCache, debug);
	    clientSendTask.start();
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	} catch (InterruptedException ie) {
	    System.out.println(ie.getMessage());
	}
	
    }

    private void printStatus() {
	// logs stats to the console every interval
	Timer timer = new Timer();
	int interval = 20000;
	timer.schedule(clientLogger, interval, interval);
    }
    
    private void setUpChannel(String serverHost, int serverPort)
	throws IOException, InterruptedException{
	System.out.println("Setting up client...");
	socketChannel = SocketChannel.open();
	socketChannel.configureBlocking(false);
	socketChannel.connect(new InetSocketAddress(serverHost, serverPort));
	while(!socketChannel.finishConnect())
	    Thread.sleep(100);
    }
    
}
