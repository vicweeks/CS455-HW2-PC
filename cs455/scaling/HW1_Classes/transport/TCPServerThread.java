package cs455.overlay.transport;

import cs455.overlay.node.Node;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

public class TCPServerThread implements Runnable {

    private Node node;
    private ServerSocket serverSocket;
    
    public TCPServerThread(Node node, int portNumber) {
	this.node = node;
        try {
	    serverSocket = new ServerSocket(portNumber);
	} catch (IOException e) {
	    System.out.println("Error setting up serverSocket");
	    System.out.println(e.getMessage());
	}
    }

    public int getPortNumber() {
	return serverSocket.getLocalPort();
    }

    public void run() {
	while(!Thread.currentThread().isInterrupted()) {
	    try {
		Socket socket = serverSocket.accept();		
		TCPConnection connection = new TCPConnection(node, socket);
		connection.setUpConnection(connection);
	    } catch (IOException ioe) {
		System.out.println(ioe.getMessage());
	    } 
	}
	return;
    }

    public void close() throws IOException {
	serverSocket.close();
    }

}
