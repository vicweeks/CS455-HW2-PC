package cs455.overlay.routing;

import java.util.ArrayList;

public class RoutingTable {

    private RoutingEntry localEntry;
    private int tableSize;
    private int numberOfNodes;
    private ArrayList<Integer> allNodeIDs;
    private ArrayList<RoutingEntry> connectedNodes;

    public RoutingTable(RoutingEntry localEntry, int tableSize, int numberOfNodes,
			ArrayList<Integer> allNodeIDs, ArrayList<RoutingEntry> nodesToConnect) {
	this.localEntry = localEntry;
	this.tableSize = tableSize;
	this.numberOfNodes = numberOfNodes;
	this.allNodeIDs = allNodeIDs;
	this.connectedNodes = nodesToConnect;
    }

    public RoutingEntry getLocalEntry() {
	return localEntry;
    }

    public int getTableSize() {
	return tableSize;
    }

    public int getNumberOfNodes() {
	return numberOfNodes;
    }

    public ArrayList<Integer> getListIDs() {
	return allNodeIDs;
    }
    
    public ArrayList<RoutingEntry> getConnectedNodes() {
	return connectedNodes;
    }

}
