import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int len;
    private final int vTop;
    private final int vBottom;
    private final WeightedQuickUnionUF uF;
    private final WeightedQuickUnionUF noBackwashUf;

    private boolean[] open;
    private int numOpen = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be greater than 0");

        // initialize union-find data structure
        len = n;
        vTop = 0;
        vBottom = (n * n) + 1;
        open = new boolean[n * n + 2];
        uF = new WeightedQuickUnionUF(n * n + 2);
        noBackwashUf = new WeightedQuickUnionUF(n * n + 2);
        open[vTop] = true;
        open[vBottom] = true;
        numOpen = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int index = getIndex(row, col, true);
        if (open[index]) {
            return;
        }

        open[index] = true;
        numOpen++;
        // connect newly opened site to adjacent open sites, if they are open
        connect(row, col, index);
    }

    // // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return open[getIndex(row, col, true)];
    }

    // // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return noBackwashUf.find(getIndex(row, col, true)) == noBackwashUf.find(vTop);
    }

    // // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // // does the system percolate?
    public boolean percolates() {
        return uF.find(vTop) == uF.find(vBottom);
    }

    private boolean validate(int row, int col) {
        if (row < 1 || row > this.len || col < 1 || col > this.len) {
            throw new IllegalArgumentException("row and col must be between 0 and n+1");
        }
        return true;
    }

    private void connect(int row, int col, int index) {
        int topRowIndex = index - len;
        int bottomRowIndex = index + len;
        int leftColIndex = index - 1;
        int rightColIndex = index + 1;

        if (row == 1) {
            // merge index into vTop
            uF.union(vTop, index);
            noBackwashUf.union(vTop, index);
        }

        if (row == len)
            // merge index into vBottom
            uF.union(vBottom, index);

        // merge adjacent index into index
        if (row > 1 && open[topRowIndex]) {
            uF.union(index, topRowIndex);
            noBackwashUf.union(index, topRowIndex);
        }

        if (row < len && open[bottomRowIndex]) {
            uF.union(index, bottomRowIndex);
            noBackwashUf.union(index, bottomRowIndex);
        }

        if (col > 1 && open[leftColIndex]) {
            uF.union(index, leftColIndex);
            noBackwashUf.union(index, leftColIndex);
        }

        if (col < len && open[rightColIndex]) {
            uF.union(index, rightColIndex);
            noBackwashUf.union(index, rightColIndex);
        }

    }

    private int getIndex(int row, int col, boolean validate) {
        // convert 2D coordinates to 1D index
        // subtract 1 from row for 0-indexing, keep col as is because of vTop
        if (validate)
            validate(row, col);
        return ((row - 1) * len) + col;
    }
}