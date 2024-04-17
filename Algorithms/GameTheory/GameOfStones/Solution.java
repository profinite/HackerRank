import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

class Result {
    /* Brute-force solution
     * 𝚯(3^n) runtime
     * Optionally parallelizable and apply dynamic programming (DP)
     * https://www.hackerrank.com/challenges/game-of-stones-1/problem
     */
    public static String gameOfStones(int n) {
        if (isWinning(n)) return "First";
        return "Second";
    }
    static private boolean isWinning(int n) {
        final Stream<Integer> subtrahends = Stream.of(5, 3, 2).parallel();
        final int baseCase = 1; // losing
        if(n <= baseCase)
            return false;
        if(!memo.containsKey(n)) // prefer memo vs searching
            memo.put(n, !subtrahends.map(i -> n - i).allMatch(Result::isWinning));
        return memo.get(n);
    }
    static final Map<Integer, Boolean> memo = new ConcurrentHashMap<>();
}




// HackerRank I/O Boilerplate
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int t = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, t).forEach(tItr -> {
            try {
                int n = Integer.parseInt(bufferedReader.readLine().trim());

                String result = Result.gameOfStones(n);

                bufferedWriter.write(result);
                bufferedWriter.newLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
        bufferedWriter.close();
    }
}
