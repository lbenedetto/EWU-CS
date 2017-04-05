import java.util.Arrays;

public class Tester {
	public static void main(String[] args) {
		BinarySearchTree BST = new BinarySearchTree();
		Arrays.stream(new int[] {8,10,14,13,3,1,6,4,7}).forEach(BST::insert);
		BST.breadthFirstPrint();
	}
}
