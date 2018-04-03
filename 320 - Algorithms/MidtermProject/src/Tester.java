import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

class Tester {
	public static void main(String[] args) {
		WordTree keypadTree;
		HashSet<String> hashDictionary = new HashSet<>();
		PrefixTree prefixTreeDictionary = new PrefixTree("dictionary.txt");
		try {
			Files.lines(Paths.get("dictionary.txt")).forEach(x -> hashDictionary.add(x.split(",")[0]));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scanner kb = new Scanner(System.in);
		System.out.println("Type \"exit\" to exit");
		System.out.print("Enter keypad input:");
		String input = kb.nextLine();
		while(!input.equals("exit")){
			try {
				int[] keypad = Arrays.stream(input.split("")).mapToInt(Integer::parseInt).toArray();
				keypadTree = new WordTree(keypad);
				ArrayList<String> hashPaths = keypadTree.getAllWords(hashDictionary);
				ArrayList<String> prefixPaths = keypadTree.getAllWords(prefixTreeDictionary);
				ArrayList<String> prefixPathsEfficient = keypadTree.getAllWordsEfficiently(prefixTreeDictionary);
				System.out.println("Words found with HashDictionary (ExhaustiveSearch): ");
				hashPaths.forEach(x -> System.out.print("|" + x + "|"));
				System.out.println("\nWords found with PrefixTree (ExhaustiveSearch): ");
				prefixPaths.forEach(x -> System.out.print("|" + x + "|"));
				System.out.println("\nWords found with PrefixTree (Branch and Bound): ");
				prefixPathsEfficient.forEach(x -> System.out.print("|" + x + "|"));
				System.out.println();
			}catch (NumberFormatException e){
				System.out.println("Invalid input");
			}
			System.out.print("Enter keypad input:");
			input = kb.nextLine();
		}
		System.out.println("Exiting...");
	}
}
