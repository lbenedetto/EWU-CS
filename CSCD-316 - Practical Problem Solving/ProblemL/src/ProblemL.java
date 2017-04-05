import java.io.FileNotFoundException;
import java.util.Scanner;

public class ProblemL {
	public static void main(String[] args) {
		try {
			IterableFile file = new IterableFile("input.txt");
			for (String s : file) {
				Scanner scanner = new Scanner(s);
				int base = scanner.nextInt();
				if (base == 0) break;
				int one = scanner.nextInt();
				int two = scanner.nextInt();
				String oneOut = Integer.toString(one, base);
				String twoOut = Integer.toString(two, base);
				String out = Integer.toString((one + two), base);
				System.out.println(oneOut + " + " + twoOut + " = " + out);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
