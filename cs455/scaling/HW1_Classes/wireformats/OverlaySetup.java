package cs455.overlay.wireformats;

import cs455.overlay.routing.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.io.IOException;
import java.net.UnknownHostException;

public class OverlaySetup {

    private ArrayList<RoutingTable> allRoutingTables;
    private SortedMap<Integer, RoutingEntry> sortedEntries;
    private ArrayList<RoutingEntry> allNodeEntries;
    private ArrayList<Integer> allNodeIDs;
    private int numberOfNodes;
    private int tableSize;
    

    public OverlaySetup(SortedMap<Integer, RoutingEntry> sortedEntries,
			int numRoutingTableEntries) {
	this.sortedEntries = sortedEntries;
	this.tableSize = numRoutingTableEntries;
	numberOfNodes = sortedEntries.size();
	allNodeEntries = new ArrayList<RoutingEntry>(sortedEntries.values());
	allNodeIDs = new ArrayList<Integer>(sortedEntries.keySet());
	constructNodeRoutingTables();
    }

    public ArrayList<RoutingTable> getAllTables() {
	return allRoutingTables;
    }
    
    private void constructNodeRoutingTables() {
	allRoutingTables = new ArrayList<RoutingTable>(numberOfNodes);
	
	for(RoutingEntry entry : allNodeEntries) {
	    int nodeID = entry.getNodeID();
	    ArrayList<RoutingEntry> nodesToConnect = calculateRoutingTable(nodeID);
	    RoutingTable nodeTable =
		new RoutingTable(entry, tableSize, numberOfNodes, allNodeIDs, nodesToConnect);
	    allRoutingTables.add(nodeTable);
	}
    }

    private ArrayList<RoutingEntry> calculateRoutingTable(int nodeID) {
	int idIndex = allNodeIDs.indexOf(nodeID);
	ArrayList<RoutingEntry> nodesToConnect = new ArrayList<RoutingEntry>(tableSize);
	int[] connectedIDs = new int[tableSize];
	for (int i=0; i<tableSize; i++) {
	    int nextIndex = (int) ((idIndex + Math.pow(2, i)) % numberOfNodes);
	    connectedIDs[i] = allNodeIDs.get(nextIndex);
	}
	for (int nextID : connectedIDs) {
	    for (RoutingEntry entry : allNodeEntries) {
		if (entry.getNodeID() == nextID) {
		    nodesToConnect.add(entry);
		}
	    }
	}	
	return nodesToConnect;
    }

    public byte[] constructNodeManifest(RoutingTable nodeTable)
	throws UnknownHostException, IOException {
	int tableSize = nodeTable.getTableSize();
	int numNodeIDs = nodeTable.getNumberOfNodes();
	RoutingEntry nodeEntry = nodeTable.getLocalEntry();
	ArrayList<RoutingEntry> nodesToConnect = nodeTable.getConnectedNodes();
	RegistrySendsNodeManifest nodeManifest =
	    new RegistrySendsNodeManifest(tableSize, nodesToConnect, numNodeIDs, allNodeIDs);
	return nodeManifest.getBytes();	
    }
    
}
