import java.io.*;
import java.time.Clock;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {
    /**
     * Compute maximum after a series of summations.
     * https://www.hackerrank.com/challenges/crush/
     *
     * I. Uses a prefix sum for tractability, including the JDK's
     * rarely-seen "parallelPrefix" library to compute
     * the final summation (!).
     *
     * 𝚯(N + M) runtime complexity for theoretical input array of size N and M queries.
     * 𝚯(N) space complexity 
     *
     * @author Tyler de Laguna <tyler at delaguna.org>
     * @param n size of theoretical array
     * @param queries summation intervals on the array
     * @return maximum element of theoretical array
     * 
     * https://github.com/profinite/HackerRank
     */
    public static long arrayManipulation(int n, List<List<Integer>> queries) {
        long[] arr = new long[n];
        for (List<Integer> q : queries) {
            int left = q.get(0), right = q.get(1), summand = q.get(2);
            arr[--left] += summand; // 0-based
            if (right < n) arr[right] -= summand;
        }
        Arrays.parallelPrefix(arr, Math::addExact);
        return Arrays.stream(arr).max().orElseThrow();
    }

    /**
     * II. Alternative using a prefix interval tree
     * to minimize space complexity.
     * Optimal for sparse queries and large arrays.
     * 
     * 𝚯(M) runtime complexity for M queries 
     * 𝚯(M) space complexity (rather than |N + M|)
     */
    public static long arrayManipulation2(int n, List<List<Integer>> queries) {
        TreeMap<Integer, Long> interval = new TreeMap<>();
        for (List<Integer> q : queries) {
            int left = q.get(0), right = q.get(1); 
            long summand = q.get(2);
            interval.merge(--left, summand, Long::sum);
            interval.merge(right, -summand, Long::sum); // boundary
        }
        Long[] arr = interval.values().toArray(new Long[0]);
        Arrays.parallelPrefix(arr, Math::addExact);
        return Collections.max(Arrays.asList(arr));
    }

    /**
     * III. 'Live' alternative using a segment tree, with the values
     * immediately available (no prefix sum required). But quadratic.
     * index -> cumulative sum
     * 
     * 𝚯(M^2) runtime complexity (quadratic)
     */
    public static long arrayManipulation3(int n, List<List<Integer>> queries) {
        TreeMap<Integer, Long> tree = new TreeMap<>();
        tree.put(0, 0L);
        for (List<Integer> q : queries) {
            int left = q.get(0), right = q.get(1), summand = q.get(2);
            tree.putIfAbsent(++right, tree.lowerEntry(right).getValue()); // boundary post
            tree.putIfAbsent(left, tree.floorEntry(left).getValue());
            tree.subMap(left, right).replaceAll((x, y) -> y + summand);
        }
        return Collections.max(tree.values());
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        Clock clock = Clock.systemDefaultZone();
        long start=clock.millis();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/tmp/input.txt")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int m = Integer.parseInt(firstMultipleInput[1]);

        List<List<Integer>> queries = new ArrayList<>();

        IntStream.range(0, m).forEach(i -> {
            try {
                queries.add(
                        Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                .map(Integer::parseInt)
                                .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        long result = Result.arrayManipulation(n, queries);
        long stop=clock.millis();
        System.out.println(result);
        System.out.println((stop - (double)start) / 1000 + " sec");

        bufferedReader.close();
    }
}
