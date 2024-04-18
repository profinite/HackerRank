import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;

/** 
 * Several Easy to Medium algorithm problems from HackerRank 
 *  
 * Most of these were fairly self-evident, but a few good
 * problem in here to remember.
 *
 *
 * */
public class Main {
    public static void main(String[] args) {
        System.out.println(minimumAbsoluteDifference(List.of(1, -3, 71, 68, 17)));
    }
    public static int chocolateFeastLoop(int n, int c, int m) {
        int totalBars = n / c;
        int bars = totalBars;
        int wrappers = bars;
        while( bars > 0 ) {
            bars = wrappers / m;
            wrappers = wrappers % m + bars;
            totalBars += bars;
        }
        return totalBars;
    }

    public static int minimumAbsoluteDifference2(List<Integer> arr) {
        int minDiff = Integer.MAX_VALUE;
        Collections.sort(arr);
        for(int i = 0; i < arr.size() - 1; i++) {
            int diff = Math.abs(arr.get(i) - arr.get(i + 1));
            minDiff = Math.min(minDiff, diff);
        }
        return minDiff;
    }
    public static int minimumAbsoluteDifference(List<Integer> arr) {
        Collections.sort(arr);
        return IntStream.range(0, arr.size() - 1)
                .parallel()
                .map(i -> arr.get(i) - arr.get(i + 1))
                .map(Math::abs)
                .reduce(Integer.MAX_VALUE, Math::min);
    }

        public static int chocolateFeast(int n, int c, int m) {
            int bars = n / c; // initial bars bought with money
            double totalBars = bars / (1d - (1d / m)); // geometric series summation
            return (int)Math.ceil(totalBars - 1);
            // https://en.wikipedia.org/wiki/Geometric_series
    }
    public static int libraryFine(int d1, int m1, int y1, int d2, int m2, int y2) {
        if (y1 > y2)
            return 10000;
        else if (y1 == y2) {
            if (m1 > m2)
                return (m1 - m2) * 500;
            else if (m1 == m2 && d1 > d2)
                return (d1 - d2) * 15;
        }
        return 0;
    }
    public static long marcsCakewalk(List<Integer> calorie) {
        Collections.sort(calorie);
        Collections.reverse(calorie);
        return IntStream.range(0, calorie.size())
                .parallel()
                .mapToLong(i -> seriesAt(i) * calorie.get(i))
                .sum();
    }
    private static long seriesAt(int n) {
        return Math.round(Math.pow(2, n));
    }
    public static int saveThePrisoner(int n, int m, int s) {
        //int madj = m - (n - (s - 1));
        m -= (n - (s - 1));
        int p = m % n;
        if (p < 0) return n;
        return p;
    }
    /* Get random integer within inclusive range. */
    private static int getRandFrom(int min, int max) {
       return new Random().ints(1, min, max + 1).findFirst().orElse(0);
    }
    static int getMinimumCost(int k, int[] c) {
        int sum = 0;
        Arrays.sort(c);
        for(int d = 1, j = 0; d < Integer.MAX_VALUE; d++) {
            for(int i = 0; i < k; i++, j++) {
                if(j >= c.length)
                    return sum;
                sum += d * c[j];
            }
        }
        return sum;

    }

}
class Result {

    public static List<Integer> icecreamParlor(int m, List<Integer> arr) {
        Map<Integer, Integer> diffs = range(0, arr.size()).boxed()
                .collect(Collectors.toConcurrentMap(i->arr.get(i) - m, Function.identity(), (x, y) -> y));
        return range(0, arr.size()).filter(i -> diffs.containsKey(-arr.get(i)))
                .mapToObj(i -> List.of(i + 1, diffs.get(-arr.get(i)) + 1)).distinct().findFirst().orElse(List.of(-1));
    }
  
    public static String happyLadybugs(String b) {
        Map<String, Long> histogram = b.codePoints().boxed()
                    .collect(Collectors.groupingBy(Character::toString, Collectors.counting()));
            //if(!histogram.containsKey("_"))
            //    return "NO"
            if(histogram.remove("_") == null) {
                if(Stream.of(b.split("(?<=(.))(?!\\1)")).map(String::length).anyMatch(i -> i == 1)) {
                    return "NO";
                }
            }
            if(histogram.values().stream().anyMatch(i -> i == 1))
                return "NO";
            return "YES";
    }
  
    // permute two arrays
    public static String twoArrays(int k, List<Integer> A, List<Integer> B) {
        Collections.sort(A);
        Collections.sort(B);
        Collections.reverse(B);
        return IntStream.range(0, A.size()).allMatch(i -> A.get(i) + B.get(i) >= k) ? "YES" : "NO";
    }

    public static String balancedSums(List<Integer> arr) {
        Integer[] ar = arr.toArray(new Integer[0]);
        if(ar.length == 1) return "YES";
        if(ar.length == 0) return "NO"; // maybe
        int leftSum = 0;
        int rightSum = arr.stream().reduce(0, Integer::sum);
        for(int i = 0; i < ar.length; i++) {
            if(i > 0)
                leftSum += ar[i - 1];
            rightSum -= ar[i];
            if(leftSum == rightSum)
                return "YES";
            if(leftSum > rightSum)
                break;
        }
        return "NO";
    }
    public static int lonelyinteger(List<Integer> a) {
        int sum = a.stream().reduce(0, Integer::sum);
        int sumDistinct = a.stream().distinct().reduce(0, Integer::sum);
        return 2 * sumDistinct - sum;
    }

        public static long strangeCounter(long t) {
        long n = cycleAtTime(t);
        long baseValue = valueAtCycle(n);
        long timeAtCycle = baseValue - 2;
        long postCycleDelta = t - timeAtCycle;
        return baseValue - postCycleDelta;
    }
    static long valueAtCycle(long n) {
        return Math.round(3 * Math.pow(2, n));
    }
    static long cycleAtTime(long t) {
        return Math.round(Math.floor(log2((t + 2)/ 3)));
    }
    static double log2(long x) {
        return Math.log(x) / Math.log(2) + 1e-10;
    }
    public static List<Integer> findSumPair(List<Integer> list, int m) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            int complement = m - list.get(i);
            if (seen.containsKey(complement)) {
                return Arrays.asList(seen.get(complement), i + 1);
            }
            seen.put(list.get(i), i + 1);
        }
        return null; // No pair found
    }


    public static void insertionSort1(int n, List<Integer> arr) {
        for (int i = arr.size() - 1; i >= 0; i--) {
            int current = arr.get(i);
            int next = arr.get(i - 1);
            if (next > current) {
                arr.set(i, next);
                showList(arr);
                arr.set(i - 1, current);
            } else {
                break;
            }
        }
        showList(arr);
    }
        private static void showList(List<Integer> arr) {
            System.out.println(arr.stream().map(String::valueOf).collect(Collectors.joining(" ")));
        }
}
