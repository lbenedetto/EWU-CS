/**
 * Stack implementation with linked list for primitive type char
 *
 * @author Lars Benedetto
 * @see java.util.Stack JavaDocs
 */
class CharStack {
	private Node top;

	public CharStack() {
		top = null;
	}

	class Node {
		final char c;
		final Node prev;

		public Node(char c) {
			this.c = c;
			prev = top;
			top = this;
		}
	}

	/**
	 * Put char on top of stack
	 *
	 * @param c char
	 */
	public void push(char c) {
		new Node(c);
	}

	/**
	 * Check top of stack
	 *
	 * @return char - top of stack
	 */
	public char peek() {
		return top.c;
	}

	/**
	 * Remove the top of the stack
	 *
	 * @return char - top of stack
	 */
	public char pop() {
		char out = top.c;
		top = top.prev;
		return out;
	}

	/**
	 * Check if the list is not empty
	 *
	 * @return boolean
	 */
	public boolean isNotEmpty() {
		return top != null;
	}
}
