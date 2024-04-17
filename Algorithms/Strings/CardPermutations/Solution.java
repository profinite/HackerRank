
import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {
    /**
     *  Find the sum of all potential permutations of a partially-described string, if
     *  each was ordinalized from 1-N where n = size of string.
     *  https://www.hackerrank.com/challenges/cards-permutation/problem?isFullScreen=true
     * @param x List of integers to permute
     * @return sum of the potential permutations, if ordinalized (hypothetical)
     */
    public static long solve(List<Integer> x) {
        complement = IntStream.rangeClosed(1, x.size()).boxed().collect(toList()); // make this statically managed
        x.forEach(complement::remove);        // restore to HashSet
        return solver(List.of(), x);
    }
    static List<Integer> complement;
    static public final long MOD_FACTOR = Math.round(Math.pow(10, 9)) + 7;
    public static long solver(List<Integer> pre, List<Integer> x) {
        final int n = x.size();
        if(n == 0)
            return solveIt(pre);
        int first = x.get(0);
        List<Integer> rest = x.subList(1, n);
        if(first == 0) {
            long total = 0;
            Collections.sort(complement);
            for(Integer p : new ArrayList<>(complement)) {
                complement.remove(p);
                List<Integer> fresh = Stream.concat(pre.stream(), Stream.of(p)).toList();
                long q = solver(fresh, rest) % MOD_FACTOR;
                total += q;
                total %= MOD_FACTOR;
                complement.add(p);
            }
            return total;
        }
        return solver(Stream.concat(pre.stream(), Stream.of(first)).toList(), rest);
    }

    static long solveIt(List<Integer> x) {
        List<Integer> observed = new ArrayList<>();
        long result = 1;
        int n = x.size();
        for(Integer first : x) {
            long normalized = first - observed.stream().filter(i -> i < first).count() - 1;
           // long normalized = first - 1;
            result += normalized * myfactorial(n - 1); //% MOD_FACTOR;
            n--;
            observed.add(first);
        }
        //System.err.println("Total of: " + x.toString() + " = " + result);
        return result % MOD_FACTOR;
    }
    static long additorial(long n) {
        return LongStream.rangeClosed(1, n).sum();
    }
    static Map<Integer, Long> memo = new HashMap<>();
    static long myfactorial(int n) {
        if(n == 1) return 1;
        if(n < 1) return 0;
//        if(n < 1) return 0;
        if(memo.containsKey(n))
            return memo.get(n);
        long result = n * myfactorial(n - 1) % Result.MOD_FACTOR;
        //LongStream.rangeClosed(1, n).reduce(1L, (x, y) -> x * y % MOD_FACTOR) % MOD_FACTOR;
        memo.put(n, result);
        return result;
    }
}

/* HackerRank Boilerplate */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> a = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Integer::parseInt)
                .collect(toList());

        long result = Result.solve(a);
        System.out.println(result);
        //System.out.println("All zeroes max: " + Result.additorial(Result.factorial(a.size())) % Result.MOD_FACTOR);
        //System.out.println("All filled max: " + Result.factorial(a.size()) % Result.MOD_FACTOR);
        //System.out.println("Answer: " + 402265192 % Result.MOD_FACTOR);
        bufferedReader.close();
    }
}
