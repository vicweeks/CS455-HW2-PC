package cs455.scaling.server;

public class Server {
    
    public static void main(String[] args) {

	Server s = new Server();
	
	if (args.length < 2) {
            System.err.println(
                "Usage: java cs455.scaling.server.Server <port-number> <thread-pool-size>");
            System.exit(1);
        }

	int portNumber = Integer.parseInt(args[0]);
	int threadPoolSize = Integer.parseInt(args[1]);
	boolean debug = false;
	
	if (args.length == 3)
	    debug = true;	
	
    }
    
}
