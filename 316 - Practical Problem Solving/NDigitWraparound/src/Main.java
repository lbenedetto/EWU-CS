import java.util.ArrayList;
import java.util.Scanner;


public class Main {
	private static LinkedList.Node curr;
	private static LinkedList<Integer> ints;

	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		String input;
		int case_ = 1;
		while (kb.hasNextLine()) {
			input = kb.nextLine();
			if (input.startsWith("0")) System.exit(0);
			readInts(input);
			while (hasRepeats() || !isRunaround()) {
				increment();
			}
			System.out.println("Case " + case_ + ": " + ints);
			case_++;
		}
		System.out.println("End");
	}

	private static boolean hasRepeats() {
		boolean[] found = new boolean[10];
		for (Integer i : ints) {
			if (found[i]) return true;
			found[i] = true;
		}
		return false;
	}

	private static boolean isRunaround() {
		curr = ints.head.next;
		LinkedList.Node start = ints.head.next;
		ArrayList<LinkedList.Node> visited = new ArrayList<>();
		while (!visited.contains(curr)) {
			visited.add(curr);
			Integer j = (Integer) curr.data;
			for (int i = 0; i < j; i++) {
				curr = curr.next;
				if (curr == ints.head) curr = curr.next;
			}
		}
		LinkedList.Node temp = curr;
		while (temp != ints.head) {
			if (!visited.contains(temp)) {
				return false;
			}
			temp = temp.next;
		}
		return curr == start;
	}

	private static void increment() {
		String temp = ints.toString();
		Integer i = Integer.parseInt(temp) + 1;
		readInts(i.toString());
	}

	private static LinkedList<Integer> readInts(String s) {
		String[] temp = s.split("");
		ints = new LinkedList<>();
		for (String st : temp) {
			ints.add(Integer.parseInt(st));
		}
		return ints;
	}
}
