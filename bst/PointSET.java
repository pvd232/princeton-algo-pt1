package bst;

import java.util.ArrayList;
import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private final SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        // draw the points
        StdDraw.enableDoubleBuffering();
        for (Point2D p : points) {
            p.draw();
        }
        StdDraw.show();
    }

    // // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> res = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p))
                res.add(p);
        }
        return res;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (points.isEmpty()) {
            return null;
        }
        Point2D closest = null;
        double dist = Double.POSITIVE_INFINITY;
        for (Point2D point : points) {
            double tmpDist = point.distanceSquaredTo(p);
            if (tmpDist < dist) {
                dist = tmpDist;
                closest = point;
            }
        }
        return closest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);

        PointSET pointsSet = new PointSET();

        assert pointsSet.isEmpty();

        Point2D aPoint = null;
        int count = 0;
        ArrayList<Point2D> testPoints = new ArrayList<>();
        while (in.hasNextLine()) {
            count++;
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D newPoint = new Point2D(x, y);
            StdDraw.setPenRadius(0.01);
            newPoint.draw();
            pointsSet.insert(newPoint);
            if (aPoint == null)
                aPoint = newPoint;
            testPoints.add(newPoint);
        }
        assert !pointsSet.isEmpty();
        assert pointsSet.contains(aPoint);
        assert count == pointsSet.size();

        int median = (count / 2) - 1;
        testPoints.sort(Point2D.X_ORDER);

        double minX = testPoints.get(0).x();
        double maxX = testPoints.get(median).x();

        testPoints.sort(Point2D.Y_ORDER);

        double minY = testPoints.get(0).y();
        double maxY = testPoints.get(testPoints.size() - 1).y();

        RectHV testRect = new RectHV(minX, minY, maxX, maxY);
        StdDraw.setPenRadius();
        testRect.draw();

        int testRangeCount = 0;
        Iterable<Point2D> testRange = pointsSet.range(testRect);
        Iterator<Point2D> it = testRange.iterator();
        while (it.hasNext()) {
            testRangeCount++;
            Point2D p = it.next();
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.01);
            p.draw();
        }
        int bruteRangeCount = 0;
        // Brute force test range function
        for (Point2D p : pointsSet.points) {
            if (p.x() <= maxX && p.x() >= minX && p.y() <= maxY && p.y() >= minY)
                bruteRangeCount++;
        }
        assert testRangeCount == bruteRangeCount;
        assert pointsSet.nearest(aPoint) == aPoint;
    }
}