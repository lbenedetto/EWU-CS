/**
 * Alex Toenniessen: ajt110195@gmail.com
 * Lars Benedetto: larsbenedetto@gmail.com
 * Richard Philips: mersennetwizter@gmail.com
 */
import java.util.Scanner;

public class a {
	private final static Scanner kb = new Scanner(System.in);
	private static int[] solved = new int[1000];

	public static void main(String[] args) {
		solved[0] = 1;
		int n;
		int p = kb.nextInt();
		for (int i = 0; i < p; i++) {
			n = kb.nextInt();
			System.out.println(i + 1 + ": " + n + " " + solve(n));
		}
	}

	private static int solve(int n) {
		//(n*(n+1))/2 for each layer
		//memoize the rest
		if (solved[n - 1] == 0) {
			int ans = solve(n - 1) + ((n * (n + 1)) / 2);
			solved[n - 1] = ans;
			return ans;
		} else return solved[n - 1];
	}
}
