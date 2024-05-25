import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {

    /**
     * Find the first string within a list which creates a prefix
     * relationship with any other string.
     * https://www.hackerrank.com/challenges/one-week-preparation-kit-no-prefix-set/p
     *
     * Applies a sorted map with Java 17. Reporting the first occurrence is
     * rather troublesome: This requires tracking the index of
     * each prefix relationship and making a second pass.
     *
     * 𝚯(n log n) or higher runtime
     * 𝚯(n) space complexity (red-black tree)
     */
    public static void noPrefix(List<String> words) {
       TreeMap<String, Integer> sorted = new TreeMap<>();
       int min = Integer.MAX_VALUE; // index of first observable prefix
       int index = 0;
       for (String word : words) {
            if (sorted.containsKey(word)) // check for identical
                min = Math.min(min, index);
            sorted.putIfAbsent(word, index); // sort the strings
            index++;
        }
        for (String word : words.stream().distinct().toList()) {
            String next = sorted.higherKey(word);
            while (isPrefix(word, next)) {
                int last = Math.max(sorted.get(word), sorted.get(next));
                min = Math.min(min, last);
                next = sorted.higherKey(next);
            }
        }
        if(min != Integer.MAX_VALUE)
            System.out.println("BAD SET\n" + words.get(min));
        else
            System.out.println("GOOD SET");
    }
    static boolean isPrefix(String pre, String full) {
        return pre != null && full != null && full.startsWith(pre);
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/tmp/input.txt")));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> words = IntStream.range(0, n).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(toList());

        Result.noPrefix(words);

        bufferedReader.close();
    }
}
