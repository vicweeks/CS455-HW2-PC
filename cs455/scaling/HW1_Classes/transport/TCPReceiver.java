package cs455.overlay.transport;

import cs455.overlay.wireformats.EventFactory;
import cs455.overlay.wireformats.Event;
import cs455.overlay.node.Node;
import java.net.Socket;
import java.net.SocketException;
import java.io.DataInputStream;
import java.io.IOException;

public class TCPReceiver implements Runnable {

    private Node node;
    private TCPConnection self;
    private Socket socket;
    private DataInputStream din;
    private static EventFactory eventFactory;

    public TCPReceiver(Node node, TCPConnection self, Socket socket) throws IOException {
	this.node = node;
	this.self = self;
	this.socket = socket;
	din = new DataInputStream(socket.getInputStream());
	eventFactory = EventFactory.getInstance();
    }
    
    public void run() {
	int dataLength;
	while (!Thread.currentThread().isInterrupted()) {
	    try {
		dataLength = din.readInt();
		byte[] data = new byte[dataLength];
		din.readFully(data, 0, dataLength);
		Event receivedEvent = eventFactory.constructEvent(data);
      
		node.onEvent(self, receivedEvent);
	     		    
	    } catch (SocketException se) {
		System.out.println(se.getMessage());
		break;
	    } catch (IOException ioe) {
		System.out.println(ioe.getMessage());
		break;
	    }
	}
	return;
    }
    
}
