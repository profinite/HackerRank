import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.awt.Point;
import static java.util.stream.Collectors.*;
import static java.util.function.Predicate.not;

class Result {

    /*
     * Find path to a portkey square, while counting the number of
     * decision points (we'll call them "junctions")
     * https://www.hackerrank.com/challenges/count-luck
     *
     * Note: Basic implementation problem, just need to
     * define "junction" appropriately for the terminal
     * points.
     *
     * Note for the internal graph, we use Cartesian
     * coordinates (x, y) to represent the matrix, eg
     * 𝒇(matrix) = Point(row, col) ⨁ size
     *
     * 𝚯(|V| + |E|) runtime complexity for DFS, hence 𝚯(N + 4N) or 𝚯(N),
     * where N = number of matrix cells (N = length * width).
     *
     * 𝚯(N) space complexity for each search
     */
    public static String countLuck(List<String> matrix, int k) {
        final Map<Point, Cell> graph = createGraph(matrix);
        long junctions = findPath(graph)
                .stream()
                .filter(Cell::isJunction)
                .count();
        if(k == junctions) {
            return "Impressed";
        }
        return "Oops!";
    }
 
    /* Create an undirected graph of the matrix, with open cells only.
     * Note the final cells are unblocked (open), but not necessarily reachable. */
    private static Map<Point, Cell> createGraph(List<String> matrix) {
        final int x = matrix.get(0).length();
        final int y = matrix.size();
        return Cell.matrixOf(x, y)
                .filter(p -> Cell.isOpen(p, matrix))
                .collect(Collectors.toMap(p->p, p -> new Cell(p, matrix)));
    }
  
    /* Initial recursive depth-first search (DFS) for the "port key" */
    private static List<Cell> findPath(Map<Point, Cell> graph) {
        return findPath(Cell.start, graph, new HashSet<>());
    }
  
    /* Search for goal in graph, then return the successful path */
    private static List<Cell> findPath(Point current, Map<Point, Cell> graph, Set<Point> visited) {
        if(current.equals(Cell.goal)) {
            return new ArrayList<>(List.of(graph.get(current)));
        }
        visited.add(current);
        List<Cell> path = graph.get(current).neighbors
                .stream()
                .filter(not(visited::contains))
                .map(p -> findPath(p, graph, visited))
                .filter(not(List::isEmpty))
                .findFirst()
                .orElse(List.of());
        if(!path.isEmpty())
            path.add(0, graph.get(current));
        return path;
    }
 
}



/* Cell of a matrix, with neighbors in the cardinal directions (N, S, E, W). 
 * Note this commandeers 'Point' from an otherwise unrelated Java library. */
class Cell {
    final Set<Point> neighbors;
    private final Point position;
    private final int width;
    private final int height;
    static Point goal;
    static Point start;
  
    Cell(Point p, List<String> matrix) {
        this.position = p;
        this.width = matrix.get(0).length();
        this.height = matrix.size();
        neighbors = neighborsOf().filter(n -> isOpen(n, matrix)).collect(toSet());
    }
  
    /* A "junction" is a cell with more than one option, in a viable path to the goal. 
     * The goal itself is never a junction. A cell with two or more 
     * non-source neighbors is always a junction. */
    public boolean isJunction() {
        if(position.equals(goal)) {
            return false;
        } else if(position.equals(start)) {
            return neighbors.size() > 1;
        }
        return neighbors.size() > 2;
    }
    /* Translate a matrix into Cartesian points for consistency */
    public static Stream<Point> matrixOf(int width, int height) {
        return IntStream.range(0, height).mapToObj(y ->
                IntStream.range(0, width).mapToObj(x -> new Point(x, y))).flatMap(p -> p);
    }
    /* Check if a given point of the matrix is unblocked (open) */
    static public boolean isOpen(Point p, List<String> matrix) {
        final char blocked = 'X';
        final char goal = '*';
        final char start = 'M';
        char terrain = matrix.get(p.y).charAt(p.x);
        if(terrain == goal) {
            Cell.goal = p;
        } else if (terrain == start) {
            Cell.start = p;
        }
        return (terrain != blocked);
    }
    private Stream<Point> neighborsOf() {
        return Direction.all().map(this::pointOf).filter(this::isValid);
    }
    private Point pointOf(Direction d) {
        Point q = this.position.getLocation();
        q.translate(d.dx, d.dy);
        return q;
    }
    private boolean isValid(Point q) {
        return q.x < width && q.x >= 0 && q.y < height && q.y >= 0;
    }
    /* Represent the four cardinal directions. */
    private enum Direction {
        N(0, 1),
        S(0, -1),
        W(-1, 0),
        E(1, 0);
        private final int dx; private final int dy;
        Direction(int dx, int dy) {
            this.dx = dx; this.dy = dy;
        }
        static Stream<Direction> all() {
            return Arrays.stream(Direction.values());
        }
    }
}







/* ----------------------
 * HackerRank boilerplate
 * ---------------------- */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
        int t = Integer.parseInt(bufferedReader.readLine().trim());
        IntStream.range(0, t).forEach(tItr -> {
            try {
                String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
                int n = Integer.parseInt(firstMultipleInput[0]);
                int m = Integer.parseInt(firstMultipleInput[1]);
                List<String> matrix = IntStream.range(0, n).mapToObj(i -> {
                            try {
                                return bufferedReader.readLine();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }).collect(toList());

                int k = Integer.parseInt(bufferedReader.readLine().trim());
                String result = Result.countLuck(matrix, k);
                bufferedWriter.write(result);
                bufferedWriter.newLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
        bufferedWriter.close();
    }
}
