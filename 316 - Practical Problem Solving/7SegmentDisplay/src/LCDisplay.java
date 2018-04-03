import java.io.BufferedReader;
import java.io.FileReader;
import java.util.stream.Stream;

public class LCDisplay {
	//UVa 706 - LC-Display
	public static void main(String[] args) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader("input.txt"));
		String line;
		while ((line = in.readLine()) != null) {
			String[] input = line.split(" ");
			int width = Integer.parseInt(input[0]);
			int[] nums = Stream.of(input[1].split("")).mapToInt(Integer::parseInt).toArray();
			Digit[] digits = new Digit[nums.length];
			for (int i = 0; i < nums.length; i++) {
				digits[i] = new Digit(getBooleanArray(nums[i]), width);
			}
			for (int y = 0; y < (width * 2) + 3; y++) {
				for (Digit digit : digits) {
					System.out.print(digit.toString(y) + " ");
				}
				System.out.println();
			}
		}
	}

	private static boolean[] getBooleanArray(int n) {
		boolean[] segments = new boolean[7];
		segments[0] = n != 1 && n != 4;
		segments[1] = n != 5 && n != 6;
		segments[2] = n != 2;
		segments[3] = n != 1 && n != 4 && n != 7;
		segments[4] = n == 0 || n == 2 || n == 6 || n == 8;
		segments[5] = n != 1 && n != 2 && n != 3 && n != 7;
		segments[6] = n != 0 && n != 1 && n != 7;
		return segments;
	}
	static class Digit {
		private String[][] pixels;

		Digit(boolean[] segments, int width) {
			int actualWidth = width + 2;
			int actualHeight = (width * 2) + 3;
			pixels = new String[actualHeight][actualWidth];
			for (int y = 0; y < actualHeight; y++) {
				for (int x = 0; x < actualWidth; x++) {
					pixels[y][x] = " ";
				}
			}
			if (segments[0])//Segment 1
				for (int x = 1; x < actualWidth - 1; x++)
					pixels[0][x] = "-";
			if (segments[1])//Segment 2
				for (int y = 1; y < actualWidth - 1; y++)
					pixels[y][actualWidth - 1] = "|";
			if (segments[2])//Segment 3
				for (int y = actualWidth; y < actualHeight - 1; y++)
					pixels[y][actualWidth - 1] = "|";
			if (segments[3])//Segment 4
				for (int x = 1; x < actualWidth - 1; x++)
					pixels[actualHeight - 1][x] = "-";
			if (segments[4])//Segment 5
				for (int y = actualWidth; y < actualHeight - 1; y++)
					pixels[y][0] = "|";
			if (segments[5])//Segment 6
				for (int y = 1; y < actualWidth - 1; y++)
					pixels[y][0] = "|";
			if (segments[6]) //Segment 7
				for (int x = 1; x < actualWidth - 1; x++)
					pixels[actualWidth - 1][x] = "-";
		}

		String toString(int y) {
			String out = "";
			String [] row = pixels[y];
			for (String s : row)
				out += s;
			return out;
		}
	}

}
