package cs455.scaling.util;

import java.nio.ByteBuffer;


public class ClientAttachment {

    public ThroughputLogger logger;
    public ByteBuffer rBuf = ByteBuffer.allocate(8*1024);
    public ByteBuffer sBuf = ByteBuffer.allocate(20);
    
    public ClientAttachment(ServerLogger serverLogger) {
	logger = serverLogger.addClient();
    }
    
}
