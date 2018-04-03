public class LinkedList4 extends LinkedList3 {
    /*
     * Remove the first occurrence of the Object element from the list
     * and return boolean true.  If there is no such Node, return false.
     */
    public boolean remove(Comparable element) {
        Node prev = header;
        for (Node curr = header.next; curr != null; curr = curr.next) {
            if (curr.data.compareTo(element) == 0) {
                prev.next = curr.next;
                size--;
                return true;
            }
            prev = curr;
        }
        return false;
    }
}