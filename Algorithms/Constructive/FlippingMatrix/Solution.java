import java.util.List;
import java.util.stream.Stream;
import java.awt.Point;
import static java.util.stream.IntStream.range;

class Result {
    /*
     *  Maximize the upper-left quadrant of an even-length matrix,
     *  using only vertical and horizontal reflections.
     *  https://www.hackerrank.com/challenges/flipping-the-matrix
     *
     *  Works by selecting the "foci" or reflectant axes of our
     *  sub-matrix, and choosing the largest ones. 
     *  https://en.wikipedia.org/wiki/Focus_(geometry)
     *
     *  𝚯(N/P) runtime, for n cells and 'p' processors.
     *  Parallelizable.
     */
    public static int flippingMatrix(final List<List<Integer>> matrix) {
        final int n = matrix.size() / 2;
        return positionsOf(n)
                .parallel()
                .mapToInt(p -> maxFocus(matrix, p))
                .sum();
    }
    /* Find largest of the reflectant foci, for this cell of the matrix. */
    private static int maxFocus(List<List<Integer>> matrix, Point p)  {
        final int m = matrix.size() - 1; // let m = 2n
        List<Point> reflectant = List.of(p,       // UL
                new Point(p.x, m - p.y),          // UR
                new Point(m - p.x, p.y),          // LL
                new Point(m - p.x, m - p.y));     // LR
        return reflectant.stream()
                .mapToInt(f -> elementAt(matrix, f))
                .max().orElseThrow();
    }
    /* Coordinates for all cells in a matrix of given 'length' in (row, col) format */
    static Stream<Point> positionsOf(int length) {
        return range(0, length).boxed().flatMap(r -> range(0, length).boxed().map(c -> new Point(c, r)));
    }
    private static int elementAt(List<List<Integer>> matrix, Point p) {
        return matrix.get(p.y).get(p.x);
    }
}

// omitting Solution class boilerplate
