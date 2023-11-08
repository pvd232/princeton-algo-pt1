import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Queue;

public class Solver {
    private final Queue<Board> gamePlay;
    private final boolean solvable;

    private class SearchNode {
        final Board move;
        final SearchNode prev;
        final int numMoves;
        private final int cachedManhattan;

        public SearchNode(Board board, SearchNode prevNode, int countMoves) {
            move = board;
            prev = prevNode;
            numMoves = countMoves;
            cachedManhattan = move.manhattan() + countMoves;
        }

        public Comparator<SearchNode> priorityFunction() {
            return new SearchNodeComparator();
        }

        private int manhattanFunction() {
            return cachedManhattan;
        }

        private class SearchNodeComparator implements Comparator<SearchNode> {
            public int compare(SearchNode a, SearchNode b) {
                // Prioritize lower distances
                if (a.manhattanFunction() < b.manhattanFunction())
                    return -1;
                else if (a.manhattanFunction() == b.manhattanFunction())
                    return 0;
                else
                    return 1;
            }
        }

    }

    private class Game {
        public boolean solved = false;
        private final MinPQ<SearchNode> pq;
        private final Stack<Board> gamePlay;

        public Game(Board initial) {
            SearchNode firstNode = new SearchNode(initial, null, 0);
            // Create priority Stack with comparator for delMin
            pq = new MinPQ<SearchNode>(firstNode.priorityFunction());
            gamePlay = new Stack<Board>();

            // First insert inital board into PQ
            pq.insert(firstNode);
        }

        public void play() {
            SearchNode node = pq.delMin();
            if (node.move.isGoal()) {
                solved = true;
                SearchNode curr = node;

                while (curr != null) {
                    gamePlay.push(curr.move);
                    curr = curr.prev;
                }
                return;
            }

            Iterable<Board> neighbors = node.move.neighbors();
            for (Board neighbor : neighbors) {
                SearchNode tmpNode = new SearchNode(neighbor, node, node.numMoves + 1);
                if (node.prev == null || !tmpNode.move.equals(node.prev.move))
                    pq.insert(tmpNode);
            }
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        gamePlay = new Queue<Board>();
        Game og = new Game(initial);
        Game twin = new Game(initial.twin());

        while (!og.solved && !twin.solved) {
            og.play();
            twin.play();
        }
        if (og.solved) {
            solvable = true;
            while (!og.gamePlay.isEmpty())
                gamePlay.enqueue(og.gamePlay.pop());
        } else {
            solvable = false;
            while (!twin.gamePlay.isEmpty())
                gamePlay.enqueue(twin.gamePlay.pop());

        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable())
            // Subtract one to account for starting board inclusion
            return gamePlay.size() - 1;
        else
            return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable())
            return gamePlay;
        else
            return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            for (Board board : solver.solution())
                StdOut.println(board);
            StdOut.println("Minimum number of moves = " + solver.moves());

        }
    }
}