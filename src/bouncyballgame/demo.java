package bouncyballgame;

import java.awt.Container;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPanel;

public class demo extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		public static void main(String... a) throws UnknownHostException {
			InetAddress IP=InetAddress.getLocalHost();
			System.out.println("IP of my system is := "+IP.getHostAddress());
		/*
		 * int count = 0;
		 * 
		 * Random r = new Random();
		 * 
		 * double i = r.nextGaussian()*2+ 2 ; System.out.println(i);
		 * 
		 * int sum = 0; while(count < 50){ i = r.nextGaussian(); sum +=i;
		 * System.out.println(i); count++; } System.out.println(sum/count); try
		 * { Thread.sleep(300); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } while(count < 50){
		 * i = r.nextGaussian(); sum +=i; System.out.println(i); count++; }
		 * System.out.println(sum/(count*2));
		 */
	}

}
