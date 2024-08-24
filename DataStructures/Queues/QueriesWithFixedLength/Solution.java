import java.io.*;
import java.util.*;
import java.util.stream.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {
    /**
     * Find the smallest of the rolling maxima within a sequence
     * https://www.hackerrank.com/challenges/queries-with-fixed-length/problem
     *
     * Tempted to use a Mergesort tree but this doesn't actually need it -
     * just used a sliding window algorithm with a max-heap of the observed values,
     * tagged with their index, and ignored the stale elements.
     *
     * 𝚯(N log N * Q) for a sequence of length N and Q queries
     * 𝚯(N) space complexity
     *
     * @param arr - sequence of values
     * @param queries - window size of the rolling maximum
     * @return list of minima for each window size
     */
    public static List<Integer> solve(List<Integer> arr, List<Integer> queries) {
        List<Integer> minima = new ArrayList<>();
        List<Element> sequence = IntStream.range(0, arr.size()).boxed().map(i -> new Element(arr.get(i), i)).toList();
        for (Integer n : queries) {
            minima.add(minimumOf(sequence, n));
        }
        return minima;
    }

    /* Sliding window algorithm with Max-Heap. */
    private static Integer minimumOf(List<Element> seq, Integer n) {
        PriorityQueue<Element> maxima = new PriorityQueue<>(Comparator.comparing(Element::value, Comparator.reverseOrder()));
        maxima.addAll(seq.subList(0, n));
        int min = Integer.MAX_VALUE;
        for (int i = n - 1, j = 0; i < seq.size(); i++, j++) {
            maxima.add(seq.get(i));
            while (maxima.peek().index() < j) {
                maxima.remove(); // ignore stale entries
            }
            min = Math.min(maxima.peek().value(), min);
        }
        return min;
    }
    record Element(int value, int index) { }
}






public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int q = Integer.parseInt(firstMultipleInput[1]);

        List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Integer::parseInt)
                .collect(toList());

        List<Integer> queries = IntStream.range(0, q).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine().replaceAll("\\s+$", "");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(toList());

        List<Integer> result = Result.solve(arr, queries);

        bufferedWriter.write(
                result.stream()
                        .map(Object::toString)
                        .collect(joining("\n"))
                        + "\n"
        );

        bufferedReader.close();
        bufferedWriter.close();
    }
}
