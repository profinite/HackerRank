import java.io.*;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Result {
    /**
     * Sum the total empty blocks within a city with arbitrary train tracks
     * https://www.hackerrank.com/challenges/gridland-metro/problem
     * 
     * Fairly simple. Here we experiment with a primitive segment
     * tree but don't really use it to full potential, since it's
     * possible to just sort the input and track the running column max.
     * 
     * @param n - city length
     * @param m - city width
     * @param k - number of train tracks
     * @param tracks - Train tracks in given format (r, c1, c2)
     * @return Sum of the empty city blocks
     */
    public static long gridlandMetro(int n, int m, int k, List<List<Integer>> tracks) {
        tracks.sort(Comparator.comparing((List<Integer> x) -> x.get(0)) // pre-sort the columns
                .thenComparing((List<Integer> x) -> x.get(1)));
        long sum = 0;
        int prev = 0; // prior row
        tracks.add(k, List.of(n + 1, 1, m)); // add termination node (row)
        SegmentTree intervals = new SegmentTree(); // to save memory, recycle the tree
        for (List<Integer> track : tracks) {
            int row = track.get(0);
            if (row > prev) {
                sum += countGaps(prev, row - 1, m);
                if(!intervals.isEmpty()) // add termination node (col)
                    intervals.add(m + 1, m + 1);
                sum += countGaps(intervals); // count the prior row
                intervals.clear();
            }
            intervals.add(track.get(1), track.get(2));
            prev = row;
        }
        return sum;
    }

    /**
     * Count the gaps in unmentioned prior rows
     * @param start - last row mentioned
     * @param end - current row
     * @param m - column width
     * @return total area
     */
    private static long countGaps(long start, long end, int m) {
        return (end - start) * m;
    }

    /**
     * Count gaps between segments
     * @param tree Segment tree
     * @return scalar area of empty segments
     */
    private static long countGaps(SegmentTree tree) {
        long sum = 0;
        for(Long end = 0L, start = tree.ceilingKey(end); start != null; start = tree.higherKey(end)) {
            sum += start - end - 1;
            end = tree.get(start);
        }
        return sum;
    }

    /**
     * Represent a primitive segment tree
     */
    private final static class SegmentTree extends TreeMap<Long, Long> {
        void add(long start, long end) {
            Long prev = floorKey(start);
            if(isOverlap(prev, start)) {
                put(prev, Math.max(end, get(prev)));
            } else {
                put(start, end);
            }
        }
        private boolean isOverlap(Long prev, long start) {
            return prev != null && get(prev) >= start;
        }
    }
}

