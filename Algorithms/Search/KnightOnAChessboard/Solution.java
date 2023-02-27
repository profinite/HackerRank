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
     * store identical states (homologs) and parallelize the search.
     *
     * Uses a reductive design for potential concurrency gains,
     * especially with newer Java runtimes on server hardware.
     * (remove .parallel otherwise)
     *
     * 𝚯(n log n) runtime, given unlimited threads and neighbor reducing enabled.
     * Otherwise, 𝚯(n * n).
     *
     * 𝚯(n) space complexity.
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

    /* Store homologs of this Knight for dynamic programming. */
    private static Integer memoize(Knight k) {
        return memo.merge(k.homolog(), distanceOf(k), (a, b) -> a);
    }

    /* Compute the board distance for this Knight, via BFS */
    private static Integer distanceOf(Knight k) {
        Set<Point> alreadyVisited = new HashSet<>();
        final Point destination = new Point(k.n - 1, k.n - 1);
        final Point origin  = new Point(new Point(0, 0));

        k.atlas.put(origin, 0);
        Deque<Point> queue = new ArrayDeque<>(List.of(origin));
        alreadyVisited.add(origin);

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

/* Knight moving in (dx, dy) fashion on chessboard. */
class Knight{
    Map<Point, Integer> atlas = new ConcurrentHashMap<>();
    int dx; int dy; int n;
    Knight(int x, int y, int n) {
        dx = x;
        dy = y;
        this.n = n;
    }
    Set<Integer> homolog() {
        if(dx == dy)
            return Set.of(dx);
        return Set.of(dx, dy);
    }
   public List<Point> neighborsOf(Point p) {
        return Stream.of(
                point(p, dx, dy),
                point(p, -dx, dy),
                point(p, dx, -dy),
                point(p, -dx, -dy),
                point(p, dy, dx),
                point(p, -dy, dx),
                point(p, dy, -dx),
                point(p, -dy, -dx))
        .filter(this::isValid).collect(toList());
    }
    private boolean isValid(Point p) {
         return p.y < n && p.x < n && p.y >= 0 && p.x >= 0;
    }
    private Point point(Point s, int dx, int dy) {
        Point q = s.getLocation();
        q.translate(dx, dy);
        atlas.merge(q, atlas.get(s) + 1, Math::min); //prolly putIfAbsent would suffice.
        return q;
    }

}

/* HackerRank boilerplate */
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
