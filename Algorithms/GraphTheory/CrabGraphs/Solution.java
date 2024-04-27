import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/* Determine how many "Crab" subgraphs can be induced in a given undirected graph.
 * https://www.hackerrank.com/challenges/crab-graphs/problem
 *
 * This one is fun!! Good 20min interview question ⭐⭐⭐⭐
 * A crab is identical to a "claw":
 * https://mathworld.wolfram.com/ClawGraph.html
 *
 * 𝚯(n) runtime for graphs of N vertices.
 */
class Result {
    /* Construct crab graph from pendants. */
    public static int crabGraphs(int n, int t, List<List<Integer>> graph) {
        Map<Integer, Node> forest = drawGraph(graph);
        int isolates = 0;
        for (Node foot : forest.values()) {
            if (foot.degree > 1) continue;
            assert (foot.neighbors.size() == 1);
            Node head = foot.neighbors.iterator().next();
            if (head.crabMates.size() < t) {
                foot.addCrabmate(head);
                head.addCrabmate(foot);
            } else {
                System.out.println("Not enough room for this pendant vertex! " + head.name + " " + head);
                isolates++;
            }
        }
        return forest.size() - isolates;
    }

    private static Graph drawGraph(List<List<Integer>> matrix) {
        Graph graph = new Graph();
        for (List<Integer> edge : matrix)
            addEdge(edge.get(0), edge.get(1), graph);
        return graph;
    }
    private static void addEdge(int source, int sink, Graph forest) {
        forest.putIfAbsent(source, new Node(source));
        forest.putIfAbsent(sink, new Node(sink));
        forest.get(source).addNeighbor(forest.get(sink));
        forest.get(sink).addNeighbor(forest.get(source));
    }

    /* Crude typedef (controversial) */
    static final class Graph extends HashMap<Integer, Node> {};

}

class Node {
    @Override
    public String toString() {
        return name + " " + " deg=" + degree + " " + " crabs: " + crabMates.size() + " neighbors=" + neighbors.size();
    }

    Set<Node> neighbors = new HashSet<>();
    public Set<Node> crabMates = new HashSet<>();
    public int degree = 0;
    final int name;

    Node(int name) {
        this.name = name;
    }

    boolean isPendant() {
        return degree == 1;
    }

    void addNeighbor(Node n) {
        neighbors.add(n);
        degree++;
    }

    void addCrabmate(Node n) {
        assert (neighbors.contains(n));
        crabMates.add(n);
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        Path inputFile = Paths.get("/tmp/input.txt");
        BufferedReader bufferedReader = Files.newBufferedReader(inputFile);

        int c = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, c).forEach(cItr -> {
            try {
                String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

                int n = Integer.parseInt(firstMultipleInput[0]);
                int t = Integer.parseInt(firstMultipleInput[1]);
                int m = Integer.parseInt(firstMultipleInput[2]);

                List<List<Integer>> graph = new ArrayList<>();

                IntStream.range(0, m).forEach(i -> {
                    try {
                        graph.add(
                                Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                        .map(Integer::parseInt)
                                        .collect(toList())
                        );
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                int result = Result.crabGraphs(n, t, graph);
                System.out.println("Value is: " + result);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
    }
}
