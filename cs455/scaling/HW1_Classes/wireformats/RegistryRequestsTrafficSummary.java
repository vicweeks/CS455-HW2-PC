package cs455.overlay.wireformats;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;

public class RegistryRequestsTrafficSummary implements Event {

    private int type = 11;

    public RegistryRequestsTrafficSummary(DataInputStream din) throws IOException {
	// nothing needed
    }

    public RegistryRequestsTrafficSummary() {
	// nothing needed
    }
    
    public int getType() {
	return type;
    }

    public byte[] getBytes() throws IOException {
	byte[] marshalledBytes = null;
	ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
	DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

	dout.writeInt(type);

	dout.flush();
	marshalledBytes = baOutputStream.toByteArray();

	baOutputStream.close();
	dout.close();
	return marshalledBytes;
    }
    
}
