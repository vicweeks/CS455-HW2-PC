package cs455.overlay.transport;

import cs455.overlay.node.Node;
import java.net.Socket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;

public class TCPConnection {

    //Identifier info for cache
    private InetAddress remoteIP;
    private InetAddress localIP;
    private int remotePort;
    private int localPort;
    private TCPConnection self;
    private Node node;
    private Socket socket;
    private TCPReceiver receiver;
    private TCPSender sender;
    private Thread receiverThread;
    // private Thread senderThread;
    
    public TCPConnection(Node node, Socket socket) {
	this.node = node;
	this.socket = socket;
	remoteIP = socket.getInetAddress();
	localIP = socket.getLocalAddress();
	remotePort = socket.getPort();
	localPort = socket.getLocalPort();
    }

    public void close() throws IOException {
	receiverThread.interrupt();
    }
    
    public void setUpConnection(TCPConnection self) {
	this.self = self;
	try {
	    this.receiver = new TCPReceiver(node, self, socket);
	    this.sender = new TCPSender(socket);
	    receiverThread = new Thread(receiver);
	    //senderThread = new Thread(sender);
	    receiverThread.start();
	    //senderThread.start();
        } catch (SocketException se) {
	    System.out.println(se.getMessage());
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	}
    }
    
    public void sendMessage(byte[] dataToSend) throws IOException {
	sender.sendData(dataToSend);
    }

    public InetAddress getRemoteIP() {
	return remoteIP;
    }

    public InetAddress getLocalIP() {
	return localIP;
    }
    
    public int getRemotePort() {
	return remotePort;
    }

    public int getLocalPort() {
	return localPort;
    }
    
}
