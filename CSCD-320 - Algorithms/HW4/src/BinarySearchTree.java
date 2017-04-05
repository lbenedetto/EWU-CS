import java.util.LinkedList;
import java.util.Queue;

class BinarySearchTree {
	private Node root;

	void insert(Comparable data) {
		root = insert(data, root);
	}

	private Node insert(Comparable data, Node current) {
		if (current == null)
			current = new Node(data);
		else if (data.compareTo(current.data) < 0)
			current.left = insert(data, current.left);
		else
			current.right = insert(data, current.right);
		return current;
	}

	void breadthFirstPrint() {
		Queue<Node> q = new LinkedList<>();
		q.add(root);
		while (!q.isEmpty()) {
			Node curr = q.peek();
			if (curr.left != null)
				q.add(curr.left);
			if (curr.right != null)
				q.add(curr.right);
			System.out.print("|" + q.remove().data + "|");
		}
	}

	private class Node {
		Comparable data;
		Node left;
		Node right;

		Node(Comparable d) {
			data = d;
			left = null;
			right = null;
		}
	}
}
