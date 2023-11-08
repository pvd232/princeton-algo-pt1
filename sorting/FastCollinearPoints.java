package sorting;

import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private LineSegment[] cLineSegments;
    private int nSeg = 0;

    public FastCollinearPoints(Point[] points) // finds all line segments containing 4 or more points
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
        for (int i = 0; i < tmpPoints.length - 1; i++) {
            int q = i + 1;
            if (tmpPoints[i].compareTo(tmpPoints[q]) == 0)
                throw new IllegalArgumentException("non unique point");
        }
        ArrayList<LineSegment> result = new ArrayList<LineSegment>();
        if (tmpPoints.length > 3) {
            Point[] backup = tmpPoints.clone();
            for (Point p : tmpPoints) {
                Arrays.sort(backup, p.slopeOrder());
                findSegments(p, result, backup);
            }
        }
        cLineSegments = result.toArray(new LineSegment[result.size()]);
    }

    private ArrayList<LineSegment> findSegments(Point origin, ArrayList<LineSegment> segs, Point[] sortedPoints) {
        // Origin (p) is 0 and start (q) is 1
        int start = 1;
        double slope = origin.slopeTo(sortedPoints[start]);
        for (int i = 2; i < sortedPoints.length; i++) {
            double tmpSlope = origin.slopeTo(sortedPoints[i]);
            // If the slope is the same as the OG slope, generate line segment
            if (slope != tmpSlope) {
                // If more than 3 points have been added, append linesegment
                if (i - start > 2) {
                    Point[] segPoints = genSeg(origin, start, i, sortedPoints);
                    if (segPoints[0].compareTo(origin) == 0) {
                        segs.add(new LineSegment(segPoints[0], segPoints[segPoints.length - 1]));
                    }
                }
                // Reset start to evaluate other potential line segments with origin
                start = i;
                slope = tmpSlope;
            }
        }
        // Get last segment, if it exists
        // Check exists by comparing start counter to len of sorted points
        if (sortedPoints.length - start > 2) {
            Point[] segPoints = genSeg(origin, start, sortedPoints.length, sortedPoints);
            // If segment is not a subsegment, grab it
            if (segPoints[0].compareTo(origin) == 0)
                segs.add(new LineSegment(segPoints[0], segPoints[segPoints.length - 1]));
        }
        return segs;
    }

    private Point[] genSeg(Point p, int st, int end, Point[] points) {

        // Point array is the start minus end plus 1 for origin p
        Point[] res = new Point[end - st + 1];
        int numRes = 0;
        res[numRes++] = p;

        for (int i = st; i < end; i++) {
            res[numRes++] = points[i];
        }

        // Sort points to check if p remains origin, to prevent adding subsegments
        Arrays.sort(res, (a, b) -> a.compareTo(b));
        return res;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}