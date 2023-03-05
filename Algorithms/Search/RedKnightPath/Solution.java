import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.awt.Point;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Compute minimal distance for a knight-like chess piece (Red Knight)
     * making L-shaped moves - with the addition of a horizontal move, akin
     * to the 'chancellor' piece in Foster Chess:
     * https://www.hackerrank.com/challenges/red-knights-shortest-path
     *
     * Note this problem uses (row, column) rather than typical
     * Cartesian coordinates (x, y). Also must store the relative moves
     * used to reach the final position.
     *
     * 𝚯(V + E) runtime complexity for BFS, hence 𝚯(N + 6N) or 𝚯(N), 
     * where N = number of board squares (N = n²).
     *
     * 𝚯(N) space complexity for each search
     */
    public static void printShortestPath(int n, int i_start, int j_start, int i_end, int j_end) {
        String path = findPath(new Point(i_start, j_start), new Point(i_end, j_end), n);
        int length = path.split(" ").length - 1;
        if(length > 1) {
            System.out.println(length);
        }
        System.out.println(path.trim());
    }

    /* Compute the board distance for this Knight, via BFS */
    private static String findPath(Point origin, Point goal, int width) {
        Deque<Point> queue = new ArrayDeque<>(List.of(origin));
        Set<Point> alreadyVisited = new HashSet<>(List.of(origin));
        Move lastMove = new Move(origin, width);

        do {
            Point current = queue.remove();
            lastMove = lastMove.atlas.get(current);
            if (current.equals(goal)) {
                return lastMove.history();
            } else {
                alreadyVisited.add(current);
                queue.addAll(lastMove.neighborsOf());
                queue.removeAll(alreadyVisited);
            }
        } while (!queue.isEmpty());

        return "Impossible";
    }
}
/*
 * Represent each Move of a "Red Knight" moving
 * in (dx, dy) fashion on a chessboard.
 * Coordinates are non-cartesian (row, col)
 *
 * Store the preferred moves in a log ("atlas"), with
 * the following priority; UL, UR, R, LR, LL, L.
 */
class Move {
    final Map<Point, Move> atlas;
    private final Move prior;
    private final Direction source;
    private final Point position;
    private final int n;
    Move(Point p, int n) {
        atlas = new HashMap<>(Map.of(p, this));
        this.position = p;
        this.n = n;
        prior = null;
        source = null;
    }
    Move(Move m, Point p, Direction d) {
        this.atlas = m.atlas;
        this.prior = m;
        this.source = d;
        this.position = p;
        this.n = m.n;
    }
    /* Enumerate all positions reachable by this Knight */
    public List<Point> neighborsOf() {
        return Direction.all()
                .map(this::pointOf)
                .filter(this::isValid)
                .collect(toList());
    }
    public String history() {
        if(this.prior == null)
            return "";
        return this.prior.history() + " " + this.source.toString();
    }
    private Point pointOf(Direction d) {
        Point q = this.position.getLocation();
        q.translate(d.dx, d.dy);
        atlas.putIfAbsent(q, new Move(this, q, d));
        return q;
    }

    /* Represent the intercardinal directions */
    private enum Direction {
        UL(-2,-1),
        UR(-2,1),
        R(0, 2),
        LR(2, 1),
        LL(2, -1),
        L(0, -2);
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
        return p.y < n && p.x < n && p.y >= 0 && p.x >= 0;
    }
}



/* ---------------------- 
 * HackerRank boilerplate 
 * ---------------------- */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine().trim());
        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
        int i_start = Integer.parseInt(firstMultipleInput[0]);
        int j_start = Integer.parseInt(firstMultipleInput[1]);
        int i_end = Integer.parseInt(firstMultipleInput[2]);
        int j_end = Integer.parseInt(firstMultipleInput[3]);
        Result.printShortestPath(n, i_start, j_start, i_end, j_end);
        bufferedReader.close();
    }
}
