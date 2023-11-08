package bst;

public class BinarySearchTree {
    private Node root;

    private class Node {
        private Comparable<Object> key;
        private Object val;
        private Node left, right;

        public Node(Comparable<Object> key, Object value) {
            this.key = key;
            this.val = value;
        }
    }

    public BinarySearchTree(Comparable<Object> nodeKey, Object nodeValue) {
        root = new Node(nodeKey, nodeValue);
    }

    public Comparable<Object> floor(Comparable<Object> key) {
        Node x = floor(root, key);
        return x.key;
    }

    public Comparable<Object> ceiling(Comparable<Object> key) {
        Node x = ceiling(root, key);
        return x.key;
    }

    private Node ceiling(Node root, Comparable<Object> key) {
        if (root == null)
            return null;
        int cmp = key.compareTo(root.key);

        if (cmp == 0)
            return root;
        // it wants to be smaller than me, so if it is bigger than me, pass to the right
        if (cmp > 0)
            return ceiling(root.right, key);
        // if it is smaller than me, pass to the left, but if it is null, return me,
        // because I am the smallest thing bigger than it
        Node t = ceiling(root.left, key);
        if (t != null)
            return t;
        else
            return root;
    }

    private Node floor(Node root, Comparable<Object> key) {
        if (root == null)
            return null;
        int cmp = key.compareTo(root.key);
        if (cmp == 0)
            return root;
        if (cmp < 0)
            return floor(root.left, key);
        Node t = floor(root.right, key);
        if (t != null)
            return t;
        else
            return root;
    }

    public Object get(Comparable<Object> key) {
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0)
                x = x.left;
            else if (cmp > 0)
                x = x.right;
            else if (cmp == 0)
                return x.val;
        }
        return null;
    }

    public void put(Comparable<Object> key, Object val) {
        root = put(root, key, val);
    }

    private Node put(Node x, Comparable<Object> key, Object val) {
        if (x == null)
            return new Node(key, val);
        int cmp = key.compareTo(x.key);
        if (cmp < 0)
            x.left = put(x.left, key, val);
        else if (cmp > 0)
            x.right = put(x.right, key, val);
        else if (cmp == 0)
            x.val = val;
        return x;
    }
}
