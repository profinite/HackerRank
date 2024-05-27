import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toList;

/** Count maximal palindromes in the given string S[l] to S[r], 1-indexed.
 *
 * Applies combinatorics principles:
 * 1) (n0 * N!) / (n1! * n2! ... ) for ni identical elements.
 * 2) Modular inverse to allow modular division
 * 3) Segment tree to count character frequencies upfront
 * 4) Uses over/underflow-safe routines to detect truncation errors
 * 5) Precompute smaller factorials upfront
 * 6) Memoize large factorials via interval tree ad-hoc
 *
 * 0(n log n) for string of length 'n'
 */

class Result {
    static SegmentTree tree; // concurrent-safe for this usage
    static Factorial factorials = new Factorial();
    final static int MODULUS = 1_000_000_007;
    /* Precompute a Segment Tree of the string. 
     * https://en.wikipedia.org/wiki/Segment_tree */
    public static void initialize(String s) {
        tree = new SegmentTree(s);
    }

    public static int answerQuery(int l, int r) {
        final List<Long> histogram = tree.interval(l, r);

        // Find the total permutations possible, n! * |singletons|
        List<Long> majorant = histogram.stream().filter(c -> c > 1).map(c -> c / 2).collect(toList());
        long multiCount = majorant.stream().reduce(0L, Math::addExact);
        long singleCount = histogram.stream().filter(k -> k % 2 == 1).count(); // singletons
        long total = safeMultiply(Math.max(singleCount, 1), factorials.get(multiCount)); // special case: even-length palindromes

        // Adjust for the overcounted identical characters  (n1! * n2!...)
        long overCount = majorant.stream().map(Result.factorials::get).reduce(1L, Result::safeMultiply);
        overCount = inverseOf(overCount);
        return Math.toIntExact(safeMultiply(total, overCount));
    }
    /* Apply modular inverse - "Fermat's Little Theorem" */
    static long inverseOf(long orig) {
        BigInteger o = BigInteger.valueOf(orig);
        o = o.modPow(BigInteger.valueOf(MODULUS - 2), BigInteger.valueOf(MODULUS));
        o = o.mod(BigInteger.valueOf(MODULUS));
        return o.longValueExact();
    }
    public static long safeMultiply(long x, long y) {
        return Math.multiplyExact(x, y) % MODULUS;
    }

}
/* Memoize factorials "ad-hoc" in segment tree to conserve heap space,
*  rather than pre-compute */
class Factorial {
    private final TreeMap<Long, Long> memo = new TreeMap<>(); // Concurrent-safe in this context
    public Factorial() {
        memo.put(0L, 1L);
        memo.put(1L, 1L);
    }

    /* Obtain large factorials with interval tree. */
    public long get(long x) {
        Map.Entry<Long, Long> e = memo.floorEntry(x);
        long start = e.getKey();
        long fresh = LongStream.rangeClosed(++start, x).reduce(e.getValue(), Result::safeMultiply);
        memo.put(x, fresh);
        return fresh;
    }   
}

/**
 * Segment tree to represent a sparse histogram of a string.
 *  Character -> (left index -> prefix sum), 1-based
 * -------------------------------
 * Example: panaman is represented as:
 *      p -> (0 -> 0)
 *      a -> (1 -> 1, 3 -> 2, 4 -> 3)
 *      n -> (2 -> 1, 6 -> 2)
 *      m -> (5 -> 1)
 * -------------------------------
 */
class SegmentTree {
    Map<Character, Segment> counts = new HashMap<>();
    public SegmentTree(String s) {
        int index = 1;
        for(Character c : s.toCharArray()) {
            Segment count = counts.computeIfAbsent(c, k -> new Segment());
            count.put(index, count.floorEntry(index).getValue() + 1);
            index++;
        }
    }
    /** Get a histogram of total character frequency, from start to end index, 1-based. */    
    public List<Long> interval(int start, int end) {
        return counts.values()
                .stream()
                .map(h -> compute(h, start, end))
                .collect(toList());
    }
    private Long compute(TreeMap<Integer, Long> arr, int start, int end) {
        long diff = arr.floorEntry(end).getValue() - arr.floorEntry(--start).getValue();
        return Math.max(0, diff);
    }
    /** Segment is Map<index, frequency>
     *  where frequency = counts up to a given index, from left to right) */
    static final class Segment extends TreeMap<Integer, Long> { { this.put(0, 0L); } };    
}



/* HackerRank Boilerplate */
public class Solution {
    public static void main(String[] args) throws IOException {
        long time = System.currentTimeMillis();
        String inputPath = "/tmp/input.txt";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputPath));
        String filePath = "/tmp/output.txt";
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/tmp/myoutput.txt"));

        BufferedReader verify = new BufferedReader(new FileReader(filePath));
        Deque<Integer> correct = verify.lines().map(Integer::parseInt).collect(Collectors.toCollection(ArrayDeque::new));

        String s = bufferedReader.readLine();

        Result.initialize(s);

        int q = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, q).forEach(qItr -> {
            try {
                String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

                int l = Integer.parseInt(firstMultipleInput[0]);

                int r = Integer.parseInt(firstMultipleInput[1]);

                int result = Result.answerQuery(l, r);

                if(result != correct.getFirst())
                    throw new IllegalStateException(STR."Error: result: \{result} should be: \{correct.getFirst()} at \{qItr}");
                correct.pop();

                bufferedWriter.write(String.valueOf(result));
                bufferedWriter.newLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
        bufferedWriter.close();
        System.err.println("Took: "  + (System.currentTimeMillis() - time) + " ms");
    }
}
