package cs455.overlay.node;

import cs455.overlay.wireformats.Event;
import cs455.overlay.transport.TCPConnection;

public interface Node {
    
    public void onEvent(TCPConnection connection, Event event);
    
}
