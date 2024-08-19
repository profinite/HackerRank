import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class Result {
    /**
     * Sum the total empty blocks within a city with arbitrary train tracks
     * https://www.hackerrank.com/challenges/gridland-metro/problem
     *
     * Here we present a parallelizable solution which operates over all
     * the rows concurrently. Overall mechanism experiments with a primitive segment
     * tree but don't really use it to full potential, since it's
     * possible to just sort the input and track the running column max.
     *
     * 𝚯(|V| log |V| / N) runtime for V total nodes and N rows (given N processors)
     * 𝚯(K) space
     *
     * @param n - city length
     * @param m - city width
     * @param k - number of train tracks
     * @param tracks - Train tracks in given format (r, c1, c2)
     * @return Sum of the empty city blocks
     */
    public static long gridlandMetro(int n, int m, int k, List<List<Integer>> tracks) {
        final ConcurrentHashMap<Integer, List<Track>> tracksByRow = tracks.stream().collect(
                Collectors.groupingByConcurrent(List::getFirst,
                ConcurrentHashMap::new, // slightly optimized for massive parallel reads
                Collectors.mapping(x -> new Track(x.get(1), x.get(2)), Collectors.toList()))
        );
        return IntStream.rangeClosed(1, n)
                .parallel()
                .mapToLong(i -> countGaps(i, tracksByRow, m))
                .sum();
    }
    private static long countGaps(int row, Map<Integer, List<Track>> tracks, int m) {
        return tracks.containsKey(row) ? countGaps(tracks.get(row), m) : m;
    }

    /**
     * Count gaps in the tracks for this row.
     * @param tracks - all tracks for the given row
     * @param m - row width
     * @return total area
     */
    private static long countGaps(List<Track> tracks, int m) {
        SegmentTree intervals = new SegmentTree();
        intervals.add(m + 1, m + 1); // termination node
        tracks.sort(Comparator.comparingInt(Track::start));
        tracks.forEach(intervals::add);
        return countGaps(intervals);
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
        void add(Track t) {
            add(t.start(), t.end());
        }
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
    record Track(int start, int end) {}
}

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/tmp/input.txt")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int m = Integer.parseInt(firstMultipleInput[1]);

        int k = Integer.parseInt(firstMultipleInput[2]);

        List<List<Integer>> track = new ArrayList<>();

        IntStream.range(0, k).forEach(i -> {
            try {
                track.add(
                        Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                .map(Integer::parseInt)
                                .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        long result = Result.gridlandMetro(n, m, k, track);
        System.out.println(result);

        bufferedReader.close();
    }
}
