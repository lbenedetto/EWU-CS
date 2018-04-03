import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.stream.Stream;

public class Sieve {
	public static void main(String[] args) {
		try {
			BufferedReader in = new BufferedReader(new FileReader("input.txt"));
			while (true) {
				System.out.println(Arrays.toString(sieveSort(Stream.of(in.readLine().split(" ")).mapToInt(Integer::parseInt).toArray())));
			}
		} catch (Exception e) {
			System.exit(0);
		}
	}

	private static int[] sieveSort(int[] arr) {
		int[] array = new int[16000];
		int[] repArray = new int[16000];
		for (int cd : arr) {
			array[cd] = cd;
			repArray[cd]++;
		}
		arr = new int[arr.length];
		int j = 0;
		for (int i : array) {
			if (i != 0) {
				for (int rep = 0; rep < repArray[i]; rep++) {
					arr[j] = i;
					j++;
				}
			}
		}
		return arr;
	}
}
