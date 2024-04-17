import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *  Generate the next-largest string of a given string 'w'
 *  using a primitive interval tree.
 *  https://www.hackerrank.com/challenges/bigger-is-greater/problem
 *
 *  𝚯(n log n), rather than 𝚯(n!) for the worst-case scenario (descending)
 *  TODO: modify to use StringBuilder or char[]
 */
public class Solution {
    public static String biggerIsGreater(String w) {
        TreeMap<Character, Integer> visited = new TreeMap<>();
        for (int i = w.length() - 1; i >= 0; i--) {
            char current = w.charAt(i);
            Map.Entry<Character, Integer> larger = visited.higherEntry(current);
            if (larger == null) {
                visited.merge(current, i, (v1, v2) -> v1); // retain lowest if identical
                continue;
            }
            String left = w.substring(0, i) + larger.getKey();
            String right = replaceAt(larger.getValue(), current, w).substring(i + 1);
            return left + sorted(right);
        }
        return "no answer";
    }
    /* Replace character 'c' at index 'i' */
    private static String replaceAt(int i, char c, String w) {
        return w.substring(0, i) + c + w.substring(i + 1);
    }
    private static String sorted(String s) {
        return s.codePoints().sorted().toString();
    }
}

// Omitting HackerRank I/O boilerplate
