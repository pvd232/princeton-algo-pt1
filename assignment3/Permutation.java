import edu.princeton.cs.algs4.StdIn;
import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            rq.enqueue(word);
        }
        Iterator<String> iterator = rq.iterator();
        int i = 0;
        while (iterator.hasNext() && i < k) {
            System.out.println(iterator.next());
            i++;
        }
    }
}
