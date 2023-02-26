import com.sun.jdi.Value;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static java.util.stream.Collectors.*;

class Result {

    /*
     * Find the longest possible substring of 's', if limited to any two characters,
     * and the terms alternate perfectly.
     * https://www.hackerrank.com/challenges/two-characters/
     *
     * Here we use successive intervals as an alternative to brute force
     *
     * Simple in theory, but the need to filter out doublets before
     * and after each term makes it truly huge and byzantine. Needs refactoring.
     *
     * 0(n) runtime
     * 0(m * n) memory complexity, where m is the alphabet of the string.
     *
     */
    public static int alternate(String s) {
        Map<Character, Set<Character>> intervals = new HashMap<>();
        Map<Character, Map<Character, Long>> observed = new HashMap<>();
        Map<Character, Long> histogram = new HashMap<>();
        boolean hasRepeat = false; // detects the trivial sequence
        for (Character c : s.toCharArray()) {
            if (observed.containsKey(c)) {
                hasRepeat = true;
                Map<Character, Long> m = observed.get(c);
                Set<Character> fresh = m.entrySet().stream()
                        .filter(a -> a.getValue() == 1) // remove doublets
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet());
                if(!intervals.containsKey(c)) { // filter out pre-observation doublets
                    Set<Character> invalid = histogram.entrySet().stream()
                            .filter(e -> e.getValue() > 2)
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toSet());
                    fresh.removeAll(invalid);
                }
                intervals.merge(c, fresh, (x, y) -> { x.retainAll(y); return x; });
            }
            // Reset the observation bin
            observed.remove(c);
            observed.values().forEach(x -> x.merge(c, 1L, Long::sum));
            observed.put(c, new HashMap<>());
            // track pre-observation doublets to invalidate
            histogram.merge(c, 1L, Long::sum);
        }
        // handle trivial sequence case
        if(!hasRepeat && s.length() > 1)
            return 2;
        intervals.values().removeIf(x -> x.size() == 0);
        // remove post-observation doublets
        for(Map.Entry<Character, Set<Character>> e : intervals.entrySet()) {
            e.getValue().removeIf(c -> {
                Character q = e.getKey();
                if(observed.containsKey(q)) {
                    return observed.get(q).getOrDefault(c, 0L) > 1;
                } else return false;
            });
        }
        intervals.values().removeIf(x -> x.size() == 0);
        return findMaxLength(intervals, s);
    }
    /* Find the longest substring and calculate its length
     * if longest is equipotent, account for the extra character */
    private static int findMaxLength(Map<Character, Set<Character>> m, String s) {
        List<Long> modes = s.codePoints()
                .mapToObj(c -> (char) c)
                .filter(m::containsKey)
                .collect(groupingBy(Function.identity(), counting()))
                .values()
                .stream()
                .collect(toList());
        long longest = modes.stream().mapToLong(x->x).max().orElse(0L);
        int minuend = 0;
        if(!isEquipotent(modes, longest) && longest > 0)
            minuend = 1;
        return Math.toIntExact(longest * 2 - minuend);
    }
    private static boolean isEquipotent(List<Long> maxima, long m) {
        return maxima.stream().filter(x -> x == m).count() > 1;
    }

}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int l = Integer.parseInt(bufferedReader.readLine().trim());

        String s = bufferedReader.readLine();

        int result = Result.alternate(s);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
