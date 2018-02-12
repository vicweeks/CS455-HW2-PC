package cs455.overlay.node;

import cs455.overlay.wireformats.RegistryProtocol;
import cs455.overlay.wireformats.Event;
import cs455.overlay.transport.TCPServerThread;
import cs455.overlay.transport.TCPConnection;
import cs455.overlay.util.InteractiveCommandParser;
import java.io.IOException;
import java.util.ArrayList;

public class Registry implements Node {

    private RegistryProtocol protocol;
    
    public void onEvent(TCPConnection connection, Event event) {
	protocol.onEvent(connection, event);
    }
    
    public static void main(String[] args) throws IOException {

	Registry r = new Registry();
	
        if (args.length != 1) {
            System.err.println("Usage: java cs455.overlay.node.Registry <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

	r.protocol = new RegistryProtocol();
	
	r.setUpServerThread(r, portNumber);
		
	// listen for commands
	r.runtimeCommands(r.protocol);
	     
    }

    public void setUpServerThread(Node registry, int portNumber) {
	TCPServerThread server = new TCPServerThread(registry, portNumber);
	Thread serverThread = new Thread(server);
	serverThread.start();
	System.out.println("Registry has been started.");
    }

    public void runtimeCommands(RegistryProtocol protocol) {
	InteractiveCommandParser icp = new InteractiveCommandParser(true, protocol);
	Thread icpThread = new Thread(icp);
	icpThread.start();
    }
        
}
