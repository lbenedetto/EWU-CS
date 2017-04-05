public class LinkedList1 extends LinkedList0 {
    /* Return the object at the indicated index.  If the index is invalid,
       print an error message and return null */
    public Comparable get(int index) {// Your code goes here --- stub treats everything as invalid
        if (index < 0 || index + 1 > size()) {
            System.out.println(index + " is invalid for a size " + size + " list");
            return null;
        }
        Node curr = header.next;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr.getData();
    }
}