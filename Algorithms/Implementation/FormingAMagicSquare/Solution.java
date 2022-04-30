import java.io.*;
import java.util.stream.*;
import java.util.*;
import java.util.stream.IntStream;
import static java.util.stream.IntStream.range;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Convert a given matrix into a "magic square" in which 
     * all rows, cols and diagonals sum to the same constant.
     *
     * We simply enumerate all achievable magic squares (8 total) as
     * affines of a blueprint, then count the differences from each.
     * The square with the fewest differences is the best solution.
     *
     * 0(n) runtime, where n is the size of the matrix - 
     * restricted to 3x3 hence 0(1) for the problem set.
     */
    public static int formingMagicSquare(List<List<Integer>> subject) {
        final int[][] blueprint = {{8, 3, 4},
                                   {1, 5, 9},
                                   {6, 7, 2}};       
        return Stream.concat(affinesOf(blueprint), 
                             mirrorsOf(blueprint))
                    .mapToInt(magic -> countDiff(subject, magic))
                    .min().orElse(0);
    }
  
    private static Stream<int[][]> mirrorsOf(int[][] magicSquare) {
        return affinesOf(magicSquare).map(Result::mirrorOf);
    }

    private static Stream<int[][]> affinesOf(int[][] magicSquare) {
        int[][] aff1 = affineOf(magicSquare);
        int[][] aff2 = affineOf(aff1);
        int[][] aff3 = affineOf(aff2);
        return Stream.of(magicSquare, aff1, aff2, aff3);
    }
  
    private static int[][] affineOf(int[][] orig) {
        final int m = orig.length; // 3
        int[][] affine = new int[m][m];
        range(0, m).forEach(i -> 
            range(0, m).forEach(j -> {
                affine[j][m - 1 - i] = orig[i][j]; }));
        return affine;
    }
  
    private static int[][] mirrorOf(int[][] orig) {
        final int n = orig.length;
        int[][] mirror = new int[n][n];
        for (int row = 0; row < n; row++) {
            int pivot = 0;
            for (int col = n - 1; col >= 0; col--, pivot++) {
                int scalar = orig[row][col];
                mirror[row][pivot] = scalar;
            }
        }
        return mirror;
    }
  
    /* Count differences from the magic square. 
     * As an experiment, converts input from lists to 2D array for readability.
     */
    private static int countDiff(List<List<Integer>> subject, int[][] magic) {
        Integer[][] arr = subject.stream()
                .map(u -> u.toArray(new Integer[0]))
                .toArray(Integer[][]::new);
        int delta = 0;
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                delta += Math.abs(arr[i][j] - magic[i][j]);
            }
        }
        return delta;
    }
}




/* Standard Java boilerplate for Hackerrank. */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        List<List<Integer>> s = new ArrayList<>();
        IntStream.range(0, 3).forEach(i -> {
            try {
                s.add(
                    Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        int result = Result.formingMagicSquare(s);
        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();
        bufferedReader.close();
        bufferedWriter.close();
    }
}

