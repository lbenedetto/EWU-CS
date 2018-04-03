import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Generator {
	public static void generateResults(int candidates, int ballots) {
		PrintStream out = System.out;
		String[] names = new String[]{"George Washington",
				"John Adams",
				"Thomas Jefferson",
				"James Madison",
				"James Monroe",
				"John Quincy Adams",
				"Andrew Jackson",
				"Martin Van Buren",
				"William H. Harrison",
				"John Tyler",
				"James K. Polk",
				"Zachary Taylor",
				"Millard Fillmore",
				"Franklin Pierce",
				"James Buchanan",
				"Abraham Lincoln",
				"Andrew Johnson",
				"Ulysses S. Grant",
				"Rutherford B. Hayes",
				"James A. Garfield",
				"Chester A. Arthur"};
		ArrayList<Integer> nums = new ArrayList<>();
		out.println(1 + "\n");
		out.println(candidates);
		for (int i = 0; i < candidates; i++) {
			out.println(names[i]);
			nums.add(i+1);
		}
		for (int i = 0; i < ballots; i++) {
			long seed = System.nanoTime();
			Collections.shuffle(nums, new Random(seed));
			Collections.shuffle(nums, new Random(seed));
			for (Integer j : nums) {
				out.print(j + " ");
			}
			out.println();
		}
	}
}
