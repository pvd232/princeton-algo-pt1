import java.util.ArrayList;

import edu.princeton.cs.algs4.In;

public class Board {
    private final int[][] tiles;
    private final int nTiles;
    private int blankX;
    private int blankY;
    private final int cachedM;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] theTiles) {
        // iterators for tiles clone
        nTiles = theTiles.length;

        tiles = new int[nTiles][nTiles];
        for (int i = 0; i < nTiles; i++) {
            for (int j = 0; j < nTiles; j++) {
                if (theTiles[i][j] == 0) {
                    blankX = i;
                    blankY = j;
                }
                // increment col for tiles in row
                tiles[i][j] = theTiles[i][j];
            }
        }
        cachedM = manhattan();
    }

    // string representation of this board
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(nTiles);
        res.append("\n");
        for (int[] tileRow : tiles) {
            for (int tile : tileRow) {
                res.append(tile);
                res.append(" ");
            }
            res.append("\n");
        }
        return res.toString();
    }

    // board dimension n
    public int dimension() {
        return nTiles;
    }

    // number of tiles out of place
    public int hamming() {
        int distance = 0;
        for (int i = 0; i < nTiles; i++) {
            for (int j = 0; j < nTiles; j++) {
                distance += findHamming(i, j);
            }
        }
        return distance;
    }

    private int findHamming(int i, int j) {
        if (tiles[i][j] == 0) {
            return 0;
        }
        if (tiles[i][j] != findTargetValue(i, j)) {
            return 1;
        } else
            return 0;
    }

    private int findTargetValue(int row, int col) {
        return (row * nTiles) + col + 1;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < nTiles; i++) {
            for (int j = 0; j < nTiles; j++) {
                distance += findManhattan(i, j);
            }
        }
        return distance;
    }

    private int findManhattan(int i, int j) {
        if (tiles[i][j] == 0)
            return 0;
        int targetRow = findTargetRow(tiles[i][j]);
        int targetCol = findTargetCol(tiles[i][j]);
        return Math.abs(i - targetRow) + Math.abs(j - targetCol);
    }

    // Subroutine to find manhattan distance for a given tile coordinates and value
    private int findTargetRow(int value) {
        return (value - 1) / nTiles;
    }

    private int findTargetCol(int value) {
        return (value - 1) % nTiles;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return cachedM == 0;
    }

    // public int tileAt(int i, int j) {
    // return tiles[i][j];
    // }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        // Check data type is Board
        Board yBoard = (Board) y;

        // Check dimensions are equal
        if (yBoard.dimension() != dimension())
            return false;

        // Finally compare tile values
        return yBoard.evalTiles(tiles);
    }

    private boolean evalTiles(int[][] otherTiles) {
        for (int i = 0; i < nTiles; i++) {
            for (int j = 0; j < nTiles; j++) {
                if (tiles[i][j] != otherTiles[i][j])
                    return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> res = new ArrayList<Board>(4);
        for (int[] boundedTile : boundedTiles()) {
            res.add(new Board(swap(boundedTile)));
        }
        return res;
    }

    // Finds valid neighboring tiles
    private int[][] boundedTiles() {
        ArrayList<int[]> res = new ArrayList<int[]>(4);
        int[] directions = { 1, -1 };
        for (int dX : directions) {
            int tmpX = blankX + dX;
            // If the tile is inside the tiles matrix
            if (tmpX >= 0 && tmpX < nTiles)
                res.add(new int[] { tmpX, blankY });
        }
        for (int dY : directions) {
            int tmpY = blankY + dY;
            // If the tile is inside the tiles matrix
            if (tmpY >= 0 && tmpY < nTiles)
                res.add(new int[] { blankX, tmpY });
        }
        return res.toArray(new int[res.size()][res.size()]);
    }

    // Deep clone double nested array
    private int[][] cloneTiles() {
        int[][] copy = new int[nTiles][nTiles];
        for (int i = 0; i < nTiles; i++) {
            for (int j = 0; j < nTiles; j++) {
                copy[i][j] = tiles[i][j];
            }
        }
        return copy;
    }

    // New tiles resulting from swapping blank tile
    private int[][] swap(int[] tile) {
        int[][] copyTiles = cloneTiles();
        int tmp = copyTiles[tile[0]][tile[1]];
        copyTiles[tile[0]][tile[1]] = 0;
        copyTiles[blankX][blankY] = tmp;
        return copyTiles;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] copyTiles = cloneTiles();
        // Swap first and second tiles, which will always exist (n >= 2)
        int[][] swapTiles = findTwinSwap();
        int[] firstSwap = swapTiles[0];
        int[] secondSwap = swapTiles[1];
        int tmp = copyTiles[firstSwap[0]][firstSwap[1]];
        copyTiles[firstSwap[0]][firstSwap[1]] = copyTiles[secondSwap[0]][secondSwap[1]];
        copyTiles[secondSwap[0]][secondSwap[1]] = tmp;
        return new Board(copyTiles);
    }

    private int[][] findTwinSwap() {
        ArrayList<int[]> swapTiles = new ArrayList<int[]>(2);
        int found = 0;
        for (int i = 0; i < nTiles; i++) {
            for (int j = 0; j < nTiles; j++) {
                if (found == 2)
                    return swapTiles.toArray(new int[swapTiles.size()][2]);
                else if (i == blankX && j == blankY)
                    continue;
                else
                    swapTiles.add(new int[] { i, j });
            }
        }
        return swapTiles.toArray(new int[swapTiles.size()][2]);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // Unit tests
        assert initial.dimension() == n;
    }
}