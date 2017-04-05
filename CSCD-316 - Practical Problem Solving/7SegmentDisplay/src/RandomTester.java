import java.util.Random;

public class RandomTester {
	public static void main(String[] args) {
		Random r = new Random();
		int zero = 0;
		int one = 0;
		int two = 0;
		int three = 0;
		for (int i = 0; i < 100000; i++) {
			int rand = r.nextInt(4);
			switch (rand) {
				case 0:
					zero++;
					break;
				case 1:
					one++;
					break;
				case 2:
					two++;
					break;
				case 3:
					three++;
					break;
			}
			System.out.print(rand);
			if(i%100==0)System.out.println();
		}
		System.out.println("Zero: " + zero);
		System.out.println("One: " + one);
		System.out.println("Two: " + two);
		System.out.println("Three: " + three);
	}
}
