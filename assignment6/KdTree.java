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
        private RectHV qRect = null; // query aligned rect

        public Node(double key, Point2D val, int size, RectHV qRect) {
            this.key = key;
            this.val = val;
            this.size = size;
            this.qRect = qRect;
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
        double xMin = 0, yMin = 0, xMax = 1, yMax = 1;
        root = insert(root, p, 0, xMin, yMin, xMax, yMax);
    }

    private Node insert(Node node, Point2D p, int lvl, double xMin, double yMin, double xMax, double yMax) {
        if (node == null)
            return new Node(getKey(p, lvl), p, 1, new RectHV(xMin, yMin, xMax, yMax));

        // Compare relevant key
        int cmp = Double.compare(getKey(p, lvl), node.key);
        if (cmp < 0) // If point is less than curr node, insert left
            if (lvl % 2 == 0) // if curr node is x aligned, new node xMax will be current node's x
                node.left = insert(node.left, p, lvl + 1, xMin, yMin, node.val.x(), yMax);
            else // if curr node is y aligned, new node yMax will be current node's y
                node.left = insert(node.left, p, lvl + 1, xMin, yMin, xMax, node.val.y());

        else {
            if (node.val.equals(p))
                return node;
            else if (lvl % 2 == 0) // vice versa, inclusive of key being the same
                node.right = insert(node.right, p, lvl + 1, node.val.x(), yMin, xMax, yMax); // xMin
            else
                node.right = insert(node.right, p, lvl + 1, xMin, node.val.y(), xMax, yMax); // yMin
        }
        node.size = 1 + size(node.left) + size(node.right); // update size
        return node;
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
        if (node == null) // didnt find the node
            return false;
        int cmp = Double.compare(getKey(p, lvl), node.key);
        if (cmp < 0) // if less than, go left
            return contains(node.left, p, lvl + 1);
        else if (node.val.equals(p)) // otherwise, if its the same return true, if not go right
            return true;
        else
            return contains(node.right, p, lvl + 1);

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

                if (lvl % 2 == 0) {
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.line(n.key, n.qRect.ymin(), n.key, n.qRect.ymax());
                } else {
                    StdDraw.setPenColor(StdDraw.BLUE);
                    StdDraw.line(n.qRect.xmin(), n.key, n.qRect.xmax(), n.key);
                }
                StdDraw.show();
            }
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> res = new ArrayList<>();
        range(root, rect, res, 0); // set default max to unit square boundaries
        return res;
    }

    private void range(Node node, RectHV rect, ArrayList<Point2D> res, int lvl) {
        if (node == null)
            return;
        if (rect.contains(node.val))
            res.add(node.val);

        // Only search left / right subtrees if rect could contain the point
        if (node.left != null && node.left.qRect.intersects(rect))
            range(node.left, rect, res, lvl + 1);
        if (node.right != null && node.right.qRect.intersects(rect))
            range(node.right, rect, res, lvl + 1);
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (root == null)
            return null;
        return nearest(p, root, 0, root.val);
    }

    private Point2D nearest(Point2D p, Node node, int lvl, Point2D closest) {
        if (node.qRect.distanceSquaredTo(p) >= closest.distanceSquaredTo(p))
            return closest;

        double cmpDist = node.val.distanceSquaredTo(p);
        double currDist = Math.min(closest.distanceSquaredTo(p), cmpDist);

        if (currDist == cmpDist)
            closest = node.val;
        if (node.left != null && node.right != null) {
            double leftDist = node.left.qRect.distanceSquaredTo(p);
            double rightDist = node.right.qRect.distanceSquaredTo(p);

            if (leftDist < currDist && rightDist < currDist) { // if both rectangles could have a closer point
                if (getKey(p, lvl) > node.key) { // if point is larger, search right node first
                    closest = nearest(p, node.right, lvl + 1, closest);
                    closest = nearest(p, node.left, lvl + 1, closest);
                } else { // otherwise search left node
                    closest = nearest(p, node.left, lvl + 1, closest);
                    closest = nearest(p, node.right, lvl + 1, closest);
                }
            } else {
                if (node.left.qRect.distanceSquaredTo(p) < currDist)
                    closest = nearest(p, node.left, lvl + 1, closest);
                if (node.right.qRect.distanceSquaredTo(p) < cmpDist)
                    closest = nearest(p, node.right, lvl + 1, closest);
            }
        } else {
            if (node.left != null && node.left.qRect.distanceSquaredTo(p) < currDist)
                closest = nearest(p, node.left, lvl + 1, closest);
            else if (node.right != null && node.right.qRect.distanceSquaredTo(p) < currDist)
                closest = nearest(p, node.right, lvl + 1, closest);
        }
        return closest;
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
            System.out.println("count " + count);
            System.out.println("kdTree.size() " + kdTree.size());
            assert count >= kdTree.size();
            testPoints.add(newPoint);
        }
        kdTree.draw();
        StdDraw.show();
        assert !kdTree.isEmpty();

        int median = (count / 2);
        testPoints.sort(Point2D.X_ORDER);

        double minX = testPoints.get(0).x();
        double maxX = testPoints.get(median).x();

        testPoints.sort(Point2D.Y_ORDER);

        double minY = testPoints.get(0).y();
        double maxY = testPoints.get(testPoints.size() - 1).y();
        RectHV testRect = new RectHV(minX, minY, maxX, maxY);
        // StdDraw.setPenColor(StdDraw.CYAN);
        // testRect.draw();
        // StdDraw.show();

        Iterable<Point2D> testRange = kdTree.range(testRect);
        Iterator<Point2D> it = testRange.iterator();

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.01);
        while (it.hasNext()) {
            // Draw points returned by range
            Point2D next = it.next();
            StdDraw.point(next.x(), next.y());
        }
        StdDraw.show();

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
