public class LinkedList5 extends LinkedList4 {
    /*
     * Either report the list to be empty, or use a recursive
     * method to print it backwards.
     */
    public void listReverse() {
        if (!isEmpty()) {
            System.out.println(recursivePrint(header.next));
            return;
        }
        System.out.println("Empty list");

    }

    public String recursivePrint(Node curr) {
        if (curr.next == null) {
            return curr.data.toString();
        } else {
            System.out.println(recursivePrint(curr.next));
            return curr.data.toString();
        }
    }
}