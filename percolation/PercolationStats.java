package percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double C_95 = 1.96;
    private double[] pResults;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (trials <= 0)
            throw new IllegalArgumentException("trials must be greater than 0");
        if (n <= 0)
            throw new IllegalArgumentException("n must be greater than 0");
        pResults = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                p.open(row, col);
            }
            pResults[i] = (double) p.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(pResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(pResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return StdStats.mean(pResults) - ((C_95 * StdStats.stddev(pResults) / Math.sqrt(pResults.length)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return StdStats.mean(pResults) + ((C_95 * StdStats.stddev(pResults) / Math.sqrt(pResults.length)));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        System.out.println("mean = " + ps.mean());
        System.out.println("stddev = " + ps.stddev());
        System.out.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }

}