import java.util.*;

class WordTree {
	private static final Character[][] numsToChars = {{null, null, null, null}, {null, null, null, null},
			{'a', 'b', 'c', null}, {'d', 'e', 'f', null},
			{'g', 'h', 'i', null}, {'j', 'k', 'l', null},
			{'m', 'n', 'o', null}, {'p', 'q', 'r', 's'},
			{'t', 'u', 'v', null}, {'w', 'x', 'y', 'z'}};
	private static int maxDepth;
	private Node root;

	WordTree(int[] digits) {
		maxDepth = digits.length;
		Arrays.stream(digits).forEach(this::insert);
	}

	WordTree(int i) {
		String d = Integer.toString(i);
		d = d.replaceAll("0", "");
		d = d.replaceAll("1", "");
		String[] digits = d.split("");
		maxDepth = digits.length;
		Arrays.stream(digits).mapToInt(Integer::parseInt).forEach(this::insert);
	}

	private void insert(int i) {
		Queue<Node> q = new LinkedList<>();
		if (root == null) root = new Node().insertChildren(i);
		else {
			q.add(root);
			while (!q.isEmpty()) {
				Node curr = q.peek();
				if (curr.n1 == null) {
					curr.insertChildren(i);
				} else {
					q.add(curr.n1);
					q.add(curr.n2);
					q.add(curr.n3);
					if (curr.n4 != null)
						q.add(curr.n4);
				}
				q.remove();
			}
		}
	}

	ArrayList<String> getAllWords(HashSet<String> dictionary) {
		ArrayList<String> paths = getAllPaths();
		ArrayList<String> words = new ArrayList<>();
		paths.stream().filter(dictionary::contains).forEach(words::add);
		return words;
	}

	ArrayList<String> getAllWords(PrefixTree dictionary) {
		ArrayList<String> paths = getAllPaths();
		ArrayList<String> words = new ArrayList<>();
		paths.stream().filter(dictionary::hasWord).forEach(words::add);
		return words;
	}

	ArrayList<String> getAllWordsEfficiently(PrefixTree dictionary) {
		ArrayList<String> paths = getAllPathsEfficiently(dictionary);
		ArrayList<String> words = new ArrayList<>();
		paths.stream().filter(dictionary::hasWord).forEach(words::add);
		return words;
	}

	private ArrayList<String> getAllPaths() {
		ArrayList<String> paths = new ArrayList<>();
		Queue<Node> q = new LinkedList<>();
		q.add(root);
		while (!q.isEmpty()) {
			Node curr = q.peek();
			if (curr.n1 != null)
				q.add(curr.n1);
			if (curr.n2 != null)
				q.add(curr.n2);
			if (curr.n3 != null)
				q.add(curr.n3);
			if (curr.n4 != null)
				q.add(curr.n4);
			if (curr.word != null && curr.word.length() == maxDepth)
				paths.add(curr.word);
			q.remove();
		}
		return paths;
	}

	private ArrayList<String> getAllPathsEfficiently(PrefixTree dict) {
		ArrayList<String> paths = new ArrayList<>();
		Queue<Node> q = new LinkedList<>();
		if (root.n1 != null) q.add(root.n1);
		if (root.n2 != null) q.add(root.n2);
		if (root.n3 != null) q.add(root.n3);
		if (root.n4 != null) q.add(root.n4);
		while (!q.isEmpty()) {
			Node curr = q.peek();
			if (dict.hasPrefix(curr.word)) {
				if (curr.n1 != null) q.add(curr.n1);
				if (curr.n2 != null) q.add(curr.n2);
				if (curr.n3 != null) q.add(curr.n3);
				if (curr.n4 != null) q.add(curr.n4);
				if (curr.word != null && curr.word.length() == maxDepth)
					paths.add(curr.word);
			}
			q.remove();
		}
		return paths;
	}

	void breadthFirstPrint() {
		Queue<Node> q = new LinkedList<>();
		q.add(root);
		while (!q.isEmpty()) {
			Node curr = q.peek();
			if (curr.n1 != null)
				q.add(curr.n1);
			if (curr.n2 != null)
				q.add(curr.n2);
			if (curr.n3 != null)
				q.add(curr.n3);
			if (curr.n4 != null)
				q.add(curr.n4);
			if (curr.data != null)
				System.out.print("|" + q.remove().data + "|");
			else q.remove();
		}
	}

	private class Node {
		Comparable data;
		String word;
		Node n1;
		Node n2;
		Node n3;
		Node n4;

		Node(Character c, String parentWord) {
			data = c;
			word = parentWord == null ? c + "" : parentWord + c;
		}

		Node() {
		}

		Node insertChildren(int i) {
			n1 = new Node(numsToChars[i][0], word);
			n2 = new Node(numsToChars[i][1], word);
			n3 = new Node(numsToChars[i][2], word);
			n4 = new Node(numsToChars[i][3], word);
			if (n4.data == null) n4 = null;
			return this;
		}

		public String toString() {
			return data == null ? "" : data + "";
		}
	}
}
