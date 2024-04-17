import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

/**
 * Naive attempt at Prim's algorithm for MST
 * https://www.hackerrank.com/challenges/primsmstsub/
 * 𝚯(E + V log V) runtime
 */
class Result {
public static int prims(int n, List<List<Integer>> edges, int start) {
    Graph graph = new Graph();
    edges.forEach(e -> graph.drawEdge(e.get(0), e.get(1), e.get(2)));
    return spanningCost(graph.get(start));
}
    static int spanningCost(Node start) {
        Queue<Edge> queue = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        queue.addAll(start.edges);
        start.visited = true;
        int weight = 0;
        while (!queue.isEmpty()) {
            Edge current = queue.remove();
            if(current.next().isPresent())
                weight += addEdge(current, queue);
        }
        return weight;
    }
    /** Add this edge if the next Node is new to the tree */
    static int addEdge(Edge current, Queue<Edge> q) {
        current.prune();
        Node next = current.next().get();
        q.addAll(next.edges);
        next.visited = true;
        return current.weight;
    }
}
/** Simulate an undirected graph */
final class Graph extends HashMap<Integer, Node> {
    void drawEdge(int n1, int n2, int weight) {
        new Edge(computeIfAbsent(n1, Node::new), computeIfAbsent(n2, Node::new), weight);
    }
}
class Edge implements Comparable<Edge> {
    final int weight;
    final Set<Node> vertices;

    public Edge(Node source, Node sink, int weight) {
        this.weight = weight;
        vertices = Set.of(source, sink);
        vertices.forEach(v -> v.edges.add(this));
    }
    public void prune() {
        vertices.forEach(n -> n.edges.remove(this));
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }
    public Optional<Node> next() {
        return vertices.stream().filter(n -> !n.visited).findFirst();
    }
}
/** Simulate a node (vertex) within a graph */
class Node {
    boolean visited = false;
    final int name;
    Set<Edge> edges = new HashSet<>();
    public Node(int name) {
        this.name = name;
    }
}



/* HackerRank Boilerplate */
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
        int n = Integer.parseInt(firstMultipleInput[0]);
        int m = Integer.parseInt(firstMultipleInput[1]);
        List<List<Integer>> edges = new ArrayList<>();

        IntStream.range(0, m).forEach(i -> {
            try {
                edges.add(
                        Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                .map(Integer::parseInt)
                                .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        int start = Integer.parseInt(bufferedReader.readLine().trim());

        int result = Result.prims(n, edges, start);
        System.out.println(result);


        bufferedReader.close();
    }
}
