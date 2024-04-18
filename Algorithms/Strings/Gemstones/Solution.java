import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

class Result {

    /** 
     * Find minerals universal to each rock
     * https://www.hackerrank.com/challenges/gem-stones/problem
     *
     * Here we simply need to find characters ubiquitous to each batch:
     * 1) Convert string to a set
     * 2) Take a collective union of all sets (parallelizable)
     * 3) Return the cardinality of the union
     *
     * Parallel 𝚯(log n) with 'neighbor reducing' enabled in theory, otherwise 𝚯(n).
     *
     */
    public static int gemstones(List<String> arr) {
        return arr.stream()
                .map(s -> s.codePoints().boxed().collect(toSet()))
                .reduce((a, b) -> { a.retainAll(b); return a; })
                .get()
                .size();
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> arr = IntStream.range(0, n).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(toList());

        int result = Result.gemstones(arr);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
