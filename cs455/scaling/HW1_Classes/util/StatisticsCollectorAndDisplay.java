package cs455.overlay.util;

import java.util.ArrayList;

public class StatisticsCollectorAndDisplay {

    public class TrafficInstance {

	private int nodeID;
	private int packetsSent;
	private int packetsReceived;
	private int packetsRelayed;
	private long sumSent;
	private long sumReceived;
	
	public TrafficInstance(int nodeID, int packetsSent, int packetsReceived, int packetsRelayed,
			  long sumSent, long sumReceived) {
	    this.nodeID = nodeID;
	    this.packetsSent = packetsSent;
	    this.packetsReceived = packetsReceived;
	    this.packetsRelayed = packetsRelayed;
	    this.sumSent = sumSent;
	    this.sumReceived = sumReceived;
	}

	public int getNodeID() {
	    return nodeID;
	}

	public int getSent() {
	    return packetsSent;
	}

	public int getReceived() {
	    return packetsReceived;
	}

	public int getRelayed() {
	    return packetsRelayed;
	}

	public long getSumSent() {
	    return sumSent;
	}

	public long getSumReceived() {
	    return sumReceived;
	}
    }
    
    private ArrayList<TrafficInstance> allTrafficInstances;
    private int totalSent;
    private int totalReceived;
    private int totalRelayed;
    private long totalSumSent;
    private long totalSumReceived;

    public StatisticsCollectorAndDisplay() {
	this.allTrafficInstances = new ArrayList<TrafficInstance>();
	this.totalSent = 0;
	this.totalReceived = 0;
	this.totalRelayed = 0;
	this.totalSumSent = 0L;
	this.totalSumReceived = 0L;
    }

    public void addInstance(int nodeID, int packetsSent, int packetsReceived, int packetsRelayed,
			  long sumSent, long sumReceived) {
	TrafficInstance instance = new TrafficInstance(nodeID, packetsSent, packetsReceived,
						       packetsRelayed, sumSent, sumReceived);
	allTrafficInstances.add(instance);
	this.totalSent += packetsSent;
	this.totalReceived += packetsReceived;
	this.totalRelayed += packetsRelayed;
	this.totalSumSent += sumSent;
	this.totalSumReceived += sumReceived;
    }

    public int getNumInstances() {
	return allTrafficInstances.size();
    }
    
    public void printTrafficSummary() {
	System.out.println("|----------|--------------|------------------|-----------------|"+
			   "---------------------|---------------------|");
	System.out.printf("|          | %12s | %16s | %15s | %19s | %19s |\n",
			  "Packets Sent", "Packets Received", "Packets Relayed",
			  "Sum Values Sent", "Sum Values Received");
	System.out.println("|----------|--------------|------------------|-----------------|"+
			   "---------------------|---------------------|");
	for (TrafficInstance instance : allTrafficInstances) {
	    System.out.printf("| Node %3d | %,12d | %,16d | %,15d | %,19d | %,19d |\n",
			      instance.getNodeID(), instance.getSent(), instance.getReceived(),
			      instance.getRelayed(), instance.getSumSent(), instance.getSumReceived());
	System.out.println("|----------|--------------|------------------|-----------------|"+
			   "---------------------|---------------------|");
	}
	System.out.printf("|    Sum   | %,12d | %,16d | %,15d | %+,19d | %+,19d |\n",
			  totalSent, totalReceived, totalRelayed, totalSumSent, totalSumReceived);
	System.out.println("|----------|--------------|------------------|-----------------|"+
			   "---------------------|---------------------|");
    }
    
}
