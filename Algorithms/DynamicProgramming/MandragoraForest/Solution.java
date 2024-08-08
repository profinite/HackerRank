import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

class Result {

    /*
     * Simulate a battling game:
     * https://www.hackerrank.com/challenges/mandragora/problem
     *
     * Excellent problem! ⭐⭐⭐⭐⭐
     * Good 30min interview question.
     * 
     * Applies sliding window to precompute battling every round
     * then step through building health. Rests on assumption
     * that best to build health only on low-experience opponents.
     *
     * 𝚯(N log N) runtime for N opponents due to sort
     */
     public static long mandragora(List<Integer> H) {
        Collections.sort(H);
        long battles = H.stream().mapToLong(Long::valueOf).sum();
        long max = 0;
        for (int i = 0; i < H.size(); battles -= H.get(i), i++) {
            int health = i + 1;
            long experience = health * battles;
            if (experience < max) return max;
            max = experience;
        }
        return max;
    }
    /* Alternative brute-force solution, albeit parallelized. */
     public static long mandragora2(List<Integer> H) {
        final int n = H.size();
        Collections.sort(H);
        return IntStream.range(0, n).parallel().mapToLong(i -> (long)H.subList(i, n).stream().mapToLong(Long::valueOf).reduce(0L, Long::sum) * ++i).max().orElseThrow();
    }
    /* More experimentation with the alternative. */
    public static long mandragora2(List<Integer> H) {
        final int n = H.size();
        Collections.sort(H);
        return IntStream.range(0, n).mapToLong(i -> (long) H.subList(i, n).stream().reduce(0, Integer::sum) * ++i).max().orElseThrow();

    }

}
