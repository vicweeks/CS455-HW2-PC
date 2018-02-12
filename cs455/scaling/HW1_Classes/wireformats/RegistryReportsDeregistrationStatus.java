package cs455.overlay.wireformats;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;

public class RegistryReportsDeregistrationStatus implements Event {

    private int type = 5;
    private int status;
    private int infoLength;
    private byte[] infoStringBytes;
    private String infoString;

    public RegistryReportsDeregistrationStatus(DataInputStream din) throws IOException {
	// for receiving
	status = din.readInt();
	infoLength = din.readInt();
	infoStringBytes = new byte[infoLength];
	din.readFully(infoStringBytes);
	infoString = new String(infoStringBytes);
    }

    public RegistryReportsDeregistrationStatus(int status, String infoString) {
	// for sending
	this.status = status;
	this.infoString = infoString;
	infoStringBytes = infoString.getBytes();
	infoLength = infoStringBytes.length;
    }
    
    public int getType() {
	return type;
    }

    public int getStatus() {
	return status;
    }

    public String getInfo() {
	return infoString;
    }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
	ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
	DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

	dout.writeInt(type);
	dout.writeInt(status);
	dout.writeInt(infoLength);
	dout.write(infoStringBytes);

	dout.flush();
	marshalledBytes = baOutputStream.toByteArray();

	baOutputStream.close();
	dout.close();
	return marshalledBytes;
    }
    
}
