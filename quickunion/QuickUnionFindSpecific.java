package quickunion;

import edu.princeton.cs.algs4.StdIn;

public class QuickUnionFindSpecific {
    private int[] id;
    private int[] sz;
    private int[] max;

    public QuickUnionFindSpecific(int N) {
        id = new int[N];
        sz = new int[N];
        max = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
            max[i] = i;
            sz[i] = 1;
        }
    }

    private int root(int i) {
        if (i >= id.length)
            return -1;
        // chase parent pointers until reach root
        while (i != id[i]) {
            id[i] = id[id[i]]; // path compression, make every other node in path point to its grandparent
            i = id[i];
        }
        return i;
    }

    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    public void union(int p, int q) {
        int i = root(p);
        int j = root(q);
        if (i == j || i == -1 || j == -1)
            return;
        if (sz[i] <= sz[j]) {
            id[i] = j;
            sz[j] += sz[i];
            if (max[i] > max[j]) {
                max[j] = max[i];
            }
        } else {
            id[j] = i;
            sz[i] += sz[j];
            if (max[j] > max[i]) {
                max[i] = max[j];
            }
        }
    }

    public int find(int p) {
        return max[p];
    }

    public static void main(String[] args) {

        int n = StdIn.readInt();
        QuickUnionFindSpecific qu = new QuickUnionFindSpecific(n);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            qu.union(p, q);
        }
        int specificElement = Integer.parseInt(args[0]);
        int maxValue = qu.find(specificElement);
        System.out.println("Max value: " + maxValue);
    }
}
