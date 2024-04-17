import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;


/**
 * Determine how many pairs of astronauts can be sent.
 * https://www.hackerrank.com/challenges/journey-to-the-moon/problem
 *
 * Builds a graph of the astronauts then applies combinatorics for
 * non-singletons.
 */
class Result {
    public static long journeyToMoon(int n, List<List<Integer>> astronaut) {
        Map<Integer, Node> members = new HashMap<>();
        for (List<Integer> pair : astronaut) {
            int source = pair.get(0);
            int sink = pair.get(1);
            members.putIfAbsent(source, new Node(source));
            members.putIfAbsent(sink, new Node(sink));
            members.get(source).add(members.get(sink));
        }
        Stream<Integer> singletons = Stream.generate(() -> 1).limit(n - members.size());
        Stream<Integer> sets = members.values().stream().filter(m -> !m.visited).map(Node::count);
        List<Integer> counts = Stream.concat(sets, singletons).toList();
        final int m = counts.size();
        return IntStream.range(0, m).flatMap(i ->
                IntStream.range(i + 1, m).map(j -> counts.get(i) * counts.get(j)))
                    .mapToLong(Long::valueOf).sum();
    }
    static class Node {
        final int name;
        boolean visited = false;
        Set<Node> neighbors = new HashSet<>();
        public Node(int name) {
            this.name = name;
        }
        int count() { // tally neighbors via BFS
            int count = 0;
            Deque<Node> queue = new ArrayDeque<>(List.of(this));
            while(!queue.isEmpty()) {
                Node current = queue.pop();
                if(current.visited) continue;
                count++;
                current.visited = true;
                queue.addAll(current.neighbors);
            }
            return count;
        }
        public void add(Node n) {
            neighbors.add(n);
            n.neighbors.add(this);
        }
    }
}

// HackerRank Boilerplate
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int p = Integer.parseInt(firstMultipleInput[1]);

        List<List<Integer>> astronaut = new ArrayList<>();

        IntStream.range(0, p).forEach(i -> {
            try {
                astronaut.add(
                        Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                .map(Integer::parseInt)
                                .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        long result = Result.journeyToMoon(n, astronaut);
        System.out.println(result);


        bufferedReader.close();
    }
}
