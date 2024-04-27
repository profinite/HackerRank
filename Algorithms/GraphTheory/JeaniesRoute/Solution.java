import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/** 
 * Find the minimum distance a postman must travel in order
 * to deliver to letters to 'K' cities, with an "open-jaw" route
 * Similar to TSP, save we can begin or end anywhere.
 * https://www.hackerrank.com/challenges/jeanies-route/problem
 *
 * Great problem ⭐⭐⭐⭐⭐ but took me longer than an hour!
 * Most interesting aspect was finding the best starting position
 * to search from...
 * Here I do this in two stages:
 *   0) Build the graph
 *   1) "Plumb the depths" by calculating the Tree diameter via DFS
 *      https://codeforces.com/blog/entry/101271
 *   2) Prune the graph to the central trunk (diameter) plus the peripheral excursions ("periphery")
 *   3) Sum the distances
 * 
 *  𝚯(|N| + |E|) runtime for N nodes and E edges in the graph
 *
 * NOTE: Uses Java 17 features. Retrofit for HackerRank as needed.
 */

record Edge(Set<Node> nodes, int weight) { };
class Node {
    Set<Edge> edges = new HashSet<>();
    final int name;
    public int depth = 0;
    public boolean isDelivery = false;
    Node(int name) {
        this.name = name;
    }
    void addEdge(Edge e) {
        edges.add(e);
    }
    boolean isLeaf() {
        return edges.isEmpty();
    }
    Node follow(Edge e) {
        return e.nodes().stream().filter(i -> !i.equals(this)).findFirst().orElseThrow(IllegalStateException::new);
    }
}

public class Solution {

    Set<Edge> route = new HashSet<>();
    static int jeanisRoute(int[] k, int[][] roads) {
        return new Main().findRoute(k, roads);
    }
    public int findRoute(int[] k, int[][] roads) {
        Graph byteLand = drawGraph(roads);
        Node city = byteLand.get(k[0]);
        for(int name : k)
            byteLand.get(name).isDelivery = true;
        containsRoute(city);
        Node start = findDeepest(city);
        List<Edge> diametric = diameterOf(start);
        diametric.forEach(route::remove); // prune diametric cities
        int diameter = lengthOf(diametric);
        int periphery = lengthOf(route);
        return diameter + 2 * periphery;
    }
    /* Compute the diameter of the tree. */
    List<Edge> diameterOf(Node current) { // FIXME precompute the length
        List<Edge> diameter = new ArrayList<>();
          if (current.isLeaf())
              return diameter;
        int maxLength = 0;
        for(Edge e : current.edges) {
            Node next = current.follow(e);
            next.edges.remove(e); // destroy the graph as we run the route
            List<Edge> path = diameterOf(next); // FIXME me use leaf + edge as base case
            path.add(e);
            int length = lengthOf(path);
            if(length > maxLength) {
                diameter = path;
                maxLength = length;
            }
        }
        return diameter;
    }
    int lengthOf(Collection<Edge> path) {
        return path.stream().map(Edge::weight).reduce(0, Integer::sum);
    }
    /* Prune the tree to the given route.
     * Uses iterator to allow concurrent modify */
    boolean containsRoute(Node current) {
        boolean inRoute = current.isDelivery;
        Iterator<Edge> iterator = current.edges.iterator();
        while(iterator.hasNext()) {
            Edge e = iterator.next();
            Node next = current.follow(e);
            next.edges.remove(e);
            if(containsRoute(next)) {
                inRoute = true;
                next.edges.add(e);
                continue;
            }
            iterator.remove();
            route.remove(e);
        }
        return inRoute;
    }
    /* Compute the diameter of the tree via DFS */
    Node findDeepest(Node current) {
        if (current.edges.isEmpty()) { // base case
            assert(current.isDelivery);
            return current;
        }
        Node deepest = new Node(0);
        for (Edge e : current.edges) {
            Node child = current.follow(e);
            child.edges.remove(e);
            Node root = findDeepest(child);
            child.edges.add(e);
            root.depth += e.weight();
            if(root.depth > deepest.depth)
                deepest = root;
        }
        return deepest;
    }
    private Graph drawGraph(int[][] matrix) {
        Graph tree = new Graph();
        for (int[] edge : matrix)
            addEdge(edge[0], edge[1], edge[2], tree);
        return tree;
    }
    private void addEdge(int source, int sink, int weight, Graph tree) {
        List<Node> vertices = Stream.of(source, sink)
                .map(n -> tree.computeIfAbsent(n, k -> new Node(n)))
                .toList();
        Edge e = new Edge(new HashSet<>(vertices), weight);
        vertices.forEach(v -> v.addEdge(e));
        route.add(e);
    }
    private void removeEdge(Graph graph, Edge e) {
        Node x = e.nodes().iterator().next();
        Node y = x.follow(e);
        graph.get(x).edges.remove(e);
        graph.get(y).edges.remove(e);
    }
    private final static class Graph extends HashMap<Integer, Node> {};

    private static final Scanner scanner;

    static {
        File file = new File("/tmp/input05.txt");
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {

        String[] nk = scanner.nextLine().split(" ");

        int n = Integer.parseInt(nk[0].trim());

        int k = Integer.parseInt(nk[1].trim());

        int[] city = new int[k];

        String[] cityItems = scanner.nextLine().split(" ");

        for (int cityItr = 0; cityItr < k; cityItr++) {
            int cityItem = Integer.parseInt(cityItems[cityItr].trim());
            city[cityItr] = cityItem;
        }

        int[][] roads = new int[n-1][3];

        for (int roadsRowItr = 0; roadsRowItr < n-1; roadsRowItr++) {
            String[] roadsRowItems = scanner.nextLine().split(" ");

            for (int roadsColumnItr = 0; roadsColumnItr < 3; roadsColumnItr++) {
                int roadsItem = Integer.parseInt(roadsRowItems[roadsColumnItr].trim());
                roads[roadsRowItr][roadsColumnItr] = roadsItem;
            }
        }

        int result = jeanisRoute(city, roads);

        System.out.println(String.valueOf(result));
    }
}
