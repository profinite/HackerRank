import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;
import java.util.*;
import java.awt.Point;

/**
 * Determine who will win a monotonic series of leftward knight moves
 * https://www.hackerrank.com/challenges/a-chessboard-game-1/problem
 *
 * Key observation is just observing that a position is winnable
 * if it contains any winnable derivations.
 *
 * 𝚯(N) runtime for chessboard of size N X N
 */
class Result {
    static Map<Point, Boolean> memo = new ConcurrentHashMap<>();
    public static String chessboardGame(int x, int y) {
        if(isWinning(new Point(x, y)))
            return "First";
        return "Second";
    }
    /* Check if this valid position is winnable */
    private static boolean isWinning(Point p) {
        if(!memo.containsKey(p))
            memo.put(p, !nextOf(p).allMatch(Result::isWinning));

        return memo.get(p);
    }
    private static Stream<Point> nextOf(Point p) {
        return Stream.of(
                    new Point(-2, 1),
                    new Point(-2, -1),
                    new Point(1, -2),
                    new Point(-1, -2))
           .parallel()
           .map(p::translate)
           .filter(Result::inBounds);
    }
    private static boolean inBounds(Point p) {
        return p.x > 0 && p.y > 0;
    }
    record Point(int x, int y) {
        public Point translate(Point p) {
            return new Point(x + p.x(), y + p.y());
        }
    }
}
