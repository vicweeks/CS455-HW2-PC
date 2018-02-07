package cs455.overlay.wireformats;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;

public class NodeReportsOverlaySetupStatus implements Event {

    private int type = 7;
    private int status;
    private int infoLength;
    private byte[] statusMessageBytes;
    private String statusMessage;
    
    public NodeReportsOverlaySetupStatus(DataInputStream din) throws IOException {
	// for receiving
	status = din.readInt();
	infoLength = din.readInt();
	statusMessageBytes = new byte[infoLength];
	din.readFully(statusMessageBytes);
	statusMessage = new String(statusMessageBytes);
    }

    public NodeReportsOverlaySetupStatus(int status, String statusMessage) throws IOException {
	// for sending
	this.status = status;
	this.statusMessage = statusMessage;
	statusMessageBytes = statusMessage.getBytes();
	infoLength = statusMessageBytes.length;
    }
    
    public int getType() {
	return type;
    }

    public int getStatus() {
	return status;
    }

    public String getInfo() {
	return statusMessage;
    }

    public byte[] getBytes() throws IOException {
        byte[] marshalledBytes = null;
	ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
	DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

	dout.writeInt(type);
	dout.writeInt(status);
	dout.writeInt(infoLength);
	dout.write(statusMessageBytes);

	dout.flush();
	marshalledBytes = baOutputStream.toByteArray();

	baOutputStream.close();
	dout.close();
	return marshalledBytes;
    }
    
}
