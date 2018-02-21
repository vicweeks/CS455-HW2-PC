package cs455.scaling.client;

import cs455.scaling.tasks.ClientTask;
import cs455.scaling.util.ClientLogger;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.util.Timer;

public class Client {
    
    private static SocketChannel socketChannel;
    private static ClientLogger clientLogger = new ClientLogger();
    
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

	Timer timer = new Timer();
	timer.schedule(clientLogger, 5000, 5000);
	
	try {
	    c.setUpChannel(serverHost, serverPort);
	    ClientTask clientTask = new ClientTask(socketChannel, messageRate, clientLogger);
	    clientTask.run();
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
    
}
