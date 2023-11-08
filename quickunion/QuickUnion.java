package quickunion;

public class QuickUnion {
    private int[] id;
    private int[] sz;

    public QuickUnion(int N) {
        id = new int[N];
        sz = new int[N];
        for (int i = 0; i < N; i++)
            id[i] = i;
        for (int i = 0; i < N; i++)
            sz[i] = 1;
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
    }
}
