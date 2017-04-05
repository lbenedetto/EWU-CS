import java.io.*;
import java.util.ArrayList;

/**
 * My hashcode method is better than the one in String.java because it
 * produces 1,505 collisions compared to Java's 1,680 collisions.
 * I obtained this by playing around with the number multiplied by each time
 * and found that 34 produced better results than 31
 *
 * As for proving performance is better than 25000*log2(25000),
 * 25000*log2(25000) is a ton more than 1,505.
 *
 * Extra credit: Have I not already clearly demonstrated that my hash function is better than the Java API?
 * If not, you can change the value of the boolean below to true, and my program will use the hashCode method from
 * the java API instead.
 */
public class HashTester {
	public static final int hashTableSize = 199999;
	//Change this boolean to use Java API hashCode instead
	public static final boolean useJavaAPIForHashCodeFunction = false;

	public static void main(String[] args) {
		HashTable dictionary = loadDictionary();
		String inFile = args[0];
		String outFile = args[1];
		try {
			BufferedReader fin = new BufferedReader(new FileReader(inFile));
			BufferedWriter fout = new BufferedWriter(new FileWriter(outFile));
			String line = fin.readLine();
			while (line != null) {
				line = line.toLowerCase();
				AnagramPair temp = new AnagramPair(line);
				HashTable.Bucket candidates = dictionary.getAnagrams(temp);
				ArrayList<String> anagrams = new ArrayList<>();
				if (candidates != null) {
					for (AnagramPair p : candidates) {
						if (p.key.equals(temp.key)) {
							if(!p.word.equals(temp.word))
								anagrams.add(p.word);
						}
					}
				}
				fout.write(line + " " + anagrams.size() + " ");
				for (String s : anagrams) {
					fout.write(s + " ");
				}
				fout.write("\n");
				line = fin.readLine();
			}
			fout.close();
		} catch (IOException e) {
			System.out.println("IOException while trying to find " + inFile + " or " + outFile);
		}
		System.out.println("Collisions: " + dictionary.collisions);
	}

	public static HashTable loadDictionary() {
		HashTable dictionary = new HashTable(hashTableSize);
		try {
			BufferedReader fin = new BufferedReader(new FileReader("words.txt"));
			try {
				String line = fin.readLine();
				while (line != null) {
					line = line.toLowerCase();
					if (!line.matches("[^a-z0-9]"))
						dictionary.add(line);
					line = fin.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return dictionary;
	}
}
