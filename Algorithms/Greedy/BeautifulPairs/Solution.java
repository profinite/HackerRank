import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toList;

class Result {
/**
 * Change one element in an array to make it a "beautiful set" 
 * A pair of indices  is beautiful if the  element of array  is equal to the  element of array.
 * https://www.hackerrank.com/challenges/beautiful-pairs/problem
 * 
 * Fairly straightforward... good implementation problem. ⭐⭐⭐
 * Builds a histogram of each element, then counts the
 * the number of prospective pairs, with tolerance for one change.
 */

    public static int beautifulPairs(List<Integer> A, List<Integer> B) {
        if(A.size() == 1) return 0;
        Map<Integer, Long> pairs = B.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        int pairCount = 0;
        for(Integer x : A) {
            if(pairs.getOrDefault(x, 0L) > 0) {
                pairCount++;
                pairs.compute(x, (k, v) -> v - 1L);
            }
        }
        if(pairs.values().stream().allMatch(x -> x == 0L))
            return pairCount - 1;
        return pairCount + 1;
    }

}


// HackerRank Boilerplate
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("/tmp/input.txt"));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> A = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Integer::parseInt)
                .collect(toList());

        List<Integer> B = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Integer::parseInt)
                .collect(toList());

        int result = Result.beautifulPairs(A, B);
        System.out.println(result);

        bufferedReader.close();
    }
}
