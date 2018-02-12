package cs455.overlay.wireformats;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;

public class RegistryRequestsTaskInitiate implements Event {

    private int type = 8;
    private int numPacketsToSend;

    public RegistryRequestsTaskInitiate(DataInputStream din) throws IOException {
	// for receiving
	numPacketsToSend = din.readInt();
    }

    public RegistryRequestsTaskInitiate(int numPacketsToSend) {
	this.numPacketsToSend = numPacketsToSend;
    }
    
    public int getType() {
	return type;
    }

    public int getNumPacketsToSend() {
	return numPacketsToSend;
    }
    
    public byte[] getBytes() throws IOException {
	byte[] marshalledBytes = null;
	ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
	DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

	dout.writeInt(type);
	dout.writeInt(numPacketsToSend);

	dout.flush();
	marshalledBytes = baOutputStream.toByteArray();

	baOutputStream.close();
	dout.close();
	return marshalledBytes;
    }
    
}
