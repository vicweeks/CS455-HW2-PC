package cs455.overlay.util;

import cs455.overlay.wireformats.RegistryProtocol;
import cs455.overlay.wireformats.MessagingProtocol;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class InteractiveCommandParser implements Runnable {

    private boolean isRegistry;
    private RegistryProtocol rProtocol;
    private MessagingProtocol mProtocol;
    
    public InteractiveCommandParser(boolean isRegistry, RegistryProtocol protocol) {
	this.isRegistry = isRegistry;
	this.rProtocol = protocol;
    }

    public InteractiveCommandParser(boolean isRegistry, MessagingProtocol protocol) {
	this.isRegistry = isRegistry;
	this.mProtocol = protocol;
    }

    public void run() {
	while(!Thread.currentThread().isInterrupted()) {
	    try {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String input = reader.readLine();
		if (!input.equals("y"))
		    parseCommand(input);
	    } catch (IOException e) {
		System.out.println("Error occured when parsing command");
	    }
	}
	return;
    }

    private void parseCommand(String input) {
	input = input.toLowerCase();
	if (isRegistry) {
	    parseRegistryCommand(input);
	} else if (input.equals("print-counters-and-diagnostics")) {
	    // prints information about the number of messages that have been sent, received, and relayed, along with the sums for sent and received messages
	    mProtocol.printDiagnostics();
	} else if (input.equals("exit-overlay")) {
	    // allows a node to exit the overlay
	    mProtocol.exitOverlay();
	} else {
	    System.out.println("Error: Command \'" + input + "\' is not a messaging node command.");
	}
    }

    private void parseRegistryCommand(String input) {
	String[] command = input.split("\\s+");
	if (command[0].equals("list-messaging-nodes")) {
	    // results in information about the messaging nodes being listed on seperate lines
	    rProtocol.listMessagingNodes();
	} else if (command[0].equals("setup-overlay")) {
	    // results in the registry setting up the overlay
	    if (command.length != 2) {
		System.out.println("Usage: setup-overlay {number-of-routing-table-entries}");
		return;
	    } else {
		int numRoutingTableEntries = Integer.parseInt(command[1]);
		rProtocol.setupOverlay(numRoutingTableEntries);
	    }
	} else if (command[0].equals("list-routing-tables")) {
	    // lists informatin about the computed routing tables for each node in the overlay
	    rProtocol.listRoutingTables();
	} else if (command[0].equals("start")) {
	    // results in the registry requesting task initiate from all nodes in the overlay
	    int numMessages = Integer.parseInt(command[1]);
	    rProtocol.initiateTask(numMessages);
	} else {
	    System.out.println("Error: Command \'" + input + "\' is not a registry command.");
	}
    }
    
}
