

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class client implements Runnable {
	client c;
	static Game game;
	private static Socket clientSocket = null; // The client socket
	private static PrintStream os = null; // The output stream
	private static DataInputStream is = null; // The input stream
	private static BufferedReader inputLine = null;
	private static boolean closed = false;

	public static void main(String[] args) {
		int port = 1234; // The default port.
		String host = "localhost"; // The default host.
		if (args.length > 1) {
			{
				host = args[0]; // if ip and port are provided, use them
				port = Integer.valueOf(args[1]).intValue();
				if (port < 1024) { // port less than 1024 are for privileged
									// users.
					port = 1234;
				}
			}

			// Open a socket on a given host and port. Open input and output
			// streams.
			try {
				clientSocket = new Socket(host, port);
				// sending info to connect to server.. .
				inputLine = new BufferedReader(new InputStreamReader(System.in));
				os = new PrintStream(clientSocket.getOutputStream());
				is = new DataInputStream(clientSocket.getInputStream());
				int spot = Integer.parseInt(is.readLine());
				System.out.print(spot);
				Game g = new Game(2);
				game = g;
				game.playersetter(spot);
				String output = "" + game.crntplyr;
				os.println(output);
				System.out.print(output);
			} catch (UnknownHostException e) {
				System.err.println("Don't know about host " + host);
			} catch (IOException e) {
				System.err
						.println("Couldn't get I/O for the connection to the host "
								+ host);
			}
			/*
			 * If everything has been initialized then we want to write some
			 * data to the socket we have opened a connection to on the port
			 * port.
			 */
			if (clientSocket != null && os != null && is != null) {
				try {
					/* Create a thread to read from the server. */
					new Thread(new client()).start();
					while (!closed) {
						// sending game data every 20 milisecond
						// game = Game.gameobject;
						// os.println(inputLine.readLine().trim());
						// String s = inputLine.readLine();
						String output = game.clientsend();
						os.println(output);

						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					// Close the output stream, close the input stream, close
					// the socket.
					os.close();
					is.close();
					clientSocket.close();

				} catch (IOException e) {
					System.err.println("IOException:  " + e);
				}
			}
		}
	}

	/*
	 * Create a thread to read from the server. (non-Javadoc)
	 */
	@SuppressWarnings("deprecation")
	public void run() {
		/*
		 * Keep on reading from the socket till we receive "Bye" from the
		 * server. Once we received that then we want to break.
		 */
		String responseLine;
		try {
			while ((responseLine = is.readLine()) != null) {
				Game g = Game.gameobject;
				g.clientprocessor(responseLine);
				if (responseLine.indexOf("*** Bye") != -1)
					break;
			}
			closed = true;
		} catch (IOException e) {
			System.err.println("IOException:  " + e);
		}
	}
}