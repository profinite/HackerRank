import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import java.awt.Point;
import static java.util.stream.Collectors.toList;

/**
 * The classic "make monetary change" problem.
 * https://www.hackerrank.com/challenges/coin-change/problem
 *
 * Straightforward memoization with a classic interview problem ⭐⭐
 * For something different, here we parallelize the recursive calls.  
 * In practice, .parallel() only seems to help starting at N > 300,000+
 *
 * Here we also opt to memoize using a hashmap. This avoids polluting 
 * the heap with a mostly-empty array. The sparsity of the memo will
 * correspond roughly to how easily divisible the target sum is.
 *
 * 𝚯(|N| * |M| / P) runtime, for P processors, and M coin options.
 */
class Result {
    private static final Map<Exchange, Long> memo = new ConcurrentHashMap<>();
    private static List<Long> coins;

    public static long getWays(int n, List<Long> c) {
        c.sort(Comparator.reverseOrder()); // optional for clarity
        coins = c;
        return getWays(n, 0);
    }

    /**
     * Recursively count potential coin exchanges.
     *
     * @param n summand to exchange with coins
     * @param start index into the coin list
     * @return total of combinations to exchange
     */
    private static long getWays(long n, int start) {
        var x = new Exchange(n, start);
        Long m = memo.get(x);
        if (m != null) return m;

        long current = coins.get(start);
        int next = start + 1;
        if (next == coins.size())  // base case is the last coin
            return n % current == 0 ? 1 : 0;

        // otherwise, pursue the recurrence relation
        long sum = LongStream.iterate(n, i -> i - current)
                .takeWhile(i -> i >= 0)
                .unordered()
                .parallel() // concurrency experiment
                .map(i -> i == 0 ? 1 : getWays(i, next))
                .sum();
        // store the result as a future subcomputation
        return memo.computeIfAbsent(x, k -> sum);
    }
    record Exchange(long summand, int index) { }
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
