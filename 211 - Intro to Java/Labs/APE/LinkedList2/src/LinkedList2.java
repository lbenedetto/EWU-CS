public class LinkedList2 extends LinkedList1 {
    /* Add the element at end of the list.  */
    public void addLast(Comparable element) {
        Node curr = header;
        while(curr.next != null)
            curr=curr.next;
        curr.next = new Node(element);
        size++;
    }
}