import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import java.awt.Point;
import static java.util.stream.Collectors.toList;

/**
 * The old standard "make monetary change" problem
 * https://www.hackerrank.com/challenges/coin-change/problem
 *
 * Straightforward memoization, although
 * here we parallelize the recursive calls for potential
 * 𝚯(n * m / p) runtime, for P processors, and list of length M.
 *
 * Space is also conserved by using a map rather
 * than polluting the heap with a mostly-empty array.
 */
class Result {
    private static Map<Point, Long> memo = new ConcurrentHashMap<>();
    private static List<Long> coins;
    public static long getWays(int n, List<Long> c) {
        c.sort(Comparator.reverseOrder()); // optional
        coins = c; 
        return getWays(n, 0);
    }
    private static long getWays(long n, int start) {
        Point p = new Point((int)n, start); // in lieu of Record 
        Long m = memo.get(p);
        if(m != null) return m;
        
        long current = coins.get(start);
        int next = start + 1;
        if(next == coins.size())  // base case = last coin       
            return n % current == 0 ? 1 : 0;
            
        long sum = LongStream.iterate(n, i -> i - current)
            .takeWhile(i -> i >= 0)
            .unordered()
            .parallel()
            .map(i -> i == 0 ? 1 : getWays(i, next))
            .sum(); 
        return memo.computeIfAbsent(p, k -> sum);
    }
}



// HackerRank Boilerplate
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int m = Integer.parseInt(firstMultipleInput[1]);

        List<Long> c = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Long::parseLong)
            .collect(toList());

        // Print the number of ways of making change for 'n' units using coins having the values given by 'c'
        long ways = Result.getWays(n, c);

        bufferedWriter.write(String.valueOf(ways));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
