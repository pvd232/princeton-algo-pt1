package queue;

import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int head, tail;
    private int capacity;

    // construct an empty randomized queue
    public RandomizedQueue() {
        capacity = 10;
        q = (Item[]) new Object[capacity];
        tail = 0;
        head = 0;
    }

    private void resize(int newCapacity) {
        Item[] copy = (Item[]) new Object[newCapacity];
        int j = 0;
        for (int i = tail; i < head; i++)
            copy[j++] = q[i];

        q = copy;
        head = head - tail;
        tail = 0;
        capacity = newCapacity;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return head == tail;
    }

    // return the number of items on the randomized queue
    public int size() {
        return head - tail;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (head == q.length)
            resize(2 * q.length);
        q[head++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        if (head > 0 && head - tail <= q.length / 4)
            resize(q.length / 2);

        // get random number in bounds of tail and head
        int intRdm = StdRandom.uniformInt(tail, head);

        // get random item
        Item item = q[intRdm];

        // swap random item with tail
        q[intRdm] = q[tail++];

        // no loitering
        q[tail - 1] = null;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();
        int intRdm = StdRandom.uniformInt(tail, head);
        return q[intRdm];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private int k = 0;
        private Item[] qCopy;
        private int sizeCopy;

        private ArrayIterator() {
            qCopy = (Item[]) new Object[size()];
            int j = 0;
            for (int i = tail; i < head; i++)
                qCopy[j++] = q[i];
            // shuffle queue
            StdRandom.shuffle(qCopy, 0, j);
            sizeCopy = j;
        }

        public boolean hasNext() {
            return k < sizeCopy;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return qCopy[k++];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(1);
        rq.enqueue(3);
        rq.enqueue(6);
        rq.enqueue(8);
        rq.enqueue(9);
        int test = rq.dequeue();
        System.out.println("test " + test);

        assert rq.size() == 2;
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();

        for (int j = 0; j < 50; j++)
            queue.enqueue(j);
        for (int j = 0; j < 40; j++)
            queue.dequeue();
        Iterator<Integer> it2 = queue.iterator();
        Iterator<Integer> it3 = queue.iterator();
        while (it2.hasNext())
            System.out.println(it2.next());

        System.out.println("second iterator");
        while (it3.hasNext())
            System.out.println(it3.next());
    }
}