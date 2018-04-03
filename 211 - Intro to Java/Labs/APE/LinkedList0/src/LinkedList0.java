public class LinkedList0 {
    /* Nested class, so that LinkedList0 has access to ALL
     * fields within Node
     */
    static protected class Node {
        Comparable data;
        Node next;

        Node(Comparable item) {
            this(item, null);
        }

        Node(Comparable item, Node nxt) {
            data = item;
            next = nxt;
        }
    }

    Node head;
    int size;

    /* Constructor builds the dummy head node */
    public LinkedList0() {
        head = new Node(null);
        head.next = null;
        size = 0;
    }

    /* Return the number of entries in the linked list */
    public int size() {
        return size;
    }

    /* Return true if the list is empty; else return false */
    public boolean isEmpty() {
        return size == 0;
    }

    /* Turn the linked list into an empty linked list */
    public void clear() {
        head.next = null;
        size = 0;
    }

    /* Add the indicated object to the front of the linked list */
    public void addFirst(Comparable item) {
        Node newNode = new Node(item);
        newNode.next = head.next;
        head.next = newNode;
        size++;
    }

    /* List the contents of the list from front to back.  If the
       list is empty, print a message to that effect.
     */
    public void listForward() {
        if (isEmpty()) {
            System.out.println("Empty list");
            return;
        }
        for (Node curr = head.next; curr != null; curr = curr.next) {
            System.out.println(curr.data.toString());
        }
    }
}