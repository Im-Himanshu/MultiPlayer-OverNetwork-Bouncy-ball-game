import java.net.*;
import java.io.*;

class serverOne extends Thread
{
   private ServerSocket serverSocket;
   private Socket socket;
   private BufferedReader in;
   private PrintWriter out;
   public serverOne(Socket s) throws IOException {
       socket = s;
       in = new BufferedReader(
               new InputStreamReader(
                   socket.getInputStream()));
       out = new PrintWriter(
               new BufferedWriter(
                   new OutputStreamWriter(
                       socket.getOutputStream())), true);
       start(); // Calls run()
   }
   
   public void run()
   {
	   try {
           while (true) { 
               String input = in.readLine();
               if (input.equals("END")) break;
               System.out.println("Echoing: " + input);
               out.println(input);
           }
           System.out.println("closing...");
       } catch (IOException e) {
       } finally {
           try {
               socket.close();
           } catch(IOException e) {}
       }
   }
}
   /*
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
   */
public class server {    
   public static void main(String [] args) throws IOException
   {
	   int clients = 0;			// number of clients connected to the server
	   int port = Integer.parseInt(args[0]);
	   ServerSocket s = new ServerSocket(port);
       try
       {
    	  while(true && clients < 4) {
              Socket socket = s.accept();
              clients++;
              try {
                  new serverOne(socket);
              } catch(IOException e) {
                  socket.close();
              }
          }
       } finally {
          s.close();
       }
   }
}