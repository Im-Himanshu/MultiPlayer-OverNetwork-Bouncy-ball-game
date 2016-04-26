package bouncyballgame;

import java.util.Random;

public class demo {
	
	public static void main (String ...a){
		
		Random r = new Random();
		
		double i = r.nextGaussian()*2+ 2 ;
		System.out.println(i);

		
		/*int count = 0;

		int sum = 0;
		while(count < 50){
		i = r.nextGaussian();
		sum +=i;
		System.out.println(i);
		count++;
		}
		System.out.println(sum/count);
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(count < 50){
			i = r.nextGaussian();
			sum +=i;
			System.out.println(i);
			count++;
			}
		System.out.println(sum/(count*2));
*/		
	}

}
