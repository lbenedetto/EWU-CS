/**
 * Alex Toenniessen: ajt110195@gmail.com
 * Lars Benedetto: larsbenedetto@gmail.com
 * Richard Philips: mersennetwizter@gmail.com
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class b {
	private final static Scanner kb = new Scanner(System.in);

	public static void main(String[] args) {
		//Just use regex instead of actually thinking
		int t = kb.nextInt();
		for (int i = 0; i < t; i++) {
			System.out.println("Test set " + i + ":");
			int n = kb.nextInt();
			kb.nextLine();
			ArrayList<String> names = new ArrayList<>();
			for (int j = 0; j < n; j++) {
				names.add(kb.nextLine());
			}
			int d = kb.nextInt();
			kb.nextLine();
			String dataStream = "";
			for (int k = 0; k < d; k++) {
				dataStream += kb.nextLine() + " ";
			}
			for (String name : names) {
				System.out.print(name + " is ");
				if (Pattern.compile(" " + name + " ").matcher(dataStream).matches()) {
					System.out.println("present");
				} else {
					System.out.println("absent");
				}
			}
			System.out.println();
		}
	}
}
