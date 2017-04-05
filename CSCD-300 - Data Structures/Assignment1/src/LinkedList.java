import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Doubly Linked circular generic LinkedList with dummy head node
 * Extra Credit implemented
 *
 * @author lars
 */
public class LinkedList<E> implements List<E>, Serializable, Cloneable, Iterable<E> {
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
            return data.toString();
        }
    }

    private final Node head;
    private int size;

    public LinkedList() {
        this.head = new Node();
        connect(head, head);
        this.size = 0;
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

    public void add(int index, E data) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index passed to add(index,data) was out of bounds");
        Node node = findNode(index);
        new Node(node.prev, node, data);
        size++;
    }

    /**
     * Returns a reference to the Node at the given index
     *
     * @param index index of Node
     * @return Node
     */
    private Node findNode(int index) {
        Node curr;
        int i;
        //If the target index is closer to the end of the list
        //Then it is more efficient to go backwards from head rather than forwards
        if (index < (size / 2))
            for (i = 0, curr = this.head.next; i < index; i++)
                curr = curr.next;
        else
            for (i = size, curr = this.head; i > index; i--)
                curr = curr.prev;
        return curr;
    }

    public boolean addAll(Collection<? extends E> c) {
        if (c == null) throw new NullPointerException("Collection passed to addAll(Collection) was null");
        boolean modified = false;
        for (E e : c)
            if (add(e))
                modified = true;
        return modified;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        if (c == null) throw new NullPointerException("Collection passed to addAll(Collection) was null");
        if (index != 0 && (index < 0 || index > size))
            throw new IndexOutOfBoundsException("Index passed to addAll(Collection) was out of bounds");
        if (c.isEmpty()) return false;
        MyListIterator i = listIterator(index);
        c.forEach(i::add);
        return true;
    }

    public void clear() {
        connect(head, head);
        size = 0;
    }

    public boolean contains(Object o) {
        for (E e : this)
            if (e == o || e.equals(o))
                return true;
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        if (c == null) throw new NullPointerException("Collection passed to containsAll(Collection) was null");
        for (Object o : c)
            if (!contains(o)) {
                return false;
            }
        return true;
    }

    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index passed to get(index) was out of bounds");
        //Used to use iterator, but findNode is more efficient
        return findNode(index).data;
    }

    public int hashCode() {
        int hashCode = 1;
        for (E e : this)
            hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
        return hashCode;
    }

    public int indexOf(Object o) {
        MyListIterator i = listIterator();
        E e;
        while (i.hasNext()) {
            e = i.next();
            if (e == o || e.equals(o)) {
                return i.prevIndex;
            }
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        MyListIterator i = listIterator();
        int ix = -1;
        E e;
        while (i.hasNext()) {
            e = i.next();
            if (e == o || e.equals(o)) {
                ix = i.prevIndex;
            }
        }
        return ix;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index passed to remove(index) was out of bounds");
        MyListIterator i = listIterator(index);
        E e = i.next();
        i.remove();
        return e;
    }

    public boolean remove(Object o) {
        MyIterator i = iterator();
        while (i.hasNext()) {
            E data = i.next();
            if (data == o || data.equals(o)) {
                i.remove();
                return true;
            }
        }
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        if (c == null) throw new NullPointerException("Collection passed to removeAll(Collection) was null");
        boolean changed = false;
        for (Object o : c) {
            if (remove(o)) changed = true;
        }
        return changed;
    }

    public boolean retainAll(Collection c) {
        if (c == null) throw new NullPointerException("Collection passed to retainAll(Collection) was null");
        boolean changed = false;
        MyListIterator i = listIterator();
        while (i.hasNext())
            if (!c.contains(i.next())) {
                i.remove();
                changed = true;
            }
        return changed;
    }

    public E set(int index, E e) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index passed to set(index, E) was out of bounds");
        //Null E is ok
        MyListIterator i = listIterator(index);
        E ret = i.next();
        i.set(e);
        return ret;
    }

    public int size() {
        return size;
    }

    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex)
            throw new IndexOutOfBoundsException("Index passed to subList(fromIndex, toIndex) was out of bounds");
        MyListIterator i = listIterator(fromIndex);
        LinkedList<E> newList = new LinkedList<>();
        while (i.hasNext() && i.nextIndex < toIndex)
            newList.add(i.next());
        return newList;
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
        return this.stream().collect(Collectors.toCollection(LinkedList::new));
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException("method not implemented");
    }

    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("method not implemented");
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