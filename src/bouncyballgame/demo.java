package bouncyballgame;

public class demo {

	public static void main(String... a) {
		String s = "2,3,4";
		String ary[] = s.split(",");
		int number = Integer.valueOf(ary[1]).intValue();
		System.out.println(number);
		while (true) {

			System.out.println("hello");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

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
