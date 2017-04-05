import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

/**
 * Doubly Linked circular generic LinkedList with dummy head node
 * Extra Credit implemented
 *
 * @author lars
 */
public class LinkedList<E extends Comparable<E>> implements Serializable, Cloneable, Iterable<E> {
	private class Node {
		E data;
		Node next;
		Node prev;

		public Node(Node prev, Node next, E data) {
			this.data = data;
			this.next = next;
			this.prev = prev;
			if (next != null)
				next.prev = this;
			if (prev != null)
				prev.next = this;
		}

		public Node() {
			this(null, null, null);
		}

		//For debugging
		public String toString() {
			try {
				return data.toString();
			} catch (NullPointerException e) {
				return "Head Node";
			}
		}
	}

	private Node head;
	private int size;

	public LinkedList() {
		this.head = new Node();
		connect(head, head);
		this.size = 0;
	}

	/**
	 * Custom sorting algorithm based on Random Access
	 * Only possible due to optimization for a specific dataset
	 * Can't hold zeros or negative numbers, but could be expanded to support those at the cost of efficiency
	 * Turns out this is a dumb version of radix sort.
	 */
	public void sieveSortAndTime(Tracker t) {
		double before;
		double after;
		before = System.nanoTime();
		int[] array = new int[16385];
		int[] repArray = new int[16385];
		for (Node curr = head.next; curr != head; curr = curr.next) {
			int cd = ((BigInteger) curr.data).intValue();
			array[cd] = cd;
			repArray[cd]++;
		}
		this.clear();
		for (int i : array)
			if (i != 0)
				for (int rep = 0; rep < repArray[i]; rep++)
					add((E) BigInteger.valueOf(i));
		after = System.nanoTime();
		t.setTime(after - before);
	}

	public void sieveSortAndTrack(Tracker t) {
		int[] array = new int[16385];
		int[] repArray = new int[16385];
		for (Node curr = head.next; curr != head; curr = curr.next) {
			t.loopControlAssignments++;//Curr is assigned or incremented
			t.loopControlComparisons++;//Curr is compared to head
			int cd = ((BigInteger) curr.data).intValue();
			t.other++;//curr.data is casted to int
			array[cd] = cd;
			repArray[cd]++;
			t.loopControlAssignments++;//Value in repArray is incremented
			t.dataAssignments++;//Value in array is assigned
		}
		this.clear();
		t.other++;//The current linked list is cleared
		for (int i : array) {
			t.loopControlAssignments++;//i is assigned to the next value
			if (i != 0) {
				t.loopControlComparisons++;//i is compared to zero
				for (int rep = 0; rep < repArray[i]; rep++)
					add((E) BigInteger.valueOf(i));
				t.dataAssignments += 4;//A new node is added to the list
			}
			t.loopControlComparisons++;//i is compared to zero
		}
	}

	/**
	 * Sorting algorithms
	 */
	public void bubbleSortAndTime(Tracker t) {
		double before;
		double after;
		before = System.nanoTime();
		if (size <= 1) return;
		Node end = head;
		boolean sorted;
		do {
			sorted = true;
			for (Node left = head.next, right = left.next; right != end; left = right, right = right.next) {
				if (left.data.compareTo(right.data) > 0) {
					E temp = left.data;
					left.data = right.data;
					right.data = temp;
					sorted = false;
				}
			}
			end = end.prev;
		} while (!sorted);
		after = System.nanoTime();
		t.setTime(after - before);
	}

	public void bubbleSortAndTrack(Tracker t) {
		t.other++;//Size is compared to 1
		if (size <= 1) return;
		Node end = head;
		t.loopControlAssignments++;//end is assigned to head
		boolean sorted;
		do {
			sorted = true;
			t.loopControlAssignments++;
			t.loopControlAssignments += 2;
			for (Node left = head.next, right = left.next; right != end; left = right, right = right.next) {
				t.loopControlComparisons++;//right == end
				t.loopControlAssignments += 2;//Assignment of left and right
				t.dataComparisons++;//compareTo()
				if (left.data.compareTo(right.data) > 0) {
					//Assigning temp, left, and right
					t.dataAssignments += 3;//Data is assigned
					E temp = left.data;
					left.data = right.data;
					right.data = temp;
					sorted = false;
					t.loopControlAssignments++;//sorted = false
				}
			}
			t.loopControlComparisons++;//right != end
			end = end.prev;

			t.loopControlAssignments++;//end = end.prev;
			t.loopControlComparisons++;//!sorted
		} while (!sorted);
		t.loopControlComparisons++;//!sorted
	}

	public void selectionSortAndTime(Tracker t) {
		double before;
		double after;
		before = System.nanoTime();
		for (Node i = head.next; i != head.prev; i = i.next) {
			Node smallest = i;
			for (Node j = i.next; j != head; j = j.next)
				if (i.data.compareTo(j.data) > 0)
					smallest = j;
			E temp = i.data;
			i.data = smallest.data;
			smallest.data = temp;
		}
		after = System.nanoTime();
		t.setTime(after - before);
	}

	public void selectionSortAndTrack(Tracker t) {
		for (Node i = head.next; i != head.prev; i = i.next) {
			t.loopControlAssignments++;//i is assigned
			t.loopControlComparisons++;//i is compared to head.prev
			Node smallest = i;
			for (Node j = i.next; j != head; j = j.next) {
				t.loopControlAssignments++;//j is assigned
				t.loopControlComparisons++;//j is compared to head
				t.dataComparisons++;//data is compared
				if (i.data.compareTo(j.data) > 0) {
					smallest = j;
					t.dataAssignments++;//data is assigned
				}
			}
			t.loopControlComparisons++;//j is compared to head
			t.dataAssignments += 3;//Assigning temp, left, and right
			E temp = i.data;
			i.data = smallest.data;
			smallest.data = temp;
		}
		t.loopControlComparisons++;//i is compared to head.prev
	}

	public void dataInsertionSortAndTime(Tracker t) {
		double before;
		double after;
		before = System.nanoTime();
		if (size > 1) {
			Node lastSorted, sortedWalker;
			for (lastSorted = head.next; lastSorted != head.prev; lastSorted = lastSorted.next) {
				E firstUnsorted = lastSorted.next.data;
				for (sortedWalker = lastSorted; sortedWalker != head && firstUnsorted.compareTo(sortedWalker.data) < 0; sortedWalker = sortedWalker.prev) {
					sortedWalker.next.data = sortedWalker.data;
				}
				sortedWalker.next.data = firstUnsorted;
			}
		}
		after = System.nanoTime();
		t.setTime(after - before);
	}

	public void dataInsertionSortAndTrack(Tracker t) {
		t.other++;//size is compared to 1
		if (size > 1) {
			Node lastSorted, sortedWalker;
			for (lastSorted = head.next; lastSorted != head.prev; lastSorted = lastSorted.next) {
				t.loopControlAssignments++;//lastSorted is assigned
				t.loopControlComparisons++;//lastSorted is compared to head.prev
				E firstUnsorted = lastSorted.next.data;
				for (sortedWalker = lastSorted; sortedWalker != head && firstUnsorted.compareTo(sortedWalker.data) < 0; sortedWalker = sortedWalker.prev) {
					t.loopControlComparisons += 2;//two comparisons are made
					t.loopControlAssignments++;//sortedWalker is assigned
					t.dataAssignments++;//data is shifted
					sortedWalker.next.data = sortedWalker.data;
				}
				t.loopControlComparisons++;//sortedWalker is compared to head
				t.dataAssignments++;//data is assigned
				sortedWalker.next.data = firstUnsorted;
			}
			t.loopControlComparisons++;//lastSorted is compared to head.prev
		}
	}

	public void nodeInsertionSortAndTime(Tracker t) {
		double before;
		double after;
		before = System.nanoTime();
		if (size > 1) {
			Node lastSorted, curr;
			for (lastSorted = head.next; lastSorted != head.prev; lastSorted = lastSorted.next) {
				Node firstUnsorted = lastSorted.next;
				curr = lastSorted;
				for (; curr != head && curr.data.compareTo(firstUnsorted.data) > 0; ) {
					curr = curr.prev;
				}
				if (curr.next != firstUnsorted) {
					detachNode(firstUnsorted);
					insertNode(curr, firstUnsorted, curr.next);
					lastSorted = lastSorted.prev;
				}
			}
		}
		after = System.nanoTime();
		t.setTime(after - before);
	}

	public void nodeInsertionSortAndTrack(Tracker t) {
		t.other++;//size compared to 1
		if (size > 1) {
			Node lastSorted, curr;
			for (lastSorted = head.next; lastSorted != head.prev; lastSorted = lastSorted.next) {
				t.loopControlAssignments++;//lastSorted is assigned
				t.loopControlComparisons++;//lastSorted is compared
				Node firstUnsorted = lastSorted.next;
				curr = lastSorted;
				for (; curr != head && curr.data.compareTo(firstUnsorted.data) > 0; ) {
					t.loopControlComparisons += 2;//two comparisons are made every loop
					t.loopControlAssignments++;//curr is moved backwards
					curr = curr.prev;
				}
				t.loopControlComparisons++;//A comparison failed to eval to true
				if (curr.next != firstUnsorted) {
					t.loopControlComparisons++;//curr.next is compared
					detachNode(firstUnsorted);
					insertNode(curr, firstUnsorted, curr.next);
					t.dataAssignments += 6;//It takes 6 data assignments to cut and paste a node
					lastSorted = lastSorted.prev;
					t.loopControlAssignments++;//lastSorted is moved back one
				}
			}
			t.loopControlComparisons++;//lastSorted is compared
		}
	}

	private void detachNode(Node middle) {
		middle.prev.next = middle.next;
		middle.next.prev = middle.prev;
	}

	private void insertNode(Node left, Node middle, Node right) {
		left.next = middle;
		middle.next = right;
		right.prev = middle;
		middle.prev = left;
	}

	/**
	 * Connects leftNode to rightNode
	 *
	 * @param leftNode  Node to the left of the deleted node
	 * @param rightNode Node to the right of the deleted node
	 */
	private void connect(Node leftNode, Node rightNode) {
		leftNode.next = rightNode;
		rightNode.prev = leftNode;
	}

	public boolean add(E e) {
		new Node(head.prev, head, e);
		size++;
		return true;
	}

	public boolean addFirst(E e) {
		new Node(head, head.next, e);
		size++;
		return true;
	}

	public int hashCode() {
		int hashCode = 1;
		for (E e : this)
			hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
		return hashCode;
	}

	public int size() {
		return size;
	}

	public void clear() {
		connect(head, head);
		size = 0;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (!(o instanceof List<?>)) return false;
		List<?> that = (List<?>) o;
		return that.size() == size() && (hashCode() == that.hashCode());
	}

	public String toString() {
		String s = "";
		for (E e : this) {
			s += e + "\n";
		}
		return s;
	}

	@SuppressWarnings("CloneDoesntCallSuperClone")
	public LinkedList<E> clone() {
		LinkedList<E> newList = new LinkedList<>();
		for (E e : this)
			newList.add(e);
		return newList;
	}

	@SuppressWarnings("CloneDoesntCallSuperClone")
	public LinkedList<E> cloneReverse() {
		LinkedList<E> newList = new LinkedList<>();
		for (E e : this)
			newList.addFirst(e);
		return newList;
	}

	public boolean isSorted() {
		MyListIterator i = listIterator();
		while (i.hasNext()) {
			if (i.hasPrevious()) i.previous();
			if (i.next().compareTo(i.next()) > 0)
				return false;
		}
		return true;
	}

	//<editor-fold desc="Iterator Stuff">
	public MyListIterator listIterator() {
		return new MyListIterator(head.next);
	}

	public MyListIterator listIterator(int index) {
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException("Index passed to listIterator(index) was out of bounds");
		MyListIterator i;
		int ix = 0;
		for (i = listIterator(); i.hasNext(); i.next()) {
			if (ix == index) break;
			ix++;
		}
		return i;
	}

	public MyIterator iterator() {
		return new MyIterator(head.next);
	}

	class MyListIterator implements ListIterator<E> {
		private Node next;
		private int nextIndex;
		private Node prev;
		private int prevIndex;
		private Node lastReturned;
		private boolean canModify;

		public MyListIterator(Node start) {
			next = start;
			nextIndex = 0;
			prev = start.prev;
			prevIndex = -1;
			lastReturned = null;
			canModify = false;
		}

		@Override
		public boolean hasNext() {
			return next != head;
		}

		@Override
		public E next() {
			if (!hasNext()) throw new NoSuchElementException("Iterator did not have next");
			prev = next;
			next = next.next;
			nextIndex++;
			prevIndex++;
			canModify = true;
			lastReturned = prev;
			return prev.data;
		}

		@Override
		public boolean hasPrevious() {
			return prev != head;
		}

		@Override
		public E previous() {
			if (!hasPrevious()) throw new NoSuchElementException("Iterator did not have previous");
			next = prev;
			prev = prev.prev;
			nextIndex--;
			prevIndex--;
			canModify = true;
			lastReturned = next;
			return next.data;
		}

		@Override
		public int nextIndex() {
			return nextIndex;
		}

		@Override
		public int previousIndex() {
			return prevIndex;
		}

		@Override
		public void remove() {
			if (lastReturned == null || !canModify)
				throw new IllegalStateException("Tried to remove an element from the list at an illegal time");
			connect(lastReturned.prev, lastReturned.next);
			lastReturned = null;
			canModify = false;
			size--;
		}

		@Override
		public void set(E t) {
			if (lastReturned == null | !canModify)
				throw new IllegalStateException("Tried to modify the list at an illegal time");
			lastReturned.data = t;
			canModify = false;
		}

		@Override
		public void add(E t) {
			prev = new Node(prev, next, t);
			nextIndex++;
			prevIndex++;
			canModify = false;
			size++;
		}
	}

	public class MyIterator implements Iterator<E> {
		private Node curr;
		private Node lastReturned;
		private boolean canRemove;

		private MyIterator(Node start) {
			curr = start;
			lastReturned = null;
			canRemove = false;
		}

		@Override
		public boolean hasNext() {
			return curr != head;
		}

		@Override
		public E next() {
			if (!hasNext()) throw new NoSuchElementException("Iterator did not have next");
			lastReturned = curr;
			E data = curr.data;
			curr = curr.next;
			canRemove = true;
			return data;
		}

		@Override
		public void remove() {
			if (!canRemove) throw new IllegalStateException("Tried to remove element from list at illegal time");
			assert lastReturned != null;
			connect(lastReturned.prev, lastReturned.next);
			size--;
			canRemove = false;
		}
	}
// </editor-fold>
}