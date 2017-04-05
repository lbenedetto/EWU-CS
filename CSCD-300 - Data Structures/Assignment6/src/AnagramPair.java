import java.util.Arrays;

public class AnagramPair {
	public String word;
	public String key;
	public int hash;

	public AnagramPair(String word) {
		this.word = word;
		key = sort(word);
		hash = hashCode(key);
	}

	public String sort(String s) {
		char[] chars = s.toCharArray();
		Arrays.sort(chars);
		return new String(chars);
	}

	/**
	 * Based on the hashCode function found in the decompiled String.java
	 */
	public int hashCode(String str) {
		char[] s = str.toCharArray();
		int total = 1;
		for (char value : s) {
			total = 34 * total + value;
		}
		total = Math.abs(total);
		if(HashTester.useJavaAPIForHashCodeFunction)
			total = Math.abs(str.hashCode());
		while (total > HashTester.hashTableSize) {
			total %= HashTester.hashTableSize;
		}
		return total;
	}
}
