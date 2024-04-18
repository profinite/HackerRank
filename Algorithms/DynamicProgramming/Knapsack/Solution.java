import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import static java.util.stream.Collectors.toCollection;


/** 
 * Find a submaximal sum of within the array nearest 'k' 
 * @param given constant 'k' to match
 * @param array of postive integers.
 * https://www.hackerrank.com/challenges/unbounded-knapsack/problem
 *
 * Similar to Knapsack problem though repeated elements.
 * We store the factors of K in a Skip List for faster retrieval. 
 *
 * 𝚯(N + K) runtime for N array elements and target sum K
 *
 * Cleanly parallelizes to 𝚯(N) with 'K' threads in theory
 */
class Result {
    public static int unboundedKnapsack(int k, List<Integer> arr) {
        SkipList quotients = factorsOf(k, arr);
        int residue = quotients.stream()
                .parallel()
                .mapToInt(i -> residueOf(quotients, k % i))
                .min().orElse(k);
        return k - residue;
    }
    static int residueOf(SkipList quotients, int k) {
        for(Integer a = quotients.floor(k); a != null; a = quotients.floor(k)) {
            k %= a;
            if(k == 0) break; // optional
        }
        return k;
    }

    /* Expand and condense the quotient list into potential subfactors of 'k' */
    private static SkipList factorsOf(int k, Collection<Integer> arr) {
        arr.add(0);
        return arr.stream()
                .parallel()
                .flatMap(a -> arr.stream().map(b -> a + b))
                .filter(a -> a <= k)
                .filter(a -> arr.stream().noneMatch(b -> isFactor(a, b)))
                .collect(toCollection(SkipList::new));
    }
    private static boolean isFactor(int a, int b) {
        if(b == 0 || a == b)
            return false;
        return a % b == 0;
    }
    public static final class SkipList extends ConcurrentSkipListSet<Integer> {};
}
class Solution {
    public static void main(String[] args) throws IOException {
        File file = new File("/tmp/old.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        Scanner scanner = new Scanner(file); 
        int t = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?"); // workaround

        for (int j = 0; j < t; ++j) { // That's what is missing
            String[] nk = scanner.nextLine().split(" ");

            int n = Integer.parseInt(nk[0]);

            int k = Integer.parseInt(nk[1]);

            ArrayList<Integer> arr = new ArrayList<>();

            String[] arrItems = scanner.nextLine().split(" ");
            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

            for (int i = 0; i < n; i++) {
                int arrItem = Integer.parseInt(arrItems[i]);
                arr.add(arrItem);
            }

            int result = Result.unboundedKnapsack(k, arr);
            System.out.println(result);

            bufferedWriter.write(String.valueOf(result));
            bufferedWriter.newLine();

        }
        bufferedWriter.close();
        scanner.close();
    }
}
