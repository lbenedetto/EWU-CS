import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Hartal {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int testCases = Integer.parseInt(br.readLine());
		for (int i = 0; i < testCases; i++) {
			int days = Integer.parseInt(br.readLine());
			int parties = Integer.parseInt(br.readLine());
			int[] hartals = new int[parties];
			int j;
			for (j = 0; j < parties; j++) {
				hartals[j] = Integer.parseInt(br.readLine());
			}
			int daysLost = 0;
			for (j = 1; j <= days; j++) {
				if (j % 7 == 6 || j % 7 == 0) continue;
				for (int k = 0; k < parties; k++) {
					if (j % hartals[k] == 0) {
						daysLost++;
						break;
					}
				}
			}
			System.out.println(daysLost);
		}
	}
}
