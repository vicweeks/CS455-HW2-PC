package cs455.overlay.transport;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.ArrayList;

public class TCPConnectionsCache {

    private SortedMap<Integer, TCPConnection> connections;
    
    public TCPConnectionsCache() {
	connections = new TreeMap<Integer, TCPConnection>();
    }

    public void addConnection(int nodeID, TCPConnection incomingConnection) {
	connections.put(new Integer(nodeID), incomingConnection);
    }

    public TCPConnection getConnection(int nodeID) {
	return connections.get(nodeID);
    }

    public void removeConnection(int nodeID) {
	connections.remove(nodeID);
    }

    public ArrayList<TCPConnection> getConnectionList() {
	return new ArrayList<TCPConnection>(connections.values());
    }
    
}
