
import java.net.*;
import java.io.*;

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