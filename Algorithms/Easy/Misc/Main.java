
import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

import static java.lang.Math.toIntExact;
import static java.util.Comparator.comparingInt;
import static java.util.Comparator.comparingLong;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;

class Main {
    public static void main(String[] args) {
       System.out.println(Result.weightedUniformStrings("abbcccdddd", List.of(1, 4, 7, 3, 16)));
    }
}

/** A few dozen miscellaneous 'Easy' problems bundled into one file. */
class Result {
    public static int towerBreakers(int n, int m) {
        if(n % 2 == 0 || m == 1)
            return 2;
        return 1;
    }
    public static String pokerNim(int k, List<Integer> c) {
        if(c.stream().reduce(0, (x, y) -> x^y) == 0) return "Second";
        return "First";
    }

    public static long sumXor(long n) {
        int zeroCount = 0;
        while(n > 0) {
            if((n & 1L) == 0)
                zeroCount++;
            n >>= 1;
        }
        return Math.round(Math.pow(2, zeroCount));
    }

public static String nimbleGame(List<Integer> s) {
        int xor = 0;
        int square = 0;
        for (Integer n : s) {
            if (n % 2 == 1)
                xor ^= square;
            square++;
        }
        if (xor > 0) return "First";
        return "Second";
    }
    public static String misereNim(List<Integer> s) {
        boolean allOnes = s.stream().allMatch(i -> i == 1);
        boolean won = isWinnableNim(s);
        if(allOnes)
            won = !won;
        return won ? "Second" : "First";
    }
    private static boolean isWinnableNim(List<Integer> pile) {
        int binSum = 0;
        for(Integer stones : pile)
            binSum ^= stones;
        return binSum == 0;
    }

        public static String nimGame(List<Integer> pile) {
        int binSum = 0;
        for(Integer stones : pile)
            binSum ^= stones;

        return binSum == 0 ? "Second" : "First";
    }
    public static String gamingArray(List<Integer> arr) {
        int moves = 0;
        for(int i = 0, max = 0; i < arr.size(); i++) {
            int current = arr.get(i);
            if(current > max) {
                moves++;
                max = current;
            }
        }
        System.out.println(moves);
        return List.of("ANDY", "BOB").get(moves % 2);
    }
    public static List<String> weightedUniformStrings(String s, List<Integer> queries) {
        Set<Long> weights = new HashSet<>();
        Character prev = s.charAt(0);
        int count = 0;
        for(Character c : s.toCharArray()) {
            if(c.equals(prev)) {
                count++;
            } else {
                count = 1;
                prev = c;
            }
            weights.add(weightOf(c) * count);
        }

        return queries.stream().map(Long::valueOf).map(b -> weights.contains(b) ? "Yes" : "No").toList();
    }
    static long weightOf(Character c) {
        return (long) c - 96;
    }
    public static List<String> weightedUniformStrings1(String s, List<Integer> queries) {
        Map<Integer, Long> count = s.codePoints().map(i -> i - 96).boxed().collect(Collectors.groupingBy(x -> x, Collectors.counting()));
        Set<Long> weights = new HashSet<>();
        for(Integer x : count.keySet())
            LongStream.rangeClosed(1, count.get(x)).map(i -> x * i).forEach(weights::add);

        System.out.println(weights);
        return queries.stream().map(Long::valueOf).map(b -> weights.contains(b) ? "Yes" : "No").toList();
    }

    /* https://www.hackerrank.com/challenges/game-of-stones-1/problem?isFullScreen=true */
    public static String gameOfStones(int n) {
            if(isWinning(n)) return "First";
            return "Second";
    }
    /* Return true if winning for the current player. */
    static boolean isWinning(int n) {
            final Stream<Integer> subtrahends = Stream.of(5, 3, 2).parallel();
            if(!memo.containsKey(n)) // search if not memoized yet
                memo.put(n, !subtrahends.map(i -> n - i).allMatch(Result::isWinning));
            return memo.get(n);
    }
    static final Map<Integer, Boolean> memo = new HashMap<>(Map.of(1, false, 3, true, 4, true));

    /* https://www.hackerrank.com/challenges/jim-and-the-orders/ */
    public static List<Integer> jimOrders(List<List<Integer>> orders) {
        final int n = orders.size();
        return range(0, n).boxed().map(i ->
                new Customer(i, orders.get(i)))
                .sorted(comparingLong(Customer::serveTime)
                        .thenComparing(Customer::name))
                .map(Customer::name)
                .toList();
    }
    record Customer(int name, long serveTime) {
        public Customer(int name, List<Integer> order) {
            this(++name, order.get(0) + order.get(1));
        }
    }
    public static List<Integer> countingSort(List<Integer> arr) {
       // final int largest = Math.max(Collections.max(arr), 100);
        //Map<Integer, Long> absent = IntStream.range(0, largest).filter(arr::contains).boxed().collect(groupingBy(identity(), Collectors.reducing(0L, x -> 0L, Long::sum)));
        TreeMap<Integer, Long> count = arr.stream().collect(groupingBy(identity(), TreeMap::new, counting()));
        count.putAll(range(0, 100).filter(i -> !count.containsKey(i)).boxed().collect(groupingBy(identity(), Collectors.reducing(0L, x -> 0L, Long::sum))));
        return count.values().stream().map(Math::toIntExact).collect(toList());
/*        Integer[] buckets = new Integer[largest];
        Arrays.fill(buckets, 0);
        for(Integer val : count.keySet())
            buckets[val] = toIntExact(count.getOrDefault(val, 0L));
        return Arrays.asList(buckets); */
    }
    public static int maximizingXor(int l, int r) {
        return rangeClosed(l, r).flatMap(i -> rangeClosed(l, r).map(j -> j ^ i)).max().orElseThrow();
    }

    public static int toys(List<Integer> w) {
        Collections.sort(w);
        int runningMin = w.get(0);
        int count = 1;
        final int THRESHOLD = 4;
        for(Integer i : w) {
            if(i > runningMin + THRESHOLD) {
                count++;
                runningMin = i;
            }
        }
        return count;
    }
    public static List<Integer> quickSort(List<Integer> arr) {
        int pivot = arr.get(0);
        return Stream.of(arr.stream().filter(i -> i < pivot),
                arr.stream().filter(i -> i > pivot),
                arr.stream().filter(i -> i == pivot))
                .reduce(Stream.of(), Stream::concat)
                .collect(toList());
    }

        public static String appendAndDelete(String s, String t, int k) {
        int i = 0;
        for(; s.startsWith(t.substring(0, i + 1)) && i < t.length() - 1; i++);
        final int prefixAt = i;
        int moves = Stream.of(t, s).map(String::length).map(l -> l - prefixAt - 1).reduce(0, Integer::sum);
        if(moves > k)
            return "No";
        int extraMoves = k - moves;
        if(extraMoves < 2 * (prefixAt + 1)) // test cases 5, 10
            if(extraMoves % 2 == 1)
                return "No";
        return "Yes";
    }


    public static void insertionSort2(int n, List<Integer> arr) {
            final int length = arr.size();
            List<Integer> copy = new ArrayList<>(arr);
            for(int i = 1; i < length; i++) {
                int current = arr.get(i);
                for(int j = 0; j < i; j++) {
                    if(current < arr.get(j)) {
                        arr.remove(i);
                        arr.add(j, current);
                        break;
                    }
                }
                System.out.println(String.join(" ", arr.stream().map(String::valueOf).collect(toList())));
            }
            Collections.sort(copy);
            if(!copy.equals(arr)) {
                System.err.println("Not sorted");
            }
        }

    public static int luckBalance(int k, List<List<Integer>> contests) {
        int unimportant = contests.stream()
                .filter(a -> a.get(1) == 0)
                .map(a -> a.get(0))
                .reduce(0, Integer::sum);
        List<Integer> important = contests.stream()
                .filter(a -> a.get(1) == 1)
                .map(a -> a.get(0))
                .sorted(Comparator.reverseOrder())
                .toList();
        int importantLost = important.stream().limit(k).reduce(0, Integer::sum);
        int won = important.stream().skip(k).reduce(0, Integer::sum);
        return unimportant + importantLost - won;
    }
    public static int maximumToys(List<Integer> prices, int k) {
        Collections.sort(prices);
        int total = 0;
        int balance = k;
        for(int price : prices) {
            if(price > balance) break;
            balance -= price;
            total++;
        }
        return total;
    }
    public static void decentNumber(int n) {
        System.out.println(decentOf(n));
    }
    static long decentOf(int n) {
        final int FACTOR_FIVE = 3;
        final int FACTOR_THREE = 5;
        if(n % FACTOR_FIVE == 0)
            return digitsOf(5, n);
        if(n < FACTOR_FIVE)
            return -1;
        long fiveLength = nextMultipleOf(n - FACTOR_THREE, FACTOR_FIVE);
        long pureFives = digitsOf(5, nextMultipleOf(n, FACTOR_FIVE));
        if(n < FACTOR_THREE)
            return pureFives;
        long shared = concat(digitsOf(5, fiveLength), digitsOf(3, FACTOR_THREE));
        return Math.max(shared, pureFives);
    }
    static int nextMultipleOf(int m, int k) {
        if(m == 0) return 0;
        return (m / k) * k;
    }
    static long digitsOf(long numeral, long length) {
        if(length == 0) return 0;
        return Long.parseLong(Stream.generate(() -> numeral).limit(length).map(String::valueOf).reduce("", String::concat));
    }
    static long concat(long upper, long lower) {
        return Long.parseLong(String.valueOf(upper) + String.valueOf(lower));
    }
    public static List<Integer> missingNumbers(List<Integer> arr, List<Integer> brr) {
        Map<Integer, Long> histogram = brr.stream().collect(groupingBy(identity(), counting()));
        for(Integer x : arr)
            histogram.computeIfPresent(x, (k, v) -> v > 1 ? --v : null);
        return new ArrayList<>(histogram.keySet());
    }
}
