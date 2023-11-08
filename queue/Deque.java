package queue;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int size;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        size++;
        if (oldFirst != null) {
            oldFirst.prev = first;
        }
        if (last == null) {
            last = first;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        size++;
        if (oldLast != null) {
            oldLast.next = last;
            last.prev = oldLast;
        }
        if (first == null) {
            first = last;
        }

    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (first == null)
            throw new NoSuchElementException();
        Item item = first.item;
        first = first.next;
        size--;
        if (isEmpty())
            last = null;
        else
            first.prev = null;

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (last == null)
            throw new NoSuchElementException();
        Item item = last.item;
        last = last.prev;
        size--;
        if (last == null)
            first = null;
        else
            last.next = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(0);
        deque.addLast(1);
        deque.removeFirst();
        assert deque.size() == 1;
        deque.removeFirst();
        assert deque.size() == 0;
        deque.addFirst(1);
        deque.addLast(2);
        deque.addFirst(3);
        deque.addLast(4);
        deque.addFirst(5);
        deque.addFirst(6);
        assert deque.size() == 6;
        assert deque.removeFirst() == 6;
        assert deque.removeLast() == 4;
        assert deque.size == 4;
        assert !deque.isEmpty();
        Iterator<Integer> iterator = deque.iterator();
        assert iterator.hasNext();

        Deque<Integer> deque2 = new Deque<Integer>();
        deque2.addFirst(1);
        deque2.addFirst(2);
        deque2.addFirst(3);
        deque2.addFirst(4);
        Iterator<Integer> it2 = deque2.iterator();
        while (it2.hasNext()) {
            System.out.println(it2.next());
        }
    }
}