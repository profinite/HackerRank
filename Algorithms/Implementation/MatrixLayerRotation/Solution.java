import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.awt.Point;
import static java.util.stream.IntStream.range;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/* 
 * Simulate millions of rotations for each layer ("ring") 
 * of a large matrix.
 *
 * 0(m + n) runtime, or 0(1) with respect to rotations k
 * 
 * Parses 2D array into a shadow matrix of cells backed 
 * by circular arrays. 
 *
 * TODO: Overall delineation of objects/tasks could be more crisp.
 */
class Result {
    
    /* Shadow the 2D array into a matrix backed by circular arrays. */
    public static void matrixRotation(List<List<Integer>> basis, int r) {
        final int m = basis.size();
        final int n = basis.get(0).size();
        Cell[][] affine = new Cell[m][n];
        final int ringLevels = Math.min(m, n) / 2;
        
        // Parse the grid into rings
        for (int i = 0; i < ringLevels; i++) {
            Ring next = new Ring(i, basis, affine);
            int width = m - i - 1;
            int length = n - i - 1;
            addRing(next, width, length);
        }
        // Now rotate the fully initialized matrix 
        System.out.println(rotate(affine, r));
    }
    
    /* Simulate a given ring of the matrix, tagging each corner. 
     * Being very deliberate here, to preclude bugs. */
    static void addRing(Ring ring, int m, int n) {
        final Point NW = new Point(ring.level, ring.level);
        final Point SW = new Point(ring.level, m);
        final Point SE = new Point(n, m);
        final Point NE = new Point(n, ring.level);

        ring.addColumn(NW, SW);
        ring.addRow(SW, SE);
        ring.addColumn(SE, NE);
        ring.addRow(NE, NW);
    }
    
    private static String rotate(Cell[][] affinity, int r) {
        return Stream.of(affinity)
                .map(l -> cellsToString(l, r))
                .collect(joining("\n"));
    }
    
    private static String cellsToString(Cell[] cells, int r){
        return Stream.of(cells)
                     .map(c -> c.rotate(r))
                     .map(String::valueOf)
                     .collect(joining(" "));
    }

}

/* Represent each layer ("ring") of matrix */
class Ring {
    public final int level;
    private final List<Integer> circular = new ArrayList<>();
    private final List<List<Integer>> basis;
    private final Cell[][] affinity;
    Ring(int level, List<List<Integer>> basis, Cell[][] affinity) {
        this.level = level;
        this.basis = basis;
        this.affinity = affinity;
    }
    private void add(List<Point> points) {
        final int bar = circular.size();
        final int span = points.size();
        range(0, span).forEachOrdered(i -> affineCell(points.get(i), bar + i));
    }
    private void affineCell(Point p, int index) {
        circular.add(basis.get(p.y).get(p.x));
        affinity[p.y][p.x] = new Cell(this, index);
    }
    void addRow(Point start, Point end) {
        this.add(interval(start.x, end.x)
            .map(x -> new Point(x, start.y))
            .collect(toList()));
    }
    void addColumn(Point start, Point end) {
        this.add(interval(start.y, end.y)
            .map(y -> new Point(start.x, y))
            .collect(toList()));
    }
    /* Rotate this ring, optimizing out any
     * over-rotations beyond the ring length. */
    Integer rotate(int index, int r) {
        final int length = circular.size();
        int offset = index - r % length;
        if (offset < 0) offset = length - Math.abs(offset);
        return circular.get(offset);
    }
    /* Helper function, extend Java's IntStream.range to support
     * descending ranges too. */
    private static Stream<Integer> interval(int start, int end) {
        IntStream range = range(start, end);
        if(start > end) 
            range = range(end, start).map(i -> end + (start - i));
        return range.boxed();
    }
}
/* Represent the individual cells of a matrix, backing them
 * with circular array. */
class Cell {
    private final Ring ring;
    private final int index;
    Cell(Ring ring, int index) {
        this.ring = ring;
        this.index = index;
    }
    public Integer rotate(int r) {
        return ring.rotate(index, r);
    }
}






/* Standard Hackerrank boilerplate for Java */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
        int m = Integer.parseInt(firstMultipleInput[0]);
        int n = Integer.parseInt(firstMultipleInput[1]);
        int r = Integer.parseInt(firstMultipleInput[2]);
        List<List<Integer>> matrix = new ArrayList<>();
        IntStream.range(0, m).forEach(i -> {
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
        Result.matrixRotation(matrix, r);
        bufferedReader.close();
    }
}
