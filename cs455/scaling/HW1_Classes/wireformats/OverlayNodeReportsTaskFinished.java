package cs455.overlay.wireformats;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class OverlayNodeReportsTaskFinished implements Event {

    private int type = 10;
    private int ipLength;
    private InetAddress ipAddress;
    private byte[] ipAddressRaw;
    private int portNumber;
    private int nodeID;
   
    public OverlayNodeReportsTaskFinished(DataInputStream din)
	throws UnknownHostException, IOException {
	// for receiving
	ipLength = din.readInt();
	ipAddressRaw = new byte[ipLength];
	din.readFully(ipAddressRaw);
	ipAddress = convertFromRaw(ipAddressRaw);
	portNumber = din.readInt();
	nodeID = din.readInt();
    }

    public OverlayNodeReportsTaskFinished(InetAddress ipAddress, int portNumber, int nodeID)
	throws UnknownHostException {
	// for sending
	this.ipAddress = ipAddress;
	this.ipAddressRaw = convertToRaw(ipAddress);
	ipLength = ipAddressRaw.length;
	this.portNumber = portNumber;
	this.nodeID = nodeID;
    }
    
    public int getType() {
	return type;
    }

    public InetAddress getIPAddress() {
	return ipAddress;
    }

    public int getPortNumber() {
	return portNumber;
    }

    public int getNodeID() {
	return nodeID;
    }
    
    private InetAddress convertFromRaw(byte[] ipAddressRaw) throws UnknownHostException {
	return InetAddress.getByAddress(ipAddressRaw);
    }

    private byte[] convertToRaw(InetAddress ipAddress) throws UnknownHostException {
	return ipAddress.getAddress();
    }
    
    public byte[] getBytes() throws IOException {
	byte[] marshalledBytes = null;
	ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
	DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

	dout.writeInt(type);
	dout.writeInt(ipLength);
	dout.write(ipAddressRaw);
	dout.writeInt(portNumber);
	dout.writeInt(nodeID);

	dout.flush();
	marshalledBytes = baOutputStream.toByteArray();

	baOutputStream.close();
	dout.close();
	return marshalledBytes;
    }
    
}
