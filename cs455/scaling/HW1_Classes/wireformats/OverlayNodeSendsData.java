package cs455.overlay.wireformats;

import java.util.ArrayList;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;

public class OverlayNodeSendsData implements Event {

    private int type = 9;
    private int destID;
    private int srcID;
    private int payload;
    private int disseminationTraceLength;
    private ArrayList<Integer> disseminationTrace;
    
    public OverlayNodeSendsData(DataInputStream din) throws IOException {
	// for receiving
	destID = din.readInt();
	srcID = din.readInt();
	payload = din.readInt();
	disseminationTraceLength = din.readInt();
	disseminationTrace = new ArrayList<Integer>();
	for (int i=0; i<disseminationTraceLength; i++) {
	    disseminationTrace.add(din.readInt());
	}
    }

    public OverlayNodeSendsData(int destID, int srcID, int payload) {
	// for sending
	this.destID = destID;
	this.srcID = srcID;
	this.payload = payload;
	disseminationTraceLength = 0;
	disseminationTrace = new ArrayList<Integer>();
    }

    public byte[] addRelayNode(int nodeID) throws IOException {
	this.disseminationTrace.add(nodeID);
	disseminationTraceLength = disseminationTrace.size();
	return getBytes();
    }
    
    public int getType() {
	return type;
    }

    public int getDestID() {
	return destID;
    }

    public int getSrcID() {
	return srcID;
    }

    public int getPayload() {
	return payload;
    }
    
    public ArrayList<Integer> getDisseminationTrace() {
	return disseminationTrace;
    }

    public byte[] getBytes() throws IOException {
	byte[] marshalledBytes = null;
	ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
	DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

	dout.writeInt(type);
	dout.writeInt(destID);
	dout.writeInt(srcID);
	dout.writeInt(payload);
	dout.writeInt(disseminationTraceLength);

	for (int nodeID : disseminationTrace) {
	    dout.writeInt(nodeID);
	}

	dout.flush();
	marshalledBytes = baOutputStream.toByteArray();

	baOutputStream.close();
	dout.close();
	return marshalledBytes;
    }
    
}
