import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Log distance from a starter node to all its reachable endpoints,
     * given a constant edge weight. Return -1 for unreachable nodes.
     *
     * Note: Simple problem with (slightly) awkward output
     *
     * 𝚯(n) runtime for n edges
     * 𝚯(n + m) space complexity for m edges
     * 
     * https://www.hackerrank.com/challenges/bfsshortreach
     */
    public static List<Integer> bfs(int n, int m, List<List<Integer>> edges, int s) {
        List<Node> forest = createForest(edges, n);
        Map<Node, Integer> atlas = searchAll(s, forest);
        forest.remove(0);
        final int unreachable = -1;
        return forest.stream().map(i -> atlas.getOrDefault(i, unreachable)).collect(toList());
    }
  
    /* Tally up the distances for all nodes reachable from S, via Breadth-First Search. */
    private static Map<Node, Integer> searchAll(int s, List<Node> forest) {
        final int EDGE_WEIGHT = 6;
        Map<Node, Integer> atlas = new HashMap<>();
        Set<Node> alreadyVisited = new HashSet<>();      
        final Node origin = forest.get(s);
        Deque<Node> queue = new ArrayDeque<>(List.of(origin));
      
        atlas.put(origin, 0);
        do {
            Node current = queue.getFirst();
            alreadyVisited.add(current);
            queue.addAll(current.neighbors);
            queue.removeAll(alreadyVisited);
            current.neighbors.forEach(n -> atlas.putIfAbsent(n, atlas.get(current) + EDGE_WEIGHT));
        } while (!queue.isEmpty());

        forest.remove(origin);
        return atlas;
    }
  
    /* Create a forest of graphs given the list of edges */
    private static List<Node> createForest(List<List<Integer>> edges, int n) {
        List<Node> forest = IntStream.rangeClosed(0, n).mapToObj(Node::new).collect(toList());
        for(List<Integer> edge : edges) {
            Node a = forest.get(edge.get(0));
            Node b = forest.get(edge.get(1));
            a.neighbors.add(b);
            b.neighbors.add(a);
        }
        return forest;
    }
    /* A Record suffices here for Java 16+ */
    private static class Node {
        Set<Node> neighbors;
        final int order;
        Node(int order) {
            this.order = order;
            neighbors = new HashSet<>();
        }
    }
}





/* ---------------------
 * HackerRank Boilerplate 
 * ----------------------*/
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        int q = Integer.parseInt(bufferedReader.readLine().trim());
        IntStream.range(0, q).forEach(qItr -> {
            try {
                String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
                int n = Integer.parseInt(firstMultipleInput[0]);
                int m = Integer.parseInt(firstMultipleInput[1]);
                List<List<Integer>> edges = new ArrayList<>();
                IntStream.range(0, m).forEach(i -> {
                    try {
                        edges.add(Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                        .map(Integer::parseInt)
                                        .collect(toList()));
                    } catch (IOException ex) { throw new RuntimeException(ex); }
                });
                int s = Integer.parseInt(bufferedReader.readLine().trim());
                List<Integer> result = Result.bfs(n, m, edges, s);
                bufferedWriter.write(result.stream().map(Object::toString).collect(joining(" ")) + "\n");
            } catch (IOException ex) { throw new RuntimeException(ex); }
        });
        bufferedReader.close();
        bufferedWriter.close();
    }
}
