import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import java.awt.Point;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Compute minimal distance for a knight making L-shaped moves.
     *
     * Notes: Essentially a mere implementation problem, but we can
     * parallelize the search, and store identical states (homologs).
     *
     * Uses a reductive design for potential concurrency gains,
     * backed by a Red-Black Tree of dependencies (ConcurrentHashMap).
     * (remove .parallel for sequential hardware)
     *
     * 𝚯(N) runtime for each Knight, given N = board size (n * n).
     *
     * Total runtime 𝚯(N log N) with unlimited processors and neighbor reducing,
     * otherwise 𝚯(knights * searches) = 𝚯(n * N) = 𝚯(n³).
     *
     * 𝚯(N) space complexity for each search
     */
    static Map<Set<Integer>, Integer> memo = new ConcurrentHashMap<>();
    public static List<List<Integer>> knightOnAChessboard(int n) {
        return IntStream.range(1, n)
                .parallel()
                .mapToObj(x -> IntStream.range(1, n)
                   .parallel()
                   .map(y -> memoize(new Knight(x, y, n)))
            .boxed().toList()).toList();
    }

    /* Store mirrors of this Knight for simple dynamic programming. */
    private static Integer memoize(Knight k) {
        return memo.merge(k.homolog(), distanceOf(k), (a, b) -> a);
    }

    /* Compute the board distance for this Knight, via BFS */
    private static Integer distanceOf(Knight k) {
        final Point destination = new Point(k.n - 1, k.n - 1);
        final Point origin  = new Point(0, 0);
        Deque<Point> queue = new ArrayDeque<>(List.of(origin));
        Set<Point> alreadyVisited = new HashSet<>(List.of(origin));
        k.atlas.put(origin, 0);

        do {
            Point current = queue.remove();
            if (current.equals(destination)) {
                return k.atlas.get(current);
            } else {
                alreadyVisited.add(current);
                queue.addAll(k.neighborsOf(current));
                queue.removeAll(alreadyVisited);
            }
        } while (!queue.isEmpty());
        return -1;
    }
}
/*
 * Knight moving in (dx, dy) fashion on chessboard.
 * The knight keeps an 'odometer' of the minimum moves
 * to reach a given square.
 */
class Knight {
    Map<Point, Integer> atlas = new ConcurrentHashMap<>();
    final int x; final int y; final int n;
    Knight(int x, int y, int n) {
        this.x = x;
        this.y = y;
        this.n = n;
    }
    /* Enumerate all positions reachable by this Knight */
    public List<Point> neighborsOf(Point p) {
        return Direction.all()
                .flatMap(d -> travel(p, d))
                .filter(this::isValid)
                .toList();
    }
    private Stream<Point> travel(Point p, Direction d) {
        return Stream.of(pointOf(p, d, x, y), pointOf(p, d, y, x));
    }
    private Point pointOf(Point p, Direction sign, int dx, int dy) {
        Point q = p.getLocation();
        q.translate(dx * sign.dx, dy * sign.dy);
        atlas.merge(q, atlas.get(p) + 1, Math::min); //prolly putIfAbsent would suffice.
        return q;
    }
    /* Represent the cardinal directions */
    private enum Direction {
        U(1,1),
        D(-1,-1),
        L(-1, 1),
        R(1, -1);
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
    Set<Integer> homolog() {
        if(x == y)
            return Set.of(x);
        return Set.of(x, y);
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<List<Integer>> result = Result.knightOnAChessboard(n);

        result.stream()
                .map(
                        r -> r.stream()
                                .map(Object::toString)
                                .collect(joining(" "))
                )
                .map(r -> r + "\n")
                .collect(toList())
                .forEach(e -> {
                    try {
                        bufferedWriter.write(e);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

        bufferedReader.close();
        bufferedWriter.close();
    }
}
