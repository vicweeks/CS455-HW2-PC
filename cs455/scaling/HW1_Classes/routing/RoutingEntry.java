package cs455.overlay.routing;

import java.net.InetAddress;

public class RoutingEntry {

    private int nodeID;
    private InetAddress ipAddress;
    private int portNumber;

    public RoutingEntry(int nodeID, InetAddress ipAddress, int portNumber) {
	this.nodeID = nodeID;
	this.ipAddress = ipAddress;
	this.portNumber = portNumber;
    }

    public int getNodeID() {
	return this.nodeID;
    }

    public InetAddress getIPAddress() {
	return this.ipAddress;
    }

    public int getPortNumber() {
	return this.portNumber;
    }

}
