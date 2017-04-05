public class LinkedList3 extends LinkedList2 {
    /* Add the element into its proper location within list.  */
    public void addOrdered(Comparable element) {
        Node curr = header.next;
        Node newNode = new Node(element);
        Node prevNode = header;
        while (curr != null) {
            if (newNode.data.compareTo(curr.data) <= 0) {
                newNode.next = curr;
                prevNode.next = newNode;
                return;
            }
            prevNode = curr;
            curr = curr.next;
            if (curr == null) {
                prevNode.next = newNode;
                return;
            }
        }
        header.next = newNode;

    }
}