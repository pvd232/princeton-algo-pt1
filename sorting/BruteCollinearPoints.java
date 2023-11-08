package sorting;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private LineSegment[] cLineSegments;
    private int nSeg = 0;

    public BruteCollinearPoints(Point[] points) // Finds all line segments containing 4 points
    {
        if (points == null) {
            throw new IllegalArgumentException("points can't be null");
        }
        Point[] tmpPoints = new Point[points.length];
        int tmp = 0;
        // Check for null points
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("point can't be null");
            } else
                tmpPoints[tmp++] = p;
        }
        // Check for duplicate points
        Arrays.sort(tmpPoints, (a, b) -> a.compareTo(b));
        for (int p = 0; p < tmpPoints.length - 1; p++) {
            int q = p + 1;
            if (tmpPoints[p].compareTo(tmpPoints[q]) == 0)
                throw new IllegalArgumentException("non unique point");
        }
        cLineSegments = extractSegs(tmpPoints);
    }

    private LineSegment[] extractSegs(Point[] points) {
        LineSegment[] tmpSegments = new LineSegment[points.length * 4];

        Arrays.sort(points, (a, b) -> a.compareTo(b));

        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    Point p = points[i];
                    Point q = points[j];
                    Point r = points[k];
                    // Check existing slopes match to prevent uncecessary loop
                    if (p.slopeTo(q) == p.slopeTo(r)) {
                        for (int m = k + 1; m < points.length; m++) {
                            Point s = points[m];
                            // If slopes match
                            if (p.slopeTo(q) == p.slopeTo(s)) {
                                tmpSegments[nSeg++] = new LineSegment(p, s);
                            }
                        }
                    }
                }
            }
        }

        LineSegment[] finalSegs = new LineSegment[nSeg];
        for (int m = 0; m < nSeg; m++) {
            finalSegs[m] = tmpSegments[m];
        }
        return finalSegs;
    }

    public int numberOfSegments() // The number of line segments
    {
        return nSeg;
    }

    public LineSegment[] segments() // The line segments
    {
        return cLineSegments.clone();
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        return;
    }
}