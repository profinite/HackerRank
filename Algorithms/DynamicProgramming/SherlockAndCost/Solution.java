import com.sun.source.tree.Tree;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class Result {

    /**
     * Find the maximum potential difference within a series, given maxima
     * https://www.hackerrank.com/challenges/sherlock-and-cost/
     *
     * I think of this problem as a wave and we've trying to find the
     * maximum "pulse" at each cycle. Initially I tried simply alternating pulses
     * but there are many scenarios where a double low or high is optimal.
     *
     * So I just search for the maximum, taking the given maximum or minimum (1).
     * It's easy to memoize which makes it more tractable.
     * 
     *  𝚯(N) for list of length N, with memoization
     *  𝚯(N) space complexity for the memos
     *
     * @param B  maxima of the potential series
     * @return maximum difference sum
     */

    static Map<List<Integer>, Integer> memo = new HashMap<>();
    private final static int LOWEST = 1;
    public static int cost(List<Integer> B) {
        memo.clear();
        return Math.max(pulseDiff(B, B.get(0), 1),
                pulseDiff(B, LOWEST,1));
    }
    private static int pulseDiff(List<Integer> B, int previous, int index) {
        if(index == B.size())
            return 0;
        int highest = B.get(index);
        return Math.max(nextPulse(B, previous, highest, index), 
                nextPulse(B, previous, LOWEST, index));
    }
    // Helper function
    private static int nextPulse(List<Integer> B, int previous, int current, int index) {
        int diff = Math.abs(previous - current);
        List<Integer> key = Arrays.asList(current, index);
        if(memo.containsKey(key))
            diff += memo.get(key);
        else {
            int sum = pulseDiff(B, current, index + 1); 
            memo.put(key, sum);
            diff += sum;
        }
        return diff;
    }
}

public class Test {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/tmp/input.txt")));
        //BufferedReader answers = new BufferedReader(new InputStreamReader(new FileInputStream("/tmp/output.txt")));
        //List<Integer> results = answers.lines().map(Integer::parseInt).toList();

        int t = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, t).forEach(tItr -> {
            try {
                int n = Integer.parseInt(bufferedReader.readLine().trim());

                List<Integer> B = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .collect(toList());

                    int result = Result.cost(B);
                    System.out.println(result);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
    }
}

/* Bump the Stack call maximum to head off stack overflow for case #12 */
class Solution implements Runnable {
    public static void main(String[] args) {
        new Thread(null, new Solution(), "morecalls", 1<<26).start();
    }
    public void run() {
        String[] args = {"", ""};
        try {
            SolutionPrimary.main(args);
        } catch (IOException e) {
            throw new IllegalArgumentException("bad IO");
        }
    }
}
class SolutionPrimary {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int t = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, t).forEach(tItr -> {
            try {
                int n = Integer.parseInt(bufferedReader.readLine().trim());

                List<Integer> B = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .collect(toList());

                int result = Result.cost(B);

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
