import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class TestParser {
	/**
	 * The assignment says to use both infix.txt and input.txt
	 * "Your input file will be named infix.txt"
	 * "...your input file (input.txt)..."
	 */
	public static void main(String[] args) {
		BufferedReader in = null;
		try {
			in = run("infix.txt");
		} catch (FileNotFoundException e) {
			System.out.println("Could not open infix.txt, looking for input.txt");
			try {
				in = run("input.txt");
			} catch (FileNotFoundException ex) {
				System.out.println("Could not find input.txt, exiting");
				System.exit(-1);
			}
		}
		System.out.println("Parsing file");
		try {
			String nextLine = in.readLine();
			while (nextLine != null) {
				System.out.println("~~~~~~~~~~~~~~~~~~~~");
				if (Parser.parse(nextLine)) {
					System.out.println("Valid: " + nextLine);
				} else {
					System.out.println("NOT Valid: " + nextLine);
				}
				nextLine = in.readLine();
			}
		} catch (IOException e) {
			System.out.println("Could not read file");
		}
	}

	public static BufferedReader run(String filename) throws FileNotFoundException {
		BufferedReader in;
		in = new BufferedReader(new FileReader(filename));
		return in;
	}
}
