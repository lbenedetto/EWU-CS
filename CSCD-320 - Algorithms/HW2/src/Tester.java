import java.util.ArrayList;

//Bonus attempted
public class Tester {
	public static void main(String... args) {
		BinarySearchTree tree = new BinarySearchTree();
		int[] array = {8, 3, 10, 1, 6, 14, 4, 7, 13};
		for (int i : array) {
			tree.insert(i);
		}
		tree.printAllPath(new ArrayList<>());
		tree.printAllPathStack();
	}
	//If the current node is null return
	//Add the current node to the arraylist of the path
	//If both the left node and right node are null print out the path
	//else attempt to print all paths of the left child node
	//and then the all paths of the right child node
	//Because of the recursive stack, this does what it is supposed to. Going all the way left,
	//and ending all the way right
}
