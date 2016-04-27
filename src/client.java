
import java.net.*;
import java.io.*;

class clientOne extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private static int counter = 0;
    public clientOne (InetAddress addr , int port) {
        System.out.println("Making client ");
        try {
            socket = new Socket(addr, port);
        } catch(IOException e) {
            // If the creation of the socket fails, nothing needs to be cleaned up
        }
        try {   
            in = new BufferedReader(
                    new InputStreamReader(
                        socket.getInputStream()));
            // Enable auto-flush:
            out = new PrintWriter(
                    new BufferedWriter(
                        new OutputStreamWriter(
                            socket.getOutputStream())), true);
            start();
        } catch(IOException e) {
            // The socket should be closed on any failures other than the socket constructor:
            try {
                socket.close();
            } catch(IOException e2) {}
        }
        // Otherwise the socket will be closed by the run() method of the thread.
    }
    public void run() {
        try {
            //for(int i = 0; i < 25; i++) {
        	int i = 0;
        	while(true){ i++;
                String str = in.readLine();
                System.out.println(str);
            }
            //out.println("END");
        } catch(IOException e) {
        } finally {
            // Always close it:
            try {
                socket.close();
            } catch(IOException e) {}
            
        }
    }
}

public class client {
    //static final int max_clients = 4;
    public static void main(String[] args)
            throws IOException, InterruptedException {
        InetAddress addr = InetAddress.getByName(args[0]);;
        int port = Integer.parseInt(args[1]);
        new clientOne(addr , port);
        /*while(true) {
            if(clientOne.threadCount()
                 < max_clients)
                new clientOne(addr , port);
            Thread.currentThread().sleep(100);
        }*/
    }
}

/*
public class client
{
   public static void main(String [] args)
   {
      String serverName = args[0];
      int port = Integer.parseInt(args[1]);
       
      try
      {
         System.out.println("Connecting to " + serverName +
		 " on port " + port);
         
         Socket client = new Socket(serverName, port);
         
         System.out.println("Just connected to " 
		 + client.getRemoteSocketAddress());
         
         OutputStream outToServer = client.getOutputStream();
         
         DataOutputStream out = new DataOutputStream(outToServer);
         InputStream inFromServer = client.getInputStream();
         DataInputStream in =
                 new DataInputStream(inFromServer);
         out.writeUTF("Hello from "
                      + client.getLocalSocketAddress());
         for (int i = 0; i<10;i++){
        	 out.writeUTF(Integer.toString(i));
	  	}
         for (int i = 0;i<10;i++){
        	 System.out.println("Server says " + in.readUTF());
         }
         client.close();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}
*/