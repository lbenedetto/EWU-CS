import java.util.Arrays;
public class Permutations {
	public static void main(String... args) {
		//Thanks Alex for these test cases
		int[][] d = {{2, 3, 1}, {1, 3, 2}, {1}, {1, 2}, {1, 2, 3}, {1, 2, 3, 4}, {2, 5, 4, 3, 1}, {3, 1, 2, 4, 5}, {5, 6, 7, 8, 9}, {6, 5, 4, 3, 2, 1}, {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}};
		for (int[] x : d) {
			System.out.println(Arrays.toString(x));
			System.out.println(Arrays.toString(findNext(x)) + "\n");
		}

	}

	private static int[] findNext(int[] digits) {
		int len = digits.length;
		int i, j;
		for (i = len - 1; i > 0; i--)//start right, traverse till number on left is smaller than number on right
			if (digits[i] > digits[i - 1])
				break;
		if (i == 0) {//If that doesn't happen, loop back to the smallest permutation
			reverseArray(digits);
			return digits;
		}
		int x = digits[i - 1], smallest = i;
		for (j = i + 1; j < len; j++)//Find the smallest digit on right side of (i-1)'th digit that is greater than digits[i-1]
			if (digits[j] > x && digits[j] < digits[smallest])
				smallest = j;

		swap(digits, smallest, i - 1);//swap them
		Arrays.sort(digits, i, len);//and sort the digits after i
		return digits;
	}

	private static void swap(int[] digits, int ix1, int ix2) {
		int temp = digits[ix1];
		digits[ix1] = digits[ix2];
		digits[ix2] = temp;
	}

	private static void reverseArray(int[] digits) {
		for (int i = 0; i < digits.length / 2; i++) {
			int temp = digits[i];
			digits[i] = digits[digits.length - i - 1];
			digits[digits.length - i - 1] = temp;
		}
	}
}