package sorting;

public class MergeSortHalves {
    private static void merge(Comparable<Object>[] a, int lo, int mid, int hi) {
        // initialize both array halves to be merged
        int n1Len = mid - lo + 1;
        int n2Len = hi - mid;
        Comparable<Object>[] auxLeft = new Comparable[n1Len];
        Comparable<Object>[] auxRight = new Comparable[n2Len];

        // clone left aux from original array a
        for (int i = 0; i < n1Len; i++) {
            auxLeft[i] = a[lo + i];
        }
        for (int j = 0; j < n2Len; j++) {
            auxRight[j] = a[mid + 1 + j];
        }

        // iterate through the array to be merged a[k] and assign the smaller of the two
        // halves to a[k]
        int i = 0, j = 0;
        for (int k = lo; k <= hi; k++) {
            if (i >= n1Len) { // if the left array is exhausted, clone the value from the right array and
                              // increment the right array
                a[k] = auxRight[j++];
            } else if (j >= n2Len) { // if the right array is exhausted, clone the value from the left array and
                                     // increment the left array
                a[k] = auxLeft[i++];
            } else if (auxLeft[i].compareTo(auxRight[j]) < 0) { // if the left value is smaller than the right value,
                                                                // clone the left value and increment the left value
                a[k] = auxLeft[i++];
            } else {
                a[k] = auxRight[j++];
            }
        }
    }

    public static void sort(Comparable<Object>[] a, int lo, int hi) {
        // if the array is len 1, it is sorted
        if (hi <= lo)
            return;

        // find the middle of the array
        int mid = lo + (hi - lo) / 2;

        // sort the left half
        sort(a, lo, mid);

        // sort the right half
        sort(a, mid + 1, hi);

        // merge the two halves
        // a and aux have been swapped for all recursive sort calls
        // so aux is being sorted in place for N - 1 calls and a is kept the same
        // then for the final call to merge, the final halves of the sorted aux are used
        // to sort a in place
        merge(a, lo, mid, hi);
    }

    public static void main(String[] args) {
        Comparable<Object>[] a = new Comparable[] { "M", "E", "R", "G", "E" };

        MergeSortHalves.sort(a, 0, a.length - 1);
        for (int i = 0; i < a.length; i++)
            System.out.println(a[i]);
    }
}