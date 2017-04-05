import javax.xml.bind.ValidationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Lars Benedetto
 *         Problem: JollyJumpers
 *         Edge Cases: Used critical input from online debugger, plus some other ones, tested for negatives
 */
class JollyJumpers {

	public static void main(String[] args) throws ValidationException, IOException {
		String infilename = "";
		if (args.length != 0)
			infilename = args[0];
		if (infilename.isEmpty()) {
			infilename = "input.txt";
		}
		IterableFile in = new IterableFile(infilename);
		PrintWriter out = new PrintWriter("output.txt");

		for (String s : in) {
			ArrayList<Integer> results = new ArrayList<>();
			int[] ints = readInts(s);
			int result;
			boolean jolly = true;
			for (int i = 1; i < ints.length - 1; i++) {
				result = Math.abs(ints[i] - ints[i + 1]);
				if (result == 0 || result >= ints[0] || results.contains(result)) {
					jolly = false;
				}
				results.add(result);
			}
			String j = jolly ? "Jolly" : "Not jolly";
			System.out.println(j);
			out.println(j);
		}
		out.close();
	}

	private static int[] readInts(String s) {
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
	}


	private static class IterableFile implements Iterable<String> {
		private BufferedReader file;

		IterableFile(String fileName) throws FileNotFoundException {
			file = new BufferedReader(new FileReader(fileName));
		}

		/**
		 * Returns an iterator over elements of type {@code T}.
		 *
		 * @return an Iterator.
		 */
		@Override
		public FileIterator iterator() {
			FileIterator fi = new FileIterator();
			fi.next();
			return fi;
		}

		private class FileIterator implements Iterator<String> {
			String nextLine;
			String curr;


			@Override
			public boolean hasNext() {
				return nextLine != null;
			}

			/**
			 * Returns the next element in the iteration.
			 *
			 * @return the next element in the iteration
			 */
			@Override
			public String next() {
				curr = nextLine;
				try {
					nextLine = file.readLine();
					while (nextLine.equals("#"))
						nextLine = file.readLine();
				} catch (IOException | NullPointerException e) {
					nextLine = null;
				}
				return curr;
			}
		}
	}
}