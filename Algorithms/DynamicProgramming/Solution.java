import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

class Result {

    /**
     * Find the maximum possible sum within an array of both positive/negative integers
     * Here we apply a sliding window algorithm rather than memoization as the category suggests.
     * 𝚯(n) runtime
     * https://www.hackerrank.com/challenges/maxsubarray/problem
     */
    public static List<Integer> maxSubarray(List<Integer> arr) {
        IntSummaryStatistics stats = arr.stream().collect(Collectors.summarizingInt(Integer::valueOf));
        final int max = stats.getMax();
        if (max < 0) // all negative
            return List.of(max, max);
        else if (stats.getMin() >= 0) { // all positive
            int sum = (int) stats.getSum();
            return List.of(sum, sum);
        }

        int supremum = 0, tally = 0;
        for (Integer x : arr) {
            tally += x;
            if (tally < 1)
                tally = 0;
            supremum = Math.max(supremum, tally);
        }
        int positiveSum = arr.stream().filter(i -> i >= 0).reduce(0, Integer::sum);
        return List.of(supremum, positiveSum);
    }
}

// Omitting HackerRank boilerplate
