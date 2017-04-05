import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Supervisors {
	private static int[][] supervisors;
	private static int[][] employees;

	public static void main(String[] args) {
		try {
			int[][] permutations = new int[5040][7];
			int[] digits = {7, 6, 5, 4, 3, 2, 1};
			for (int i = 0; i < 5040; i++) {
				digits = Permutations.findNext(digits);
				permutations[i] = digits.clone();
			}
			BufferedReader in = new BufferedReader(new FileReader("input.txt"));
			int dataSets = Integer.valueOf(in.readLine());
			in.readLine();
			for (int i = 1; i <= dataSets; i++) {
				supervisors = new int[7][7];

				employees = new int[7][7];
				for (int j = 0; j < 7; j++) {
					supervisors[j] = Stream.of(in.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
				}
				for (int j = 0; j < 7; j++) {
					employees[j] = Stream.of(in.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
				}
				double lowestScore = 8.0;
				ArrayList<int[]> bestPermutations = new ArrayList<>();
				double score;
				for (int[] n : permutations) {
					score = score(n);
					if (score < lowestScore) {
						lowestScore = score;
						bestPermutations = new ArrayList<>();
						bestPermutations.add(n);
					} else if (score == lowestScore) {
						bestPermutations.add(n);
					}
				}
				System.out.printf("Data Set %d, Best average difference: %.6f\n", i, lowestScore);
				int j = 1;
				for (int[] n : bestPermutations) {
					System.out.println("Best Pairing " + j);
					score(n);
					for (int k = 0; k < 7; k++) {
						System.out.println("Supervisor " + (k + 1) + " with Employee " + n[k]);
					}
					j++;
				}
				in.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static double score(int[] permutation) {
		List<Integer> employeePermutation = new ArrayList<>(permutation.length);
		for (int aPermutation : permutation) employeePermutation.add(aPermutation);
		int diff = 0;
		for (int i = 0; i < 7; i++) {
			int[] supPref = supervisors[i];
			int[] empPref = employees[i];
			for (int j = 0; j < 7; j++)
				if (supPref[j] == permutation[i]) {
					diff += j;
					break;
				}
			int g = employeePermutation.indexOf(i + 1) + 1;
			for (int j = 0; j < 7; j++)
				if (empPref[j] == g) {
					diff += j;
					break;
				}
		}
		return diff / 14.0;
	}

	public static class Permutations {

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
}
