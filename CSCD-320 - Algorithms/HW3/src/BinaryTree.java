public class BinaryTree {
	private Node root;
	private int pathCount;
	private int postIx;

	public BinaryTree() {

	}

	private BinaryTree(Node root) {
		this.root = root;
	}

	public BinaryTree buildTree(Object inOrderSequence[], Object postOrderSequence[]) {
		int size = postOrderSequence.length;
		postIx = size - 1;
		root = buildTree(inOrderSequence, postOrderSequence, 0, size - 1);
		return this;
	}

	private Node buildTree(Object inOrder[], Object postOrder[], int startIx, int endIx) {
		if (startIx > endIx)
			return null;
		Node node = new Node(postOrder[postIx]);
		postIx--;
		if (startIx == endIx)
			return node;
		int iIndex = indexOfInRange(inOrder, startIx, endIx, node.data);
		node.right = buildTree(inOrder, postOrder, iIndex + 1, endIx);
		node.left = buildTree(inOrder, postOrder, startIx, iIndex - 1);
		return node;
	}

	private int indexOfInRange(Object a[], int start, int end, Object o) {
		int i;
		for (i = start; i <= end; i++) {
			if (a[i].equals(o))
				break;
		}
		return i;
	}

	void printPreOrder() {
		printPreOrder(root);
		System.out.println();
	}

	void printInOrder() {
		printInOrder(root);
		System.out.println();
	}
	void printPostOrder() {
		printPostOrder(root);
		System.out.println();
	}

	private void printPreOrder(Node current) {
		if(current == null) return;
		System.out.print("|" + current.data.toString() + "|");
		printPreOrder(current.left);
		printPreOrder(current.right);
	}

	private void printInOrder(Node current) {
		if(current == null) return;
		printInOrder(current.left);
		System.out.print("|" + current.data.toString() + "|");
		printInOrder(current.right);
	}
	private void printPostOrder(Node current) {
		if(current == null) return;
		printPostOrder(current.left);
		printPostOrder(current.right);
		System.out.print("|" + current.data.toString() + "|");
	}

	private class Node {
		Object data;
		Node left;
		Node right;

		Node(Object d) {
			data = d;
			left = null;
			right = null;
		}

		public String toString() {
			String out = "";
			if (left != null) out += left.data.toString() + "<-";
			out += data.toString();
			if (right != null) out += "->" + right.data.toString();
			return out;
		}
	}
}