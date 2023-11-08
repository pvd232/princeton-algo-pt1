package intro;

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
            if (StdRandom.bernoulli(1 / numWords)) {
                champion = word;
            }
        }
        StdOut.println("Surviving Champion: " + champion);
    }
}
