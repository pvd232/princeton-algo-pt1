package sorting;

public class MergeSortAux {
    private static void merge(Comparable<Object>[] a, Comparable<Object>[] aux, int lo, int mid, int hi) {
        // assign pointers to the beginning of each half of the array to be merged
        // lo(i) is the start of left half
        // mid is the end of the left half

        // mid + 1(j) is the start right half
        // hi is the end of the right half
        int i = lo, j = mid + 1;

        // copy the array to be merged into aux
        for (int k = lo; k <= hi; k++)
            aux[k] = a[k];
        // iterate through the array to be merged a[k] and assign the smaller of the two
        // halves to a[k]
        for (int k = lo; k <= hi; k++) {
            if (i > mid) // if the left half is exhausted, assign the right half to a[k]
                a[k] = aux[j++];
            else if (j > hi) // if the right half is exhausted, assign the left half to a[k]
                a[k] = aux[i++];
            else if (aux[j].compareTo(aux[i]) < 0) // if the right half is smaller, assign the right half to a[k] and
                                                   // increment the right
                                                   // half pointer
                a[k] = aux[j++];
            else // if the left half is smaller, assign the left half to a[k] and
                 // increment the left
                 // half pointer
                a[k] = aux[i++];
        }
    }

    private static void sort(Comparable<Object>[] a, Comparable<Object>[] aux, int lo, int hi) {
        // if the array is len 1, it is sorted
        if (hi <= lo)
            return;

        // find the middle of the array
        int mid = lo + (hi - lo) / 2;

        // sort the left half
        sort(a, aux, lo, mid);

        // sort the right half
        sort(a, aux, mid + 1, hi);

        // merge the two halves
        // a and aux have been swapped for all recursive sort calls
        // so aux is being sorted in place for N - 1 calls and a is kept the same
        // then for the final call to merge, the final halves of the sorted aux are used
        // to sort a in place
        merge(a, aux, lo, mid, hi);
    }

    public static void sort(Comparable<Object>[] a) {
        Comparable<Object>[] aux = a.clone();
        // the proper a and aux are passed in for the first call to sort
        sort(a, aux, 0, a.length - 1);
    }

    public static void main(String[] args) {
        Comparable<Object>[] a = new Comparable[] { "M", "E", "R", "G", "E" };

        MergeSortAux.sort(a);
        for (int i = 0; i < a.length; i++)
            System.out.println(a[i]);
    }
}