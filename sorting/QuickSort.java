package sorting;

import edu.princeton.cs.algs4.StdRandom;

public class QuickSort {
    private static int partition(Comparable<Object>[] a, int lo, int hi) {
        int j = lo;

        for (int i = lo; i < hi; i++) {
            if (less(a[i], a[hi])) {
                swap(a, i, j++);
            }
        }

        swap(a, hi, j);
        return j;
    }

    private static boolean less(Comparable<Object> a, Comparable<Object> b) {
        if (a.compareTo(b) < 0)
            return false;
        else
            return true;
    }

    private static void swap(Comparable<Object>[] a, int i, int j) {
        Comparable<Object> tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;

    }

    private static void sort(Comparable<Object>[] a, int lo, int hi) {
        // if the array is len 1, it is sorted
        if (hi <= lo)
            return;
        // get partition
        int p = partition(a, lo, hi);
        // sort left side of partition
        sort(a, lo, p - 1);
        // sort right side of partition
        sort(a, p + 1, hi);
    }

    public static Comparable<Object> select(Comparable<Object>[] a, int k) {
        StdRandom.shuffle(a);
        int lo = 0, hi = a.length - 1;
        while (hi > lo) {
            int j = partition(a, lo, hi);
            System.out.println("j " + j);
            if (j < k)
                lo = j + 1;
            else if (j > k)
                hi = j - 1;
            else
                return a[k];
        }
        return a[k];
    }

    public static void sort(Comparable<Object>[] a) {
        sort(a, 0, a.length - 1);
    }

    public static void main(String[] args) {
        // test the program
        Comparable<Object>[] a = new Comparable[] { 1, 3, 2, 5, 4, 7, 6, 9, 8 };
        StdRandom.shuffle(a);
        Comparable<Object> soln = QuickSort.select(a, 2);
        System.out.println(soln);
    }
}
