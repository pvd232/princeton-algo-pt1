package quickunion;

import edu.princeton.cs.algs4.StdIn;

public class QuickUnionConnected {
    private int[] id;
    private int[] sz;
    private int numRoots;

    public QuickUnionConnected(int N) {
        id = new int[N];
        sz = new int[N];
        numRoots = N;
        for (int i = 0; i < N; i++) {
            id[i] = i;
            sz[i] = 1;
        }

    }

    private int root(int i) {
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

    public boolean singleComponent() {
        return numRoots == 1;
    }

    public void union(int p, int q) {
        int i = root(p);
        int j = root(q);
        if (i == j)
            return;
        // make smaller root (by number of nodes, not tree height) point to larger one
        if (sz[i] < sz[j]) {
            id[i] = j;
            sz[j] += sz[i];
        } else {
            id[j] = i;
            sz[i] += sz[j];
        }
        numRoots--;
    }

    public float tsMembersConnected(int m, int n) {
        QuickUnionConnected qu = new QuickUnionConnected(n);
        int timestamp = 0;
        while (!StdIn.isEmpty() && !qu.singleComponent()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (!qu.connected(p, q)) {
                qu.union(p, q);
            }
            timestamp = StdIn.readInt();
        }
        return timestamp;
    }

    public static void main(String[] args) {
        int m = StdIn.readInt();
        int n = StdIn.readInt();
        QuickUnionConnected qu = new QuickUnionConnected(n);

        float earliestTime = qu.tsMembersConnected(m, n);
        System.out.println("Earliest time at which all members of a group are connected: " + earliestTime);
    }
}
