import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.*;
import java.awt.Point;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Find size of the largest region of connected cells
     * https://www.hackerrank.com/challenges/connected-cell-in-a-grid
     *
     * Notes: This uses BFS, in retrospect considerably more awkward 
     * than DFS or a basic 'connected components' algorithm.
     * 
     * 𝚯(V + E) runtime complexity for BFS, hence 𝚯(N + 8N) or 𝚯(N),
     * where N = number of matrix cells (N = l * h).
     *
     * 𝚯(N) space complexity for each search
     * 
     * As an experiment, converts the 2D list into 2D array.
     */
    public static int connectedCell(List<List<Integer>> lists) {
        Integer[][] matrix = lists.stream()
                .map(x -> x.toArray(new Integer[0]))
                .toArray(Integer[][]::new);
        return findRegions(matrix).stream().mapToInt(Set::size).max().orElse(0);
    }
  
    /* Tally up distinct regions for this matrix. */
    private static Collection<Set<Point>> findRegions(Integer[][] matrix) {
        final Point start = new Point(0, 0);
        Deque<Point> queue = new ArrayDeque<>(List.of(start));
        Set<Point> alreadyVisited = new HashSet<>(List.of(start));
        final Region regions = new Region(matrix.length, matrix[0].length);

        do {
            Point current = queue.remove();
            if(matrix[current.x][current.y] == 1) {
                regions.add(current);
            }
            alreadyVisited.add(current);
            queue.addAll(regions.neighborsOf(current));
            queue.removeAll(alreadyVisited);
        } while (!queue.isEmpty());

        return regions.all.values();
    }
}
/*
 * Find neighbors in the matrix.
 * Needs refactoring, not a coherent class
 */
class Region {
    Map<Point, Set<Point>> all = new HashMap<>();
    final int length; final int height;
    Region(int length, int height) {
        this.length = length;
        this.height = height;
    }
    public void add(Point p) {
        Set<Point> sources = new HashSet<>(neighborsOf(p));
        Set<Point> region = new HashSet<>(List.of(p));      
        sources.retainAll(all.keySet());
        for(Point q : sources) {
            all.get(q).addAll(region);
            region = all.get(q);
        }
        all.put(p, region);
    }
    /* Enumerate all positions reachable from this cell. */
    public List<Point> neighborsOf(Point p) {
        return Direction.all()
                .map(d -> pointOf(d, p))
                .filter(this::isValid)
                .collect(toList());
    }
    private Point pointOf(Direction d, Point p) {
        Point q = p.getLocation();
        q.translate(d.dx, d.dy);
        return q;
    }

    /* Represent the cardinal directions */
    private enum Direction {
        N(0, 1),
        E(1, 0),
        S(0, -1),
        W(-1, 0),
        NW(-1,1),
        NE(1,1),
        SE(1, -1),
        SW(-1, -1);
        final int dx; final int dy;
        private Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
        static Stream<Direction> all() {
            return Arrays.stream(Direction.values());
        }
    }
    private boolean isValid(Point p) {
        return p.y < height && p.x < length && p.y >= 0 && p.x >= 0;
    }
}



/* ---------------------- */
/* HackerRank boilerplate */
/* ---------------------- */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = Integer.parseInt(bufferedReader.readLine().trim());
        int m = Integer.parseInt(bufferedReader.readLine().trim());
        List<List<Integer>> matrix = new ArrayList<>();
        IntStream.range(0, n).forEach(i -> {
            try {
                matrix.add(Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                .map(Integer::parseInt)
                                .collect(toList()));
            } catch (IOException ex) { throw new RuntimeException(ex); }
        });

        int result = Result.connectedCell(matrix);
        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();
        bufferedReader.close();
        bufferedWriter.close();
    }
}

