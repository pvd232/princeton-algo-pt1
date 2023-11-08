import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    // Main driver method
    public static void main(String[] args) {
        String champion = "";
        int numWords = 0;
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            numWords++;
            double probability = (double) 1 / numWords;
            if (StdRandom.bernoulli(probability)) {
                champion = word;
            }
        }
        StdOut.println(champion);
    }
}
