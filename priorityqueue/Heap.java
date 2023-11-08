package priorityqueue;

public class Heap {
    public static void sort(Comparable<Object>[] a) {
        // not modifying array, don't need parametarized N
        int N = a.length;

        // first pass, build heap from array
        for (int k = N / 2; k >= 1; k--) {
            // swap parent with child using sink
            sink(a, k, N);
        }

        // second pass, sort array
        while (N > 1) {
            exch(a, 1, N--);
            sink(a, 1, N);
        }
    }

    private static void swim(Comparable<Object>[] a, int k) {
        // child's key becomes larger key than its parent's key
        // while loop to check that key has not reached root,
        // and that parent is stil smaller
        while (k < 1 && less(a, k / 2, k)) {
            exch(a, k, k / 2);
            k = k / 2;
        }
    }

    private static void sink(Comparable<Object>[] a, int k, int N) {
        while (2 * k < N) {
            // assign pointer to left child
            int j = 2 * k;

            // if the right child is greater than the left child, increment j
            // must check j because it is incremented after the while loop condition is
            // checked for N+1 iterations
            if (j < N && less(a, j, j + 1))
                j++;

            // if the larger child is smaller than the parent, stop
            if (less(a, j, k))
                break;
            // exchange the parent with the larger child
            exch(a, k, j);
            k = j;
        }
    }

    private static boolean less(Comparable<Object>[] a, int i, int j) {
        // convert from 0 to 1 based indexing
        if (a[i + 1].compareTo(a[j + 1]) < 0) {
            return true;
        }
        return false;
    }

    private static void exch(Comparable<Object>[] a, int i, int j) {
        // convert from 0 to 1 based indexing
        Comparable<Object> temp = a[i + 1];
        a[i + 1] = a[j + 1];
        a[j + 1] = temp;
    }

    public static void insert(Comparable<Object>[] a, Comparable<Object> k, int N) {
        // pre increment N, then assign k
        a[++N] = k;
        // restore heap order
        swim(a, N);
    }

    public static Comparable<Object> delMax(Comparable<Object>[] a, int N) {
        // assign pointer to current root
        Comparable<Object> root = a[1];

        // exchange root with last item in array,
        // decrement N after passing reference to current N for exch,
        // to reduce size of heap
        exch(a, 1, N--);

        // sink new root
        sink(a, 1, N);

        // prevent loitering (remove reference to item in the array)
        // must be N+1 because N was decremented
        a[N + 1] = null;
        return root;
    }
}