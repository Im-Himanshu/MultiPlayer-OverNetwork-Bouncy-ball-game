import java.net.*;
import java.io.*;

public class server extends Thread
{
   private ServerSocket serverSocket;
   
   public server(int port) throws IOException
   {
      serverSocket = new ServerSocket(port);
      serverSocket.setSoTimeout(100000);
   }

   public void run()
   {
      while(true)
      {
         try
         {
            System.out.println("Waiting for client on port " +
            serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            System.out.println("Just connected to "
                  + server.getRemoteSocketAddress());
            DataInputStream in =
                    new DataInputStream(server.getInputStream());
            DataOutputStream out =
                    new DataOutputStream(server.getOutputStream());
               out.writeUTF("Thank you for connecting to "
                 + server.getLocalSocketAddress() + "\nGoodbye!");
            for(int i = 0;i<10;i++){
	            String received = in.readUTF();
	            if(received != null){
		            System.out.println(received);
		            out.writeUTF("received packet" + (received));
	            }
            }
            server.close();
         }catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e)
         {
            e.printStackTrace();
            break;
         }
      }
   }
   public static void main(String [] args)
   {
      int port = Integer.parseInt(args[0]);
      try
      {
         Thread t = new server(port);
         t.start();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}