import java.io.*;
import java.util.*;
import java.util.stream.*;

class Result {

    /*
     * Compute the optimal reduction sequence to zero, based on
     * two choices at each step: factorize or decrement.
     * 
     * Uses a canonical breadth-first search (BFS).
     *
     * 0(2^n/2) runtime, where n is the optimal number of steps.
     * Acceptable given problem contraints (max 10^6) but could be 
     * improved with memoization and factorization tricks.
     */
    public static int downToZero(int x) {
        Map<Integer, Node> visited = new HashMap<>();
        Deque<Node> queue = new ArrayDeque<>();
        Node root = new Node(x, 0);
        queue.add(root);
        
        while(!queue.isEmpty()) {
            Node n = queue.remove();
            if(n.value == 0)
                return n.distance;
            for(Node reduction : reduce(n, visited)) {
                if(reduction == null) break;
                queue.add(reduction);
            }
        }
        throw new IllegalArgumentException("No monotonic path to zero for: " + x);
    }
    
    /* Generate the integers reduceable from this search node. 
     * Skip any integer already being searched for (aka visited). 
     */
    static List<Node> reduce(Node n, Map<Integer, Node> visited) {
        List<Integer> reductions = computeFactors(n.value);
        reductions.add(n.value - 1);
        List<Node> candidates = new ArrayList<>();
        for(Integer x : reductions) {
            if(visited.containsKey(x)) continue;
            Node r = new Node(x, n.distance + 1);
            visited.put(x, r);
            candidates.add(r);
        }
        return candidates;
    }
    /* For each factorized pair, find the larger multiplicand. */
    static List<Integer> computeFactors(int num) {
         List<Integer> factors = new ArrayList<>();
         for(int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                int multiplier = num / i;
                factors.add(Math.max(i, multiplier));
            }
        }
        return factors;
    }
}
/* Search node for a particular integer */
class Node {
    final int distance;
    final int value;
    public Node(int value, int distance) {
        this.value = value;
        this.distance = distance;
    }
}



/* Standard Hackerrank Java Boilerplate */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        int q = Integer.parseInt(bufferedReader.readLine().trim());
        IntStream.range(0, q).forEach(qItr -> {
            try {
                int n = Integer.parseInt(bufferedReader.readLine().trim());
                int result = Result.downToZero(n);
                bufferedWriter.write(String.valueOf(result));
                bufferedWriter.newLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        bufferedReader.close();
        bufferedWriter.close();
    }
}
