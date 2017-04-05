
public class LinkedList {
    private Node head;
    private Node tail;
    private static int counter;

    public void add(Integer data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = new Node();
            head.next = newNode;
            newNode.prev = head;
            head.prev = null;
        }
        if (tail == null) {
            tail = new Node();
            tail.prev = newNode;
            newNode.next = tail;
            tail.next = null;
        }
        Node currentNode = tail.prev;
        if (currentNode != null && currentNode != newNode) {
            currentNode.next = newNode;
            newNode.prev = currentNode;
            tail.prev = newNode;
        }
        incrementCounter();
    }

    public boolean remove(Integer i) {
        if(head==null) return false;
        Node currentNode = head.next;
        if (currentNode != null) {
            while (currentNode != tail && currentNode != null && currentNode != head) {
                if (currentNode.data.equals(i)) {
                    if (currentNode.equals(tail)) {
                        currentNode.prev.next = tail;
                        tail.prev = currentNode.prev;
                    } else {
                        currentNode.prev.next = currentNode.next;
                        currentNode.next.prev = currentNode.prev;
                    }
                    decrementCounter();
                    return true;
                }
                currentNode = currentNode.next;
            }
        }
        return false;
    }

    public void sort() {
        if (size() == 0) return;
        Node min;
        for (Node ix = head.next; ix != null; ix = ix.next) {
            min = ix;
            //Finds smallest value after ix
            for (Node tx = ix.next; tx != null; tx = tx.next) {
                if (tx.compareTo(min) < 0) {
                    min = tx;
                }
            }
            //And swaps it with ix
            if (min != ix) {
                swapNodes(ix, min);
            }
        }
    }

    private void swapNodes(Node currentNode, Node nextNode) {
        Integer temp = currentNode.data;
        currentNode.data = nextNode.data;
        nextNode.data = temp;
    }

    public String toString(int n) {
        String output = "";
        int skipped = 0;
        if (head != null && tail != null) {
            Node currentNode = head.next;
            while (currentNode != tail && currentNode != null) {
                skipped += 1;
                if (skipped == n) {
                    output += "[" + currentNode.data.toString() + "]\n";
                    skipped = 0;
                }
                currentNode = currentNode.next;
            }
        }
        return output;
    }

    public String toStringReverse(int n) {
        String output = "";
        int skipped = 0;
        if (tail != null) {
            Node currentNode = tail;
            while (currentNode.hasPrev()) {
                currentNode = currentNode.prev;
                if (currentNode.equals(head)) break;
                skipped += 1;
                if (skipped == n) {
                    output += "[" + currentNode.data.toString() + "]\n";
                    skipped = 0;
                }
            }
        }
        return output;
    }

    public LinkedList evenNumbersOnly() {
        LinkedList evensOnly = new LinkedList();
        if(head == null || head.next == null) return this;
        Node currentNode = head.next;
        int max = size();
        for (int i = 0; i < max; i++) {
            if (currentNode != tail && currentNode != null) {
                if (currentNode.data % 2 == 0) {
                    evensOnly.add(currentNode.data);
                }
                currentNode = currentNode.next;
            } else break;
        }
        return evensOnly;
    }

    private static int getCounter() {
        return counter;
    }

    private static void incrementCounter() {
        counter++;
    }

    private void decrementCounter() {
        counter--;
    }

    private int size() {
        return getCounter();
    }

    class Node {
        Node next;
        Node prev;
        Integer data;

        Node(Integer dataValue) {
            next = null;
            prev = null;
            data = dataValue;
        }
        Node() {
            next = null;
            prev = null;
            data = null;
        }

        boolean hasPrev() {
            return (prev != null);
        }

        int compareTo(Node that) {
            return this.data - that.data;
        }
    }
}
