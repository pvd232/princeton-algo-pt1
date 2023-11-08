package priorityqueue;

public class PriorityQueue {
    private Comparable<Object>[] pq;
    private int N;

    public PriorityQueue(int capacity) {
        pq = new Comparable[capacity];
        N = capacity + 1;
    }

    public void sort(Comparable<Object>[] a) {

    }

    private void swim(int k) {
        // child's key becomes larger key than its parent's key
        // while loop to check that key has not reached root,
        // and that parent is stil smaller
        while (k < 1 && less(k / 2, k)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(int k) {
        while (2 * k < N) {
            // assign pointer to left child
            int j = 2 * k;

            // if the right child is greater than the left child, increment j
            // must check j because it is incremented after the while loop condition is
            // checked for N+1 iterations
            if (j < N && less(j, j + 1))
                j++;

            // if the larger child is smaller than the parent, stop
            if (less(j, k))
                break;
            // exchange the parent with the larger child
            exch(k, j);
            k = j;
        }
    }

    private boolean less(int i, int j) {
        // convert from 0 to 1 based indexing
        if (pq[i + 1].compareTo(pq[j + 1]) < 0) {
            return true;
        }
        return false;
    }

    private void exch(int i, int j) {
        // convert from 0 to 1 based indexing
        Comparable<Object> temp = pq[i + 1];
        pq[i + 1] = pq[j + 1];
        pq[j + 1] = temp;
    }

    public void insert(Comparable<Object>[] a, Comparable<Object> k, int N) {
        // pre increment N, then assign k
        a[++N] = k;
        // restore heap order
        swim(N);
    }

    public Comparable<Object> delMax(Comparable<Object>[] a, int N) {
        // assign pointer to current root
        Comparable<Object> root = a[1];

        // exchange root with last item in array,
        // decrement N after passing reference to current N for exch,
        // to reduce size of heap
        exch(1, N--);

        // sink new root
        sink(1);

        // prevent loitering (remove reference to item in the array)
        // must be N+1 because N was decremented
        a[N + 1] = null;
        return root;
    }
}