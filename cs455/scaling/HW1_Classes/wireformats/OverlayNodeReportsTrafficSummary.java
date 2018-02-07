package cs455.overlay.wireformats;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;

public class OverlayNodeReportsTrafficSummary implements Event {

    private int type = 12;
    private int nodeID;
    private int totalPacketsSent;
    private int totalPacketsRelayed;
    private long sumOfPacketDataSent;
    private int totalPacketsReceived;
    private long sumOfPacketDataReceived;

    public OverlayNodeReportsTrafficSummary(DataInputStream din) throws IOException {
	nodeID = din.readInt();
	totalPacketsSent = din.readInt();
	totalPacketsRelayed = din.readInt();
	sumOfPacketDataSent = din.readLong();
	totalPacketsReceived = din.readInt();
	sumOfPacketDataReceived = din.readLong();
    }

    public OverlayNodeReportsTrafficSummary(int nodeID, int totalSent, int totalRelayed,
					    long sumOfSent, int totalReceived, long sumOfReceived) {
	this.nodeID = nodeID;
	totalPacketsSent = totalSent;
	totalPacketsRelayed = totalRelayed;
	sumOfPacketDataSent = sumOfSent;
	totalPacketsReceived = totalReceived;
	sumOfPacketDataReceived = sumOfReceived;
    }
    
    public int getType() {
	return type;
    }

    public int getNodeID() {
	return nodeID;
    }

    public int getTotalSent() {
	return totalPacketsSent;
    }

    public int getTotalRelayed() {
	return totalPacketsRelayed;
    }

    public long getSumSent() {
	return sumOfPacketDataSent;
    }

    public int getTotalReceived() {
	return totalPacketsReceived;
    }

    public long getSumReceived() {
	return sumOfPacketDataReceived;
    }
    
    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
	ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
	DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

	dout.writeInt(type);
	dout.writeInt(nodeID);
	dout.writeInt(totalPacketsSent);
	dout.writeInt(totalPacketsRelayed);
	dout.writeLong(sumOfPacketDataSent);
	dout.writeInt(totalPacketsReceived);
	dout.writeLong(sumOfPacketDataReceived);

	dout.flush();
	marshalledBytes = baOutputStream.toByteArray();

	baOutputStream.close();
	dout.close();
	return marshalledBytes;
    }
    
}
