/**
 * Determine if all characters of the string appear the same number of times, with
 * option to remove one character (string is "valid")
 * https://www.hackerrank.com/challenges/sherlock-and-valid-string/problem
 *
 * Builds a histogram and checks if the outlier is +-1.
 * 𝚯(N) runtime for string of length N
 */
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Result {
    public static String isValid(String s) {
        Collection<Long> histogram = s.codePoints().boxed().collect(Collectors.groupingBy(x->x, Collectors.counting())).values();
        long count = histogram.stream().distinct().count();
        if (count == 1)
            return "YES";
        else if (count == 2) {
            long max = Collections.max(histogram);
            long min = Collections.min(histogram);
            if(Collections.frequency(histogram, max) == 1 && max == min + 1)
                return "YES";
            if(Collections.frequency(histogram, min) == 1 && min == 1)
                return "YES";
        }
        return "NO";
    }
}

// Omit HackerRank boilerplate
