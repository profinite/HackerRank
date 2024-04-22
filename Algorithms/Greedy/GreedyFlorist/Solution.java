import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class Main {
    /**
     * Minimize cost given 'k' buyers and 'c' prices of items where
     * costs multiply for prior buyers
     * https://www.hackerrank.com/challenges/greedy-florist/problem
     *
     * Key observations:
     * Just purchase the most expensive flowers in the first buying round,
     * the next in the second, and so on.
     *
     * 𝚯(n log n) for n prices
     *
     * @param k number of buyers
     * @param prices unsorted list of prices
     * @return optimal cost
     */
    static int getMinimumCost(int k, List<Integer> prices) {
        final int ROUNDS = divideUp(prices.size(), k);
        prices.sort(Comparator.reverseOrder());
        return IntStream.range(0, ROUNDS).map(r -> costOf(r, k, prices)).sum();
    }

    /** 
     * @param round - current buying cycle
     * @param k - total buyers per round
     * @return cost for this round
     */
    private static int costOf(int round, int k, List<Integer> prices) {
        int start = round * k;
        int end = Math.min(start + k, prices.size()); // account for last round
        return ++round * prices.subList(start, end).stream().reduce(0, Integer::sum);
    }
    private static int divideUp(int x, int y) {
       return (int) Math.ceil((double) x / y);
    }
    
    
    /* HackerRank Boilerplate. */
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        String[] nk = scanner.nextLine().split(" ");
        int n = Integer.parseInt(nk[0]);
        int k = Integer.parseInt(nk[1]);
        List<Integer> c = new ArrayList<>();
        String[] cItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
        for (int i = 0; i < n; i++) {
            int cItem = Integer.parseInt(cItems[i]);
            c.add(cItem);
        }
        int minimumCost = getMinimumCost(k, c);
        bufferedWriter.write(String.valueOf(minimumCost));
        bufferedWriter.newLine();
        bufferedWriter.close();
        scanner.close();
    }
}
