CS455-HW2-PC
Intro to Distributed Systems: Homework 2
Victor Weeks
Feburary 7, 2018

Usage:
	* Compile the program using ```make```.
	
	* Create one server with the command ```java cs455.scaling.server.Server <portnum> <thread-pool-size>``` \
	where <portnum> is the desired port for the ServerSocketChannel and \
	<thread-pool-size> is the desired size of the thread pool that will be handling tasks.

	* Create multiple instances of clients with the command ```java cs455.scaling.client.Client <server-host> <server-port> <message-rate>``` \
	where <server-host> is the name of the host that the server is running on, \
	<server-port> is the port that the server is running on, and \
	<message-rate> is the number of messages per second that each client should send.

	* Every 20 seconds the server will print its status to the console. This status includes a timestamp, the server throughput in messages/second, \
	the number of active client connections, the mean per-client throughput in messages/second, and the standard deviation of per-client throughput in messages/second.

	* Every 20 seconds each client will print their status to the console. This status includes a timestamp, the number of messages send in the last 20 seconds, and \
	the number of messages received in the last 20 seconds.
	

Class Desriptions:

client
	* Client.java : This class provides the following functions:
	  1. Connect and maintain an active connection to the server.
	  2. Regularly send data packets to the server. The payloads for these data packets are 8 KB and the values for these bytes are randomly generated. \
	  The rate at which each connection will generate packets is R per-second.
	  3. Tracks hashcodes of the data packets it has sent to the server. Checks received hashcodes against stored ones.
	
server
	* Server.java : This class proveds the following functions by relying on a thread pool.
	  1. Accepts incoming network connections from the clients.
	  2. Accepts incomming traffic from these connections.
	  3. Creates tasks for each message that reply to clients with a hash code for that message.
	
tasks
	* ClientSendTask.java : This class represents a thread responsible for the logistics of sending messages to the server.

	* ClientTask.java : This class represents a thread responsible for receiving replies from the server.

	* ServerTask.java : This class represents a single task that threads from a pool can be assigned to complete. Such a task involves reading a message from a channel, computing its hash code, and sending that hash code back to the client.

	* TestTask.java : This class is a way to test the functionality of the thread pool without any server/client activity.

threadpool
	* FixedThreadPool.java : This class is a thread safe implementation of a thread pool. It handles the creation of a thread pool of a fixed sized. It also allows a thread pool manager to retrieve available worker threads and assigned them tasks. Worker threads are able to add themselves back to the pool upon task completion.

	* ThreadPoolManager.java : This class manages the thread pool by maintaining a list of tasks that need to be completed and assigning those tasks to threads from the thread pool.

	* WorkerThread.java : This class allows threads to remain idle in a pool until assigned a task.

util
	* ClientLogger.java : This class handles client status logging and printing to the console. A timer object utilizes this task to print to the console every 20 seconds.

	* HashCache.java : This class is a thread safe way for clients to store the hash codes of messages they have sent. It allows clients to compare received hash codes to stored ones and removes them from the list in the case of a match.

	* HashGenerator.java : This class handles the generation of SHA1 hash codes and conversion of such codes to string representations.

	* ServerLogger.java : This class handles server status logging and printing to the console. It maintains throughput loggers for all clients and calculates the mean and standard deviation of per-client throughput.

	* ThroughputLogger.java : This class allows the server to track throughput for a single client.
