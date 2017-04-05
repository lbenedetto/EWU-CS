import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class PrefixTree {
	private final Node root;

	PrefixTree(String filename) {
		root = new Node();
		try {
			Files.lines(Paths.get(filename)).forEach(x -> insert(x.split(",")[0]));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Inserts a word into the trie.
	private void insert(String word) {
		Node curr = root;
		char[] chars = word.toCharArray();
		for (int i = 0; i < word.length(); i++) {
			char c = chars[i];
			int index = c - 'a';
			if (curr.nodes[index] == null) {
				Node temp = new Node(curr.prefix, c);
				curr.nodes[index] = temp;
				curr = temp;
			} else {
				curr = curr.nodes[index];
			}
		}
		curr.isWord = true;
	}

	// Returns if the word is in the trie.
	boolean hasWord(String word) {
		Node n = search(word);
		return !(n == root || n == null) && n.isWord;
	}

	boolean hasPrefix(String prefix) {
		Node n = search(prefix);
		return n != root && n != null;
	}

	private Node search(String s) {
		Node curr = root;
		char[] chars = s.toCharArray();
		for (int i = 0; i < s.length(); i++) {
			char c = chars[i];
			int index = c - 'a';
			if (curr.nodes[index] != null) {
				curr = curr.nodes[index];
			} else {
				return null;
			}
		}
		return curr;
	}

	class Node {
		final Node[] nodes;
		boolean isWord;
		String prefix = "";

		Node(String prefix, char c) {
			this.prefix = prefix + c;
			this.nodes = new Node[26];
		}

		Node() {
			this.prefix = "";
			this.nodes = new Node[26];
		}

		public String toString() {
			return prefix;
		}
	}
}