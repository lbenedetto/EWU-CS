import java.util.ArrayList;
import java.util.Scanner;

/**
 * For submission to online judge
 */
class Main {
	public static void main(String[] args) {
		String input;
		Scanner kb = new Scanner(System.in);
		while (kb.hasNextLine()) {
			input = kb.nextLine();
			ArrayList<Integer> results = new ArrayList<>();
			int[] ints = readInts(input);
			int result;
			boolean jolly = true;
			assert ints != null;
			for (int i = 1; i < ints.length - 1; i++) {
				result = Math.abs(ints[i] - ints[i + 1]);
				if (result == 0 || result >= ints[0] || results.contains(result)) {
					jolly = false;
				}
				results.add(result);
			}
			String j = jolly ? "Jolly" : "Not jolly";
			System.out.println(j);
		}
	}

	private static int[] readInts(String s) {
		try {
			Scanner scanner = new Scanner(s);
			int firstInt = scanner.nextInt();
			int[] ints = new int[firstInt + 1];
			ints[0] = firstInt;
			int i = 1;
			while (scanner.hasNextInt()) {
				ints[i] = scanner.nextInt();
				i++;
			}
			return ints;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Invalid Input");
		}
		System.exit(-1);
		return null;
	}
}
