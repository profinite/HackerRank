import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;

class Result {

    /*
     * Find the shortest path through a "Chutes & Ladders"
     * game, if every dice throw is optimal.
     * Return -1 if unreachable.
     *
     * Note: A simple problem, solvable with BFS on directed
     * graphs and accounting for cycles.
     * https://www.hackerrank.com/challenges/the-quickest-way-up
     * 
     */
    public static int quickestWayUp(List<List<Integer>> ladders, List<List<Integer>> snakes) {
        List<Node> digraph = createForest(100);
        addClique(digraph, ladders);
        addClique(digraph, snakes);
        return searchMin(digraph);
    }
    /* Find shortest path from origin to destination, via Breadth-First Search.
     * Track the number of "rolls" (distance) for each future square. */
    private static int searchMin(List<Node> graph) {
        final Node origin = graph.get(1);
        final Node destination = graph.get(100);
        Map<Node, Integer> atlas = new HashMap<>();
        Set<Node> alreadyVisited = new HashSet<>();
        Deque<Node> queue = new ArrayDeque<>(List.of(origin));
        atlas.put(origin, 0);

        do {
            Node current = queue.getFirst();
            if(current.equals(destination)) {
                return atlas.get(current);
            }
            alreadyVisited.add(current);
            queue.addAll(current.neighborsOf(graph));
            queue.removeAll(alreadyVisited);
            current.neighbors.forEach(i -> 
                    atlas.putIfAbsent(graph.get(i), atlas.get(current) + 1));
        } while (!queue.isEmpty());

        return -1;
    }
    
    /* Update the forest with a new 'clique' (chute or ladder) */
    private static void addClique(List<Node> forest, List<List<Integer>> cliques) {
        for(List<Integer> clique : cliques) {
            Node start = forest.get(clique.get(0));
            Node end = forest.get(clique.get(1));
            forest.set(start.order, end);
        }
    }
    /* Create a forest of digraphs given the list of edges */
    private static List<Node> createForest(int n) {
        List<Node> forest = IntStream.rangeClosed(0, n).mapToObj(Node::new).collect(toList());
        final int DICE_SIZE = 6;
        for(Node node : forest) {
            final int current = node.order;
            final int maxRoll = current + DICE_SIZE;
            for(int i = current + 1; i <= Math.min(maxRoll, n); i++) {
                node.neighbors.add(i);
            }
        }
        return forest;
    }
    /* Represent a square of the board. */
    private static class Node {
        Set<Integer> neighbors;
        final int order;
        Node(int order) {
            this.order = order;
            neighbors = new HashSet<>();
        }
        List<Node> neighborsOf(List<Node> graph) {
            return neighbors.stream().map(graph::get).collect(toList());
        }
    }
}





/* ---------------------
 * HackerRank Boilerplate
 * ----------------------*/

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int t = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, t).forEach(tItr -> {
            try {
                int n = Integer.parseInt(bufferedReader.readLine().trim());

                List<List<Integer>> ladders = new ArrayList<>();

                IntStream.range(0, n).forEach(i -> {
                    try {
                        ladders.add(
                                Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                        .map(Integer::parseInt)
                                        .collect(toList())
                        );
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                int m = Integer.parseInt(bufferedReader.readLine().trim());

                List<List<Integer>> snakes = new ArrayList<>();

                IntStream.range(0, m).forEach(i -> {
                    try {
                        snakes.add(
                                Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                        .map(Integer::parseInt)
                                        .collect(toList())
                        );
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                int result = Result.quickestWayUp(ladders, snakes);

                bufferedWriter.write(String.valueOf(result));
                bufferedWriter.newLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
        bufferedWriter.close();
    }
}
