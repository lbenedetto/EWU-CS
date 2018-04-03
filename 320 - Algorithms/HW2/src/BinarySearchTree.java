import java.util.ArrayList;
import java.util.Stack;

class BinarySearchTree {
	private Node root;
	private int pathCount;

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

	void printAllPath(ArrayList<Comparable> path) {
		pathCount = 0;
		printAllPath(root, path);
	}

	private void printAllPath(Node current, ArrayList<Comparable> path) {
		if (current == null) return;//If the current node is null return
		path.add(current.data);//Add the current node to the arraylist of the path
		if (current.left == null && current.right == null) {//If both the left node and right node are null print out the path
			pathCount++;
			System.out.print("path " + pathCount + ": ");
			int i = 0;
			for (Comparable c : path) {
				System.out.print(c);
				i++;
				if (i < path.size()) {
					System.out.print("->");
				}
			}
			System.out.println();
		} else {
			printAllPath(current.left, new ArrayList<>(path));//Attempt to print all paths of the left child node
			printAllPath(current.right, new ArrayList<>(path));//Attempt to print all paths of the right child node
		}
	}

	void printAllPathStack() {
		pathCount = 0;
		printAllPathStack(root);
	}

	private void printAllPathStack(Node current) {
		Stack<String> stackString = new Stack<>();
		Stack<Node> stackNode = new Stack<>();
		if (current == null) return;
		stackString.push(current.data + "");//Push the current data to the stack as a string
		stackNode.push(current);//Push the current node to the stack to keep track of current position
		while (!stackString.isEmpty()) {
			Node temp = stackNode.pop();//Pop the node off the stack
			String path = stackString.pop();//Pop the stack into the path so we can modify it
			if (temp.right != null) {
				stackString.push(path + "->" + temp.right.data);//Add to the path and put it back in the stack
				stackNode.push(temp.right);//Put the right child node into the stack
			}
			if (temp.left != null) {
				stackString.push(path + "->" + temp.left.data);//Add to the path and put it back in the stack
				stackNode.push(temp.left);//Put the left child node into the stack
			}
			if (temp.left == null && temp.right == null) {//If we have reached max depth
				pathCount++;
				System.out.print("path " + pathCount + ": ");
				System.out.println(path);//Print out the path so far.
			}
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
