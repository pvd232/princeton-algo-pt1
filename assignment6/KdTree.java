import java.util.ArrayList;
import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;

    public KdTree() {
    }

    private class Node {
        private double key; // sorted by key
        private Point2D val; // associated data
        private Node left, right; // left and right subtrees
        private int size; // number of nodes in subtree
        private Node prev; // parent node

        public Node(double key, Point2D val, int size, Node prev) {
            this.key = key;
            this.val = val;
            this.size = size;
            this.prev = prev;
        }
    }

    /**
     * Returns true if this symbol table is empty.
     * 
     * @return {@code true} if this symbol table is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * 
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return size(root);
    }

    // return number of key-value pairs in BST rooted at x
    private int size(Node x) {
        if (x == null)
            return 0;
        else
            return x.size;
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        root = insert(root, p, 0);
    }

    private Node insert(Node node, Point2D p, int lvl) {
        if (node == null)
            return new Node(getKey(p, lvl), p, 1, null);
        int cmp = Double.compare(getKey(p, lvl), node.key);
        if (cmp < 0) {
            node.left = insert(node.left, p, lvl + 1);
            node.left.prev = node;
        } else if (cmp > 0) {
            node.right = insert(node.right, p, lvl + 1);
            node.right.prev = node;
        } else {
            int otherCmp = Double.compare(getNonKey(p, lvl), getNonKey(node.val, lvl));
            if (otherCmp < 0) {
                node.left = insert(node.left, p, lvl + 1);
                node.left.prev = node;
            } else if (otherCmp > 0) {
                node.right = insert(node.right, p, lvl + 1);
                node.right.prev = node;
            } else
                node.val = p;
        }

        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    private double getNonKey(Point2D p, int lvl) {
        if (lvl % 2 == 0)
            return p.y();
        else
            return p.x();
    }

    private double getKey(Point2D p, int lvl) {
        if (lvl % 2 == 0)
            return p.x();
        else
            return p.y();
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return contains(root, p, 0);
    }

    private boolean contains(Node node, Point2D p, int lvl) {
        if (node == null)
            return false;
        int cmp = Double.compare(getKey(p, lvl), node.key);
        if (cmp < 0)
            return contains(node.left, p, lvl + 1);
        else if (cmp > 0)
            return contains(node.right, p, lvl + 1);
        else {
            int otherCmp = Double.compare(getNonKey(p, lvl), getNonKey(node.val, lvl));
            if (otherCmp < 0)
                return contains(node.left, p, lvl + 1);
            else if (otherCmp > 0)
                return contains(node.right, p, lvl + 1);
        }
        return node.val.equals(p);

    }

    private Iterable<Node> bfs() {
        ArrayList<Node> res = new ArrayList<>();
        bfs(root, res);
        return res;
    }

    private void bfs(Node node, ArrayList<Node> res) {
        Queue<Node> q = new Queue<>();
        q.enqueue(node);
        while (q.size() > 0) {
            int qSize = q.size();
            for (int i = 0; i < qSize; i++) {
                Node currNode = q.dequeue();
                res.add(currNode);
                if (currNode.left != null) {
                    q.enqueue(currNode.left);
                }
                if (currNode.right != null)
                    q.enqueue(currNode.right);
            }
            res.add(null); // Null node indicates level transition
        }
    }

    // draw all points to standard draw
    public void draw() {
        // draw the points
        StdDraw.enableDoubleBuffering();
        // Draw axis boundaries
        StdDraw.line(0, 0, 0, 1);
        StdDraw.line(0, 0, 1, 0);
        StdDraw.line(0, 1, 1, 1);
        StdDraw.line(1, 0, 1, 1);
        int lvl = 0;
        for (Node n : bfs()) {
            if (n == null)
                lvl++;
            else {
                // Draw point
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.01);
                StdDraw.point(n.val.x(), n.val.y());
                StdDraw.show();
                StdDraw.setPenRadius();

                // Get updated bounds for line segment and draw segment with appropriate color
                Point2D[] seg = getSeg(n, lvl);
                if (lvl % 2 == 0)
                    StdDraw.setPenColor(StdDraw.RED);
                else
                    StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(seg[0].x(), seg[0].y(), seg[1].x(), seg[1].y());
                StdDraw.show();
            }
        }
    }

    // Return line segment for given node
    private Point2D[] getSeg(Node n, int lvl) {
        double axisMin = 0, axisMax = 1;
        if (n.prev == null)
            return new Point2D[] { new Point2D(n.val.x(), 0), new Point2D(n.val.x(), 1) };
        if (n.prev.left != null && n.prev.left.equals(n)) { // if point is left child, yMax is parent
            axisMax = getNonKey(n.prev.val, lvl);
            Node gP = n.prev.prev;
            if (gP != null && gP.prev != null && getNonKey(gP.prev.val, lvl) <= getNonKey(n.val, lvl))
                // axisMin is great grandparent, or 0
                axisMin = getNonKey(gP.prev.val, lvl);
            else
                axisMin = 0;
        } else { // if point is right child, axisMin is parent
            axisMin = getNonKey(n.prev.val, lvl);
            Node gP = n.prev.prev;
            // axisMin is great grandparent, or 1
            if (gP != null && gP.prev != null && getNonKey(gP.prev.val, lvl) >= getNonKey(n.val, lvl))
                axisMax = getNonKey(gP.prev.val, lvl);
            else
                axisMax = 1;

        }
        if (lvl % 2 == 0)
            return new Point2D[] { new Point2D(n.val.x(), axisMin), new Point2D(n.val.x(), axisMax) };
        else
            return new Point2D[] { new Point2D(axisMin, n.val.y()), new Point2D(axisMax, n.val.y()) };
    }

    private Point2D[] getParallelSeg(Node n, int lvl, boolean left, Point2D[] seg) {
        if (n.prev == null) { // if prev is null, return line seg for root (x-axis)
            if (left)
                return new Point2D[] { new Point2D(0, 0), new Point2D(0, 1) };
            else
                return new Point2D[] { new Point2D(1, 0), new Point2D(1, 1) };
        }
        Node gP = n.prev.prev;
        if (gP == null) { // if gp node null, node is root child (will always follow this pattern)
            if (left)
                return new Point2D[] { new Point2D(0, 0), new Point2D(n.prev.val.x(), 0) };
            else
                return new Point2D[] { new Point2D(0, 1), new Point2D(n.prev.val.x(), 1) };
        }

        double axisConst = 0;
        if (left) { // check if gp node key is valid, and if so, assign to parallel axis value
            if (gP.key <= n.key)
                axisConst = gP.key;
            else
                axisConst = 0;
        } else {
            if (gP.key >= n.key)
                axisConst = gP.key;
            else
                axisConst = 1;
        }

        if (lvl % 2 == 0) // if x-axis node, y value changes while x is const
            return new Point2D[] { new Point2D(axisConst, getNonKey(seg[0], lvl)),
                    new Point2D(axisConst, getNonKey(seg[1], lvl)) };
        else // vice-versa
            return new Point2D[] { new Point2D(getNonKey(seg[0], lvl), axisConst),
                    new Point2D(getNonKey(seg[1], lvl), axisConst) };
    }

    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> res = new ArrayList<>();
        range(root, rect, res, 0); // set default max to unit square boundaries
        return res;
    }

    private RectHV getRect(Point2D d1, Point2D d2) {
        return new RectHV(d1.x(), d1.y(), d2.x(), d2.y());
    }

    private void range(Node node, RectHV rect, ArrayList<Point2D> res, int lvl) {
        if (node == null)
            return;
        if (rect.contains(node.val))
            res.add(node.val);

        Point2D[] seg = getSeg(node, lvl);
        // Only search left / right subtrees if rect could contain the point
        if (rect.intersects(getRect(getParallelSeg(node, lvl, true, seg)[0], seg[1])))
            range(node.left, rect, res, lvl + 1);

        if (rect.intersects(getRect(seg[0], getParallelSeg(node, lvl, false, seg)[1])))
            range(node.right, rect, res, lvl + 1);
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        // Use array as global(ish) pointer for helper function
        ArrayList<Point2D> res = new ArrayList<>();
        res.add(root.val);
        nearest(p, root, 0, res);
        return res.get(0);

    }

    private void nearest(Point2D p, Node node, int lvl, ArrayList<Point2D> res) {
        if (node == null)
            return;
        double cmpDist = node.val.distanceSquaredTo(p);
        if (cmpDist < res.get(0).distanceSquaredTo(p)) { // If curr point is closer, clear old point and replace with
                                                         // new
            res.clear();
            res.add(node.val);
        }

        Point2D[] seg = getSeg(node, lvl);
        double leftDist = getRect(getParallelSeg(node, lvl, true, seg)[0],
                seg[1]).distanceSquaredTo(p);
        double rightDist = getRect(seg[0], getParallelSeg(node, lvl, false,
                seg)[1]).distanceSquaredTo(p);

        if (leftDist < cmpDist && rightDist < cmpDist) { // if both rectangles could have a closer point
            if (getKey(p, lvl) > node.key) { // if point is larger, search right node first
                nearest(p, node.right, lvl + 1, res);
                nearest(p, node.left, lvl + 1, res);
            } else { // otherwise search left node
                nearest(p, node.left, lvl + 1, res);
                nearest(p, node.right, lvl + 1, res);
            }
        } else { // otherwise check each child node individually
            if (node.left != null && leftDist < cmpDist)
                nearest(p, node.left, lvl + 1, res);
            if (node.right != null && rightDist < cmpDist)
                nearest(p, node.right, lvl + 1, res);
        }
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);

        KdTree kdTree = new KdTree();

        assert kdTree.isEmpty();

        int count = 0;
        ArrayList<Point2D> testPoints = new ArrayList<>();
        while (in.hasNextLine()) {
            count++;
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D newPoint = new Point2D(x, y);
            kdTree.insert(newPoint);
            testPoints.add(newPoint);
        }
        kdTree.draw();
        assert !kdTree.isEmpty();

        assert count == kdTree.size();

        int median = (count / 2);
        testPoints.sort(Point2D.X_ORDER);

        double minX = testPoints.get(0).x();
        double maxX = testPoints.get(median).x();

        testPoints.sort(Point2D.Y_ORDER);

        double minY = testPoints.get(0).y();
        double maxY = testPoints.get(testPoints.size() - 1).y();
        RectHV testRect = new RectHV(minX, minY, maxX, maxY);
        StdDraw.setPenColor(StdDraw.CYAN);
        testRect.draw();
        StdDraw.show();

        Iterable<Point2D> testRange = kdTree.range(testRect);
        Iterator<Point2D> it = testRange.iterator();

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.01);
        int testRangeCount = 0;

        while (it.hasNext()) {
            // Draw points returned by range
            Point2D next = it.next();
            StdDraw.point(next.x(), next.y());
            testRangeCount++;
        }
        StdDraw.show();

        // Brute force test range
        int bruteForceRangeCount = 0;
        for (Point2D p : testRange) {
            if (p.x() >= testRect.xmin() && p.x() <= testRect.xmax() && p.y() <= testRect.ymin()
                    && p.y() >= testRect.ymax())
                bruteForceRangeCount++;
        }

        assert testRangeCount == bruteForceRangeCount;

        Point2D testPoint = new Point2D(0.0, 0.0);
        Point2D testNearestPoint = kdTree.nearest(testPoint);
        double dist = Double.POSITIVE_INFINITY;

        Point2D bruteTestNearestPoint = null;
        for (Node p : kdTree.bfs()) {
            if (p != null) {
                double tmpDist = p.val.distanceSquaredTo(testPoint);
                if (tmpDist < dist) {
                    dist = tmpDist;
                    bruteTestNearestPoint = p.val;

                }
            }
        }
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(testPoint.x(), testPoint.y());
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(testNearestPoint.x(), testNearestPoint.y());
        StdDraw.setPenColor(StdDraw.PINK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(bruteTestNearestPoint.x(), bruteTestNearestPoint.y());
        StdDraw.show();
        assert bruteTestNearestPoint.equals(testNearestPoint);
    }
}
