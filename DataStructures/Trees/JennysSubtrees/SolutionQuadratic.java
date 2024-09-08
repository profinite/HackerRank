import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;



class Result {
    static final Set<String> observed = new HashSet<>();
    static Graph g;

    public static int jennysSubtrees(int n, final int r, List<List<Integer>> edges) {
        g = drawGraph(edges);
        //observed.clear();
        //System.out.println(edges);
        //System.out.println(g.values());

        if(g.values().size() == 2 || r == 0) {
            return 1;
        }
        int sum = 0;
        for (Node node : g.values()) {
            if (isSubtree(node, r))
                sum++;
        //    g.values().forEach(Node::clear);
        }
        return sum;
    }

    // idea - ignore searching identical arms of the tree
    // find center of the tree
    static boolean isSubtree(Node current, int radius) {
        final Set<Integer> subtree = current.subtreeOf(radius);
       /* if(current.name == 28) {
            System.out.println("");
        } */
        final String canonical = canonize(subtree);
        if (observed.contains(canonical)) {
            //System.out.println("Isomorphism for node " + current.name + " : " + canonical);
            return false;
        }
        observed.add(canonical);
        return true;
    }
    static String canonize(Set<Integer> subtree) {
        Deque<Node> leaves = subtree.stream().map(g::get).filter(Node::isLeaf).collect(Collectors.toCollection(ArrayDeque::new));
        Deque<Node> next = new ArrayDeque<>();
        while (subtree.size() > 2) {
            while(!leaves.isEmpty()) {
                Node current = leaves.remove();
                subtree.remove(current.name);
                Node parent = current.parent();
                parent.canonicals.add(current.canonize());
                parent.adjacent.remove(current.name);
                current.clear();
                if (parent.isLeaf())
                    next.add(parent);
            }
            Deque<Node> tmp = leaves; // swap the queues
            leaves = next;
            next = tmp;
        }
        String result;
        if(subtree.size() == 2) {
            result = "00" + subtree.stream().map(g::get).map(Node::catenate).sorted().collect(Collectors.joining());
        } else {
            result = subtree.stream().map(g::get).findAny().orElseThrow().canonize();
        }
        subtree.forEach(i -> g.get(i).clear());
        return result;
    }
    static class Node {
        void clear() {
            visited = false;
            adjacent.clear();
            canonicals.clear();
            radius = 0 ; // unneeded in theory
        }
        boolean visited = false;
        int radius = 0;
        final Set<Node> neighbors = new HashSet<>();
        final Set<Integer> adjacent = new HashSet<>();
        final List<String> canonicals = new ArrayList<>();
        final int name;

        Node(int name) {
            this.name = name;
        }
        Node parent()  {
            return g.get(adjacent.iterator().next());
        }

        Set<Integer> subtreeOf(int radius) { // TODO: do BFS instead
            this.radius = radius;
            if(this.degree() == 1) { // special case where we start on an edge.
                Node parent = neighbors.iterator().next();
                if(radius == 1) { // 2 node case
                    parent.canonicals.add("01");
                    return Set.of(parent.name);
                }
                return parent.subtreeOf(radius - 1);
            }
            final Set<Integer> subtree = new HashSet<>();
            final Queue<Node> queue = new ArrayDeque<>();
            queue.add(this);
            while(!queue.isEmpty()) {
                Node current = queue.remove();
                if(current.visited) continue;// throw new IllegalStateException("mismatch");
                current.visited = true;
                subtree.add(current.name);
                for (Node child : current.neighbors) {
                    if (child.visited)
                        continue;
                    if (child.degree() <= 1 || current.radius <= 1) {
                        current.canonicals.add("01");
                    } else {
                        current.adjacent.add(child.name);
                        child.adjacent.add(current.name);
                        child.radius = current.radius - 1;
                        queue.add(child);
                    }
                }
            }
            return subtree;
        }

        String canonize() {
            if (!isLeaf())
                throw new IllegalStateException("Canonizing non-leaf");
            return "0" + catenate() + "1";
        }
        String catenate() {
            Collections.sort(canonicals);
            return String.join("", canonicals);
        }

        int degree() {
            return neighbors.size();
        }

        boolean isLeaf() {
            return adjacent.size() <= 1;
        }

        @Override
        public String toString() {
            return "{" + name + " => " + neighbors.stream().map(n -> n.name).map(n -> n + ", ").collect(Collectors.joining()) + '}';
        }
    }

    private static Graph drawGraph(List<List<Integer>> matrix) {
        final Graph graph = new Graph();
        for (List<Integer> edge : matrix)
            addEdge(edge.get(0), edge.get(1), graph);
        return graph;
    }

    private static void addEdge(int source, int sink, Graph forest) {
        forest.putIfAbsent(source, new Node(source));
        forest.putIfAbsent(sink, new Node(sink));
        forest.get(source).neighbors.add(forest.get(sink));
        forest.get(sink).neighbors.add(forest.get(source));
    }

    /* Crude typedef (controversial) */
    private static final class Graph extends HashMap<Integer, Node> {}
}


public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int r = Integer.parseInt(firstMultipleInput[1]);

        List<List<Integer>> edges = new ArrayList<>();

        IntStream.range(0, n - 1).forEach(i -> {
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

        int result = Result.jennysSubtrees(n, r, edges);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
