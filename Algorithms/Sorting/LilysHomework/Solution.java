import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {

    /**
     * Count how many swaps yield a sorted array (asc or desc)
     * https://www.hackerrank.com/challenges/lilys-homework/problem
     *
     * Fairly straightforward. Requires distinct elements since it
     * hashes each one to its position.
     *
     * 𝛀(n log n) runtime complexity due to sort
     * 𝚯(N log N + N * M) for N elements and M swaps
     * 𝚯(N) space
     *
     * @param arr List to be rendered monotonic via swaps
     * @return count of minimum swaps needed
     */
    public static int lilysHomework(List<Integer> arr) {
        List<Integer> sorted = new ArrayList<>(arr);
        Collections.sort(sorted);
        List<Integer> reverseSort = new ArrayList<>(sorted);
        Collections.reverse(reverseSort);

        int count = diff(new ArrayList<>(arr), sorted);
        count = Math.min(count, diff(arr, reverseSort));

        return count;
    }
    /* Count swaps needed to achieve a sorted list.
     * Destructive of the incoming list. */
    private static int diff(List<Integer> a, List<Integer> sorted) {
        Map<Integer, Integer> position = IntStream
                .range(0, sorted.size())
                .boxed()
                .collect(Collectors.toMap(sorted::get, x -> x));
        int count = 0;
        for(int i = 0; i < a.size(); i++) {
            if(!a.get(i).equals(sorted.get(i))) {
                swap(a, i, position.get(a.get(i)));
                count++;
                i--; // revisit this element
            }
        }
        if(!a.equals(sorted))
            throw new IllegalStateException();
        return count;
    }
    /* Swap indices of list, destructive */
    private static void swap(List<Integer> a, int i, int j) {
        int tmp = a.get(j);
        a.set(j, a.get(i));
        a.set(i, tmp);
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Integer::parseInt)
                .collect(toList());

        int result = Result.lilysHomework(arr);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
