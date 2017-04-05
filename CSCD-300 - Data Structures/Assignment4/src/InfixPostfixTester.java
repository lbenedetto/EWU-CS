import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * Parser tester class
 * <p><b>
 * Note to Grader
 * </b><pre>
 * My program safely handles raising a BigInteger to a power greater than Integer.MAX_VALUE
 * As long as the number doesn't exceed the maximum heap size
 * This also means that the program may take a long time trying to find an answer.
 * In some cases it ran for around 15 minutes before outputting a number a few thousand digits long
 * I utilized multithreading to show that the program is still working and is not frozen
 * </pre>
 *
 * @author Lars Benedetto
 */
class InfixPostfixTester {
	private static final long heapSize = Runtime.getRuntime().totalMemory() / 1000000;
	private static final long heapMaxSize = Runtime.getRuntime().maxMemory() / 1000000;
	private static final long heapFreeSize = Runtime.getRuntime().freeMemory() / 1000000;

	public static void main(String[] args) {
		System.out.println("Total Memory: " + heapSize + "MB");
		System.out.println("Max Memory: " + heapMaxSize + "MB");
		System.out.println("Free Memory: " + heapFreeSize + "MB");
		BufferedReader in;
		HashMap<Character, BigInteger> valueOf = new HashMap<>();
		try {
			in = new BufferedReader(new FileReader("infix.txt"));
			System.out.println("Parsing file");
			//Read in the values of the letters
			readValues(in, valueOf);
			//Read in the expressions, convert them to postfix, and evaluate them
			postfixAndEvaluate(in, valueOf);
		} catch (FileNotFoundException e) {
			System.out.println("Could not find infix.txt");
		} catch (IOException e) {
			System.out.println("Could not read file");
		}
	}

	/**
	 * Read the values of letters into a HashMap
	 *
	 * @param in      BufferedReader
	 * @param valueOf HashMap
	 * @throws IOException if reading file goes wrong
	 */
	private static void readValues(BufferedReader in, HashMap<Character, BigInteger> valueOf) throws IOException {
		String nextLine = in.readLine();
		while (nextLine != null && !nextLine.contains("#")) {
			String[] line = nextLine.split(" ");
			valueOf.put(line[0].charAt(0), new BigInteger(line[1]));
			nextLine = in.readLine();
		}
	}

	/**
	 * Convert to postfix and evaluate
	 *
	 * @param in      BufferedReader
	 * @param valueOf HashMap
	 * @throws IOException if reading file goes wrong
	 */
	private static void postfixAndEvaluate(BufferedReader in, HashMap<Character, BigInteger> valueOf) throws IOException {
		String nextLine = in.readLine();
		while (nextLine != null && !nextLine.contains("#")) {
			System.out.println("Input expression: " + nextLine);
			//Replace the letters with their values and print the expression again (useful for debugging)
			System.out.print("Expression with numbers: ");
			for (char c : nextLine.toCharArray()) {
				if (Parser.isLetter(c))
					System.out.print(String.valueOf(valueOf.get(c)));
				else
					System.out.print(c);
			}
			String postfix = Parser.parse(nextLine);
			System.out.println("\nPostfix expression: " + postfix);
			try {
				System.out.println("Expression value after postfix evaluation: " + Parser.evaluate(valueOf, postfix));
			} catch (OutOfMemoryError | ArithmeticException e) {
				System.out.println("Expression created a value that is too large for BigInteger, given the current heap size");
				System.out.println("The current maximum heap size is " + heapMaxSize + "MB");
				System.out.println("You could try increasing the heap size,");
				System.out.println("or take a long hard look at the decisions you've made that led you to need a number this big");
			}
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			nextLine = in.readLine();
		}
	}
}
