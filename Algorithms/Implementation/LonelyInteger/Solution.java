import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * https://www.hackerrank.com/challenges/lonely-integer/
     *
     * Simple problem not worth adding but capturing my implementation
     * with a Bloom Filter for fun. Bloom Filter is 100% inappropriate here
     * since it's easily solved with a XOR, and requires removing duplicates.
     *
     * 0(N^2) - since we remove duplicates. XOR is better 0(N)
     */
     static class BloomFilter {
        final BitSet observed;
        final List<Function<Integer, Integer>> hashes = Arrays.asList(
            i -> i.hashCode(),
            i -> String.valueOf(i).length());
        public BloomFilter(int size) {
            observed = new BitSet(size);
        }
        void add(int x) {
            int n = observed.size();
             for(Function<Integer, Integer> f : hashes) { 
                 int hash = f.apply(x); 
                 observed.set(Math.abs(hash) % n, true);
             }
        }
        boolean contains(int x) {
            int n = observed.size();
            for(Function<Integer, Integer> f : hashes) { 
                int hash = f.apply(x);
                if(!observed.get(Math.abs(hash) % n))
                    return false;
            }
            return true;
        }
     }
    public static int lonelyinteger(List<Integer> a) {
        BloomFilter filter = new BloomFilter(BigInteger.probablePrime(8, new Random()).intValue());
        for(Integer i : new ArrayList<>(a)) {
            if(filter.contains(i)) {
                a.remove(i); // HACK: 0(n) operation
                a.remove(i);
            }
            filter.add(i);
        }
        return a.get(0);
    }

}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> a = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(toList());

        int result = Result.lonelyinteger(a);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
