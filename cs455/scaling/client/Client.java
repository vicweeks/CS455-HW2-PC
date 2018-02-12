package cs455.scaling.client;

public class Client {

    public static void main(String[] args) {

	Client s = new Client();
	
	if (args.length < 3) {
            System.err.println(
                "Usage: java cs455.scaling.client.Client <server-host> <server-port> <message-rate>");
            System.exit(1);
        }

	String serverHost = args[0];
	int serverPort = Integer.parseInt(args[1]);
	int messageRate = Integer.parseInt(args[2]);
	boolean debug = false;
	
	if (args.length == 4)
	    debug = true;
	
    }
    
}
