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



class Result {

    // take #2 with simpler flow. See Solution.java for comments
    public static int flippingMatrix(List<List<Integer>> matrix) {
        final int n = matrix.size() / 2;
        return IntStream.range(0, n).flatMap(x ->
            IntStream.range(0, n).map(y -> maxOf(x, y, matrix))).sum();
    }
    private static int maxOf(int x, int y, List<List<Integer>> matrix) {
        final int m = matrix.size() - 1;
        int UL = matrix.get(x).get(y);
        int UR = matrix.get(m - x).get(y);
        int LR = matrix.get(x).get(m - y);
        int LL = matrix.get(m - x).get(m - y);
        return Collections.max(Arrays.asList(UL, UR, LR, LL));
    }

}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int q = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, q).forEach(qItr -> {
            try {
                int n = Integer.parseInt(bufferedReader.readLine().trim());

                List<List<Integer>> matrix = new ArrayList<>();

                IntStream.range(0, 2 * n).forEach(i -> {
                    try {
                        matrix.add(
                            Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                .map(Integer::parseInt)
                                .collect(toList())
                        );
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                int result = Result.flippingMatrix(matrix);

                bufferedWriter.write(String.valueOf(result));
                bufferedWriter.newLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
        bufferedWriter.close();
    }
}
