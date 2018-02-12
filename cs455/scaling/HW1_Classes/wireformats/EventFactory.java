package cs455.overlay.wireformats;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

public class EventFactory {

    private static EventFactory instance = null;

    protected EventFactory() {
	//defeat instantiation
    }

    public static EventFactory getInstance() {
	if (instance == null)
	    instance = new EventFactory();
	return instance;
    }

    public Event constructEvent(byte[] marshalledBytes) throws IOException {
	int type;
	Event event = null;
	
	ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
	DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

	type = din.readInt();

	switch (type) {
	    case 2: event = new OverlayNodeSendsRegistration(din);
		break;
	    case 3: event = new RegistryReportsRegistrationStatus(din);
		break;
            case 4: event = new OverlayNodeSendsDeregistration(din);
		break;
	    case 5: event = new RegistryReportsDeregistrationStatus(din);
		break;
	    case 6: event = new RegistrySendsNodeManifest(din);
		break;
	    case 7: event = new NodeReportsOverlaySetupStatus(din);
		break;
   	    case 8: event = new RegistryRequestsTaskInitiate(din);
		break;
	    case 9: event = new OverlayNodeSendsData(din);
		break;
	    case 10: event = new OverlayNodeReportsTaskFinished(din);
		break;
	    case 11: event = new RegistryRequestsTrafficSummary(din);
		break;
	    case 12: event = new OverlayNodeReportsTrafficSummary(din);
		break;
	    default: System.out.println("Error in EventFactory: message type " + type + " is invalid.");
		System.exit(1);
	}

	return event;
    } 
    
}
