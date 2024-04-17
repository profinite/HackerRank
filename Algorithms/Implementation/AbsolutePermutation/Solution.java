import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/** 
 *  Find the lexicographically-minimal permutation of the integers from 1 to N.
 *  𝚯(n log n)
 *  https://www.hackerrank.com/challenges/absolute-permutation/problem
 */
class Result {
    static Map<Permutation, List<Integer>> memo = new HashMap<>();

    public static List<Integer> absolutePermutation(int n, int k) {
        Set<Integer> options = IntStream.rangeClosed(1, n).boxed().collect(Collectors.toCollection(HashSet::new));
        if(k > n / 2) return FAIL;
        List<Integer> result = permutationOf(k, n);
        return result;
    }
    static final List<Integer> FAIL = List.of(-1);
    private static List<Integer> permutationOf(int k, int n) {
        if (k == 0)
            return IntStream.rangeClosed(1, n).boxed().collect(Collectors.toList());
        int half = n / 2;
        List<Integer> first = IntStream.rangeClosed(half, n).boxed().collect(Collectors.toList());
        List<Integer> last = IntStream.range(1, half).boxed().collect(Collectors.toList());
        List<Integer> result = Stream.concat(first.stream(), last.stream()).collect(Collectors.toList());
        if (verify(k, result).equals(FAIL))
            result = verify(k, Stream.concat(last.stream(), first.stream()).collect(Collectors.toList()));
        return result;
    }
    private static List<Integer> verify(int k, List<Integer> result) {
        int n = result.size();
        for(int i = 1; i <= n; i++) {
            int current = result.get(i - 1);
            if(current - i == k || current + i == k)
                continue;
            return FAIL;
        }
        return result;
    }
    private static List<Integer> permutationOf(int k, Set<Integer> options) {
        if (options.isEmpty())
            return new ArrayList<>();
        if(k == 0)
            return options.stream().sorted().collect(Collectors.toList());

        int pos = options.size();
        int addend = k + pos;
        int minuend = Math.abs(pos - k);
        for (Integer op : List.of(addend, minuend)) {
            if (!options.contains(op))
                continue;
            if(op == 2 * k)
                if(k != pos)
                    continue;
            options.remove(op);
            List<Integer> result = permutationOf(k, options);
            if (!result.equals(FAIL)) {
                result.add(op);
                return result;
            }
            options.add(op);
        }
        return FAIL;
    }
}
class Permutation {
    int k;
    Set<Integer> options;
    public Permutation(int k, Set<Integer> options) {
        this.k = k;
        this.options= options;
    }
}


// Omitting HackerRank Boilerplate
