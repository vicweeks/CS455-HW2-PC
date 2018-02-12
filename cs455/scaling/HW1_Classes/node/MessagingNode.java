package cs455.overlay.node;

import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.MessagingProtocol;
import cs455.overlay.wireformats.OverlayNodeSendsRegistration;
import cs455.overlay.transport.TCPServerThread;
import cs455.overlay.transport.TCPConnection;
import cs455.overlay.util.InteractiveCommandParser;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;

public class MessagingNode implements Node {

    private TCPConnection registryConnection;
    private MessagingProtocol protocol;
    private InteractiveCommandParser icp;
    private Thread icpThread;
    private TCPServerThread server;
    private Thread serverThread;
    
    public void onEvent(TCPConnection connection, Event event) {
	protocol.onEvent(connection, event);
    }
    
    public static void main(String[] args) throws IOException {

	MessagingNode m = new MessagingNode();
	
        if (args.length != 2) {
            System.err.println(
                "Usage: java cs455.overlay.node.MessagingNode <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int registryPortNumber = Integer.parseInt(args[1]);

	int localPortNumber = m.setUpServerThread(m);
	
	
	
	try { // register with Registry
	    byte[] registerMessageBytes = m.createRegistrationMessage(localPortNumber);
	    m.connectToRegistry(m, hostName, registryPortNumber);
	    m.protocol = new MessagingProtocol(m, m.registryConnection, localPortNumber);
	    m.registryConnection.sendMessage(registerMessageBytes);
	} catch(UnknownHostException uhe) {
	    System.out.println(uhe.getMessage());
	} catch(IOException ioe) {
	    System.out.println(ioe.getMessage());
	}

	m.runtimeCommands(m.protocol);
	return;
    }

    public int setUpServerThread(Node m) {
	int portNumber = -1;
        server = new TCPServerThread(m, 0);
	portNumber = server.getPortNumber();
	serverThread = new Thread(server);
	serverThread.start();
	System.out.println("Messaging Node has been started.");
	return portNumber;
    }
    
    public byte[] createRegistrationMessage(int localPortNumber) throws UnknownHostException, IOException {
	InetAddress ipAddress = InetAddress.getLocalHost();
	OverlayNodeSendsRegistration registerMessage = new OverlayNodeSendsRegistration(ipAddress, localPortNumber);
	return registerMessage.getBytes();
    }
    
    public void connectToRegistry(Node messagingNode, String hostName, int portNumber) throws UnknownHostException, IOException {
	Socket registrySocket = new Socket(hostName, portNumber);	
	registryConnection = new TCPConnection(messagingNode, registrySocket);
	registryConnection.setUpConnection(registryConnection);
    }

    public void runtimeCommands(MessagingProtocol protocol) {
	icp = new InteractiveCommandParser(false, protocol);
        icpThread = new Thread(icp);
	icpThread.start();
    }

    public void close(TCPConnection connection) {
	try {
	    connection.close();
	    server.close();
	    serverThread.interrupt();
	    icpThread.interrupt();
	    System.out.println("Would you like to close this node? (y/n)");
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	}
    }
    
}
