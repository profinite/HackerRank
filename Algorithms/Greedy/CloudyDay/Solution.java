import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class Result {
    /**
     * Return the maximum number of people that will be in a sunny town after
     * removing exactly one cloud.
     * https://www.hackerrank.com/challenges/cloudy-day/
     *
     * Fairly simple implementation problem.
     * 1) Represent each town's location in a Binary Search Tree or Skiplist.
     *  If towns share a location, simply treat them as combined.
     *
     * 2) For each cloud, assign towns within range to it:
     *  Town Location -> Cloud
     * Important: remove any towns already covered by this cloud. This keeps
     * the algorithm runtime linear.
     *
     * 3) Now for each town, group their populations by the cloud that covers them.
     *
     * 4) Sum the largest group with the uncovered "SUNNY" towns
     *
     * @param p - town populations
     * @param x - town locations
     * @param y - cloud locations
     * @param r - cloud range
     * @return sum of people in a sunny town after maximal cloud is removed
     */
    public static long maximumPeople(List<Long> p, List<Long> x, List<Long> y, List<Long> r) {
        final Cloud NONE = new Cloud(0, 0, -1L);
        // 1) Town -> Cloud
        ConcurrentSkipListMap<Long, Cloud> cover = rangeOf(x.size())
                .collect(Collectors.toMap(x::get, a -> NONE, (a, b) -> a, ConcurrentSkipListMap::new));

        // 2) for each cloud, assign any virgin towns to it. Remove those already covered.
        for (Cloud c : rangeOf(y.size()).map(i -> new Cloud(y.get(i), r.get(i), i)).toList()) {
            cover.subMap(c.start(), c.end()).forEach((a, b) -> {
                if (b == NONE) cover.put(a, c);
                else cover.remove(a);
            });
        }

        // Sum the populations for each cloud.
        Map<Long, Long> popByLocation = rangeOf(x.size()).collect(Collectors.toMap(x::get, p::get, Long::sum));
        Map<Cloud, Long> popByCloud = cover.entrySet().stream()
                .collect(Collectors.groupingByConcurrent(
                    Map.Entry::getValue,
                    Collectors.summingLong(e -> popByLocation.get(e.getKey())))
                );

        // 3) Now find the cloud with the largest population
        long largest = popByCloud.values().stream().max(Comparator.comparingLong(e -> e)).orElse(0L);

        // 4) Sum it with uncovered towns, if any
        Long uncovered = popByCloud.remove(NONE);
        if(uncovered != null)
            largest += uncovered;
        return largest;
    }
    private record Cloud(long location, long range, long name) {
        long start() { return location - range; }
        long end() { return location + range + 1; } // exclusive
    }
    // helper function for a parallel stream
    private static Stream<Integer> rangeOf(int n) {
        return IntStream.range(0, n).parallel().boxed();
    }


    // For pre-Records java versions
    public static long maximumPeople2(List<Long> p, List<Long> x, List<Long> y, List<Long> r) {
        final long SUNNY = -1L;
        ConcurrentSkipListMap<Long, Long> cover = rangeOf(x.size())
                .collect(Collectors.toMap(x::get, a -> SUNNY, (a, b) -> a, ConcurrentSkipListMap::new));
        for (int i = 0; i < y.size(); i++) {
            Long start = y.get(i) - r.get(i);
            Long end = y.get(i) + r.get(i) + 1;
            long cloud = i;
            cover.subMap(start, end).forEach((a, b) -> { if(b == -1) cover.put(a, cloud); else cover.remove(a); });
        }
        Map<Long, Long> populations = rangeOf(x.size()).collect(Collectors.toMap(x::get, p::get, Math::addExact));
        Map<Long, Long> popByCloud = cover.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.summingLong(e -> populations.get(e.getKey()))));
        Long uncovered = Optional.ofNullable(popByCloud.remove(SUNNY)).orElse(0L);
        Long largest = popByCloud.values().stream().max(Comparator.comparingLong(e -> e)).orElse(0L);
        return uncovered + largest;
    }
}


public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/tmp/input.txt")));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<Long> p = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Long::parseLong)
                .collect(toList());

        List<Long> x = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Long::parseLong)
                .collect(toList());

        int m = Integer.parseInt(bufferedReader.readLine().trim());

        List<Long> y = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Long::parseLong)
                .collect(toList());

        List<Long> r = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Long::parseLong)
                .collect(toList());

        long result = Result.maximumPeople(p, x, y, r);

        System.out.println(result);

        bufferedReader.close();
    }
}
