import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {
    /**
     * Provide libraries to a nation of cities at minimal cost
     * https://www.hackerrank.com/challenges/torque-and-development/
     * 
     * Here we treat this as a graph partitioning problem. Let each
     * neighborhood or subgraph of connected cities be a "metro".
     * - Every isolated city is a "metro", by necessity.
     * - If the library cost is lower than roads, every city is a metro.
     * - Otherwise, favor roads over libraries.
     * - Count the number of metros by simply counting the connectable subgraphs.
     * 
     * 𝚯(n) runtime
     * 𝚯(n) space complexity to represent the nation of cities
     *
     * @param n        - number of cities
     * @param libCost  - cost of each library
     * @param roadCost - cost of each road
     * @param cities   - graph of cities
     * @return total cost
     */
    public static long roadsAndLibraries(int n, long libCost, long roadCost, List<List<Integer>> cities) {
        if (libCost <= roadCost)
            return Math.multiplyExact(n, libCost);
        Graph forest = drawGraph(cities);

        // Count the connectable subgraphs
        Map<Node, Set<Node>> subgraphs = new HashMap<>();
        for (Node current : forest.values()) {
            if (!current.visited)
                buildPartition(current, subgraphs);
        }
        long isolateCityCount = n - forest.size();
        long metroCount = subgraphs.values().stream().distinct().count();
        long roadCount = n - isolateCityCount - metroCount; // no road needed for the "capitol" city
        metroCount += isolateCityCount; // each isolate must be a metro, too
        return Math.multiplyExact(metroCount, libCost) +
                Math.multiplyExact(roadCount, roadCost);
    }

    /* Count the cities connectable from this one. */
    private static void buildPartition(Node start, Map<Node, Set<Node>> subgraphs) {
        Deque<Node> queue = new ArrayDeque<>(Set.of(start));
        Set<Node> partition = new HashSet<>();
        while (!queue.isEmpty()) {
            Node current = queue.pop();
            if (current.visited) continue;
            current.visited = true;
            partition.add(current);
            subgraphs.put(current, partition);
            queue.addAll(current.neighbors);
        }
    }
    private static Graph drawGraph(List<List<Integer>> matrix) {
        Graph graph = new Graph();
        for (List<Integer> edge : matrix) {
            addEdge(edge.get(0), edge.get(1), graph);
        }
        return graph;
    }

    private static void addEdge(int source, int sink, Graph forest) {
        Node start = forest.addNode(source);
        Node end = forest.addNode(sink);
        start.addNeighbor(end);
    }

    /* Represent a city within the nation */
    private static class Node {
        Set<Node> neighbors = new HashSet<>();
        boolean visited = false;
        final int name;

        Node(int name) {
            this.name = name;
        }
        void addNeighbor(Node n) {
            neighbors.add(n);
            n.neighbors.add(this);
        }
        @Override
        public String toString() {
            return name + " " + " neighbors=" + neighbors.size();
        }
    }
    private static final class Graph extends HashMap<Integer, Node> {
        public Node addNode(int name) {
            return this.computeIfAbsent(name, Node::new);
        }
    }
}



/* Test Harness */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/tmp/input.txt"))));

        int q = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, q).forEach(qItr -> {
            try {
                String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

                int n = Integer.parseInt(firstMultipleInput[0]);

                int m = Integer.parseInt(firstMultipleInput[1]);

                int c_lib = Integer.parseInt(firstMultipleInput[2]);

                int c_road = Integer.parseInt(firstMultipleInput[3]);

                List<List<Integer>> cities = new ArrayList<>();

                IntStream.range(0, m).forEach(i -> {
                    try {
                        cities.add(
                                Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                        .map(Integer::parseInt)
                                        .collect(toList())
                        );
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                long result = Result.roadsAndLibraries(n, c_lib, c_road, cities);
                System.out.println(result);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
    }
}
