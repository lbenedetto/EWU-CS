public class Tester {
	public static void main(String... args) {
		Object[] inOrderSequence = {9, 5, 1, 7, 2, 12, 8, 4, 3, 11};
		Object[] postOrderSequence = {9, 1, 2, 12, 7, 5, 3, 11, 4, 8};
		//Object[] inOrderSequence = {'A','B','C','D','E','F','G','H','I'};
		//Object[] postOrderSequence = {'A','C','E','D','B','H','I','G','F'};
		BinaryTree tree = new BinaryTree().buildTree(inOrderSequence, postOrderSequence);
		tree.printInOrder();
		tree.printPostOrder();
	}
}
