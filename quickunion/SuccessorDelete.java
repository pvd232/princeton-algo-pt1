package quickunion;

public class SuccessorDelete {
    private int[] id;
    private int[] sz;
    private int[] max;

    public SuccessorDelete(int N) {
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
        return max[root(p)];
    }

    public void remove(int p) {
        // remove value by unioning with next value
        union(p, p + 1);
    }

    public int successor(int p) {
        // get value such that value >= p
        return find(p);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        SuccessorDelete sd = new SuccessorDelete(n);
        sd.remove(Integer.parseInt(args[1]));
        sd.remove(Integer.parseInt(args[2]));

        System.out.println(sd.successor(Integer.parseInt(args[3])));
    }
}
