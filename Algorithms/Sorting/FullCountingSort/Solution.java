import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.*;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toList;

class Result {
    /*
     * Perform a stable 'counting sort' on the given string,
     * As an extra quirky condition, replace the first half of the
     * input with a hyphen (-) character.
     * https://www.hackerrank.com/challenges/countingsort4/
     *
     * NOTES: Here, we use a stable Map (LinkedHashMap)
     * to store the counting results. The Map must be
     * pre-initialized with the counting 'alphabet' to
     * maintain its ordering.
     *
     * 0(n + m), where m is the 'alphabet' of the string, 'n' is length.
     * 0(m) space complexity
     */
    public static void countSort(List<List<String>> arr) {
        final int MAX_KEY = 100;
        final int mid = arr.size() / 2;
        Map<Number, String> sorted = toCountableMap(MAX_KEY);

        arr.subList(0, mid).forEach(a -> a.set(1, "-")); // hyphen req
        arr.forEach(a -> count(sorted, a.get(0), a.get(1)));
        System.out.println(String.join("", sorted.values()).trim());
    }
    /* Pre-initialize the linked map to preserve stable counting */
   private static void count(Map<Number, String> count, String key, String clause) {
        int i = Integer.parseInt(key);
        count.merge(i, clause, (a, b) -> a + " " + b);
   }
   private static Map<Number, String> toCountableMap(int max) {
        return IntStream.rangeClosed(0, max).boxed()
           .collect(toMap(x->x, x->"", (a, b)->a, LinkedHashMap::new));
   }
}


public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<List<String>> arr = new ArrayList<>();

        IntStream.range(0, n).forEach(i -> {
            try {
                arr.add(
                        Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        Result.countSort(arr);

        bufferedReader.close();
    }
}
