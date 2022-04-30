import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {
   /* 
    * Count all triplets in a sequence for which:
    *   a[j] - a[i] = a[k] - a[j] = k
    * 
    * given the desired summand 'k'.
    * 
    * Key insight is speculatively subtract the minuend constant from each term
    * then comparing each one in turn.
    *
    * For elegance, here we create a frequency map of the sequence for 0(n log n).
    * then compare to an inner class for tracking deltas.
    */
    public static int beautifulTriplets(final int diff, final List<Integer> arr) {
        final Map<Integer, Long> histogram = frequencyMap(arr);
        long count = histogram
                .keySet()
                .stream()
                .mapToLong(i->countTriplet(histogram, 
                    new Result()
                    .new Triplet(i, diff)))
                .reduce(0, Long::sum);
                
        System.out.println("Found: " + count + " triplets.");
        return Math.toIntExact(count);
    }

    private static long countTriplet(final Map<Integer, Long> histogram, Triplet beauty) {
        if(beauty.terms().allMatch(histogram::containsKey)) {
            return beauty.terms()
                .mapToLong(histogram::get)
                .reduce(1, Math::multiplyExact);
        }
        return 0;
    }

    /* NOTE: Must use LinkedHashMap to preserve ordering. */
    private static <Integer> Map<Integer, Long> frequencyMap(List<Integer> list) {
        return list.stream()
                    .collect(Collectors.groupingBy(
                        Function.identity(), 
                        LinkedHashMap::new, 
                        Collectors.counting()));
    }
    
    /* A beautiful triplet for the given constants.
     *  Terminology: subtrahend - base == minuend - subtrahend = diff */
    class Triplet {
        final int base;
        final int subtrahend;
        final int minuend;
        public Triplet(int base, int diff) {
            this.base = base;
            this.subtrahend = diff + base;
            this.minuend = 2 * diff + base;
        }
        public Stream<Integer> terms() {
            return Stream.of(base, subtrahend, minuend);
        }
    }
}


/* Standard Hackerrank boilerplate de rigeur for Java. */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
        int n = Integer.parseInt(firstMultipleInput[0]);
        int d = Integer.parseInt(firstMultipleInput[1]);
        List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(toList());
        int result = Result.beautifulTriplets(d, arr);
        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();
        bufferedReader.close();
        bufferedWriter.close();
    }

}
