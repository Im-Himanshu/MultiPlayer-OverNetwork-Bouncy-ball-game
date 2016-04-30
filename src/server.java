import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class server {
	static Game g;

	// The server socket.
	private static ServerSocket serverSocket = null;
	// The client socket.
	private static Socket clientSocket = null;
	// The server can accept up to maxClients clients' connections.
	private static final int maxClients = 4;
	private static final clientThread[] threads = new clientThread[maxClients];

	public static void main(String args[]) {
		Game g2 = new Game(2);
		g = g2;
		// The default port number.
		int port = 1234;
		if (args.length > 0) {
			port = Integer.valueOf(args[0]).intValue();
			if (port < 1024) {
				port = 1234;
			}
		}
		// Open a server socket on the port (default 3333).
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println(e);
		}

		// Create a client socket for each connection and pass it to a new
		// client thread.
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				DataInputStream is = new DataInputStream(
						clientSocket.getInputStream());
				// will connect the client to server or supress the computer
				// player at that spot;
				String output = is.readLine();
				g.connectclient(output);
				int i = 0;
				for (i = 0; i < maxClients; i++) {
					if (threads[i] == null) {
						(threads[i] = new clientThread(clientSocket, threads))
								.start();
						break;
					}
				}
				if (i == maxClients) {
					PrintStream os = new PrintStream(
							clientSocket.getOutputStream());
					os.println("maximum number of players reached. Start a new game");
					os.close();
					clientSocket.close();
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
}

/*
	This client thread opens the input and the output streams for a particular
	client. starts the server players game, receives the data from all the clients ,
	process the data and send the precesed info to all the clients. 
 */
class clientThread extends Thread {

	private String clientName = null;
	private DataInputStream is = null;
	private PrintStream os = null;
	private Socket clientSocket = null;
	private final clientThread[] threads;
	private int maxClients;

	public clientThread(Socket clientSocket, clientThread[] threads) {
		this.clientSocket = clientSocket;
		this.threads = threads;
		maxClients = threads.length;
	}

	public void run() {
		int maxClients = this.maxClients;
		clientThread[] threads = this.threads;

		try {
			// Create input and output streams for this client.
			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());

			while (true) {
				Game g = Game.gameobject;
				String line = is.readLine();
				g.serverprocessor(line);
				if (line.startsWith("/quit")) {
					break;

				}
				synchronized (this) {
					for (int i = 0; i < maxClients; i++) {
						String output = g.serversend();
						if (threads[i] != null && threads[i] == this) {
							threads[i].os.println(output);
						}
					}
				}
			}
			/*
			 * Clean up. Set the current thread variable to null so that a new
			 * client could be accepted by the server.
			 */
			synchronized (this) {
				for (int i = 0; i < maxClients; i++) {
					if (threads[i] == this) {
						threads[i] = null;
					}
				}
			}
			// Close the output stream, close the input stream, close the socket.
			is.close();
			os.close();
			clientSocket.close();
		} catch (IOException e) {
		}
	}
}