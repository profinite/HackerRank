
import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {

    /**
     * Calculate the smallest possible loss over a series o prics.
     * https://www.hackerrank.com/challenges/minimum-loss/problem
     * 
     * Applies a search tree to store the nearest potential sale price.
     * 𝚯(n log n) runtime
     *
     */
    public static int minimumLoss(List<Long> prices) {
        TreeSet<Long> history = new TreeSet<>();
        long min = Long.MAX_VALUE;
        for(Long price : prices) {
            Long higher = history.ceiling(price);
            if(higher != null)
                min = Math.min(min, higher - price);
            history.add(price);
        }
        return Math.toIntExact(min);
    }

}


// HackerRank boilerplate
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<Long> price = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Long::parseLong)
                .collect(toList());

        int result = Result.minimumLoss(price);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
