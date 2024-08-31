import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import static java.util.stream.Collectors.toList;

class Result {
    static Set<String> observed = new HashSet<>();

    public static int jennysSubtrees(int n, final int r, List<List<Integer>> edges) {
        Graph g = drawGraph(edges);
        observed.clear();
        //System.out.println(edges);
        //System.out.println(g.values());

        if(g.values().size() == 2 || r == 0) {
            return 1;
        }
        int sum = 0;
        List<Node> sorted = new ArrayList<>(g.values());
        sorted.sort(Comparator.comparing(Node::degree, Comparator.reverseOrder()));
        for (Node node : g.values()) {
            if (isSubtree(node, r))
                sum++;
            g.values().forEach(Node::clear);
        }
        return sum;
    }

    // idea - ignore searching identical arms of the tree
    // find center of the tree
    static boolean isSubtree(Node current, int radius) {
        Set<Node> subtree = current.subtreeOf(radius);
       /* if(current.name == 28) {
            System.out.println("");
        } */
        final String canonical = canonize(subtree);
        if (observed.contains(canonical)) {
            //System.out.println("Isomorphism for node " + current.name + " : " + canonical);
            return false;
        }
        //System.out.println("Fresh morph for node " + current.name + " : " + canonical);
        observed.add(canonical);
        return true;
    }

    static String canonize(Set<Node> subtree) {
//        Deque<Node> leaves = subtree.stream().filter(Node::isLeaf).collect(Collectors.toCollection(ArrayDeque::new));
        Deque<Node> leaves = subtree.stream().filter(Node::isLeaf).collect(Collectors.toCollection(ArrayDeque::new));
        Deque<Node> next = new ArrayDeque<>();
        while (subtree.size() > 2) {
            while(!leaves.isEmpty()) {
                Node current = leaves.remove();
                subtree.remove(current);
                Node parent = current.parent();
                parent.canonicals.add(current.canonize());
                parent.adjacent.remove(current);
                current.clear();
                if (parent.isLeaf())
                    next.add(parent);
            }
            Deque<Node> tmp = leaves; // swap the queues
            leaves = next;
            next = tmp;
        }
//        String result = leaves.stream().map(Node::canonize).sorted().collect(Collectors.joining());
        String result;
        //if(subtree.stream().map(n -> n.name).anyMatch(n -> Set.of(7, 28).contains(n)))
            //System.out.println();
        if(subtree.size() == 2) {
            /*
            Node c1 = leaves.remove();
            Node c2 = leaves.remove();
            c1 = c2.catenate().length() > c1.catenate().length() ? c1 : c2;
            Stream<String> cat = Stream.of(c1.canonize(), c2.catenate()).sorted();
            */
            result = "00" + subtree.stream().map(Node::catenate).sorted().collect(Collectors.joining());
        } else {
            result = subtree.stream().findAny().orElseThrow().canonize();
        }
//        List<String> test = leaves.stream().map(Node::canonize).collect(toList());
//        Collections.sort(test);
//        if(!result.equals(String.join("", test)))
//            throw new IllegalStateException();
        subtree.forEach(Node::clear);
        return result;
    }
static class Node {
        void clear() {
            visited = false;
            adjacent.clear();
            canonicals.clear();
        }
    boolean visited = false;
    Set<Node> neighbors = new HashSet<>();
    Set<Node> adjacent = new HashSet<>();
    List<String> canonicals = new ArrayList<>();
    final int name;

    Node(int name) {
        this.name = name;
    }
    Node parent()  {
        return adjacent.stream().findFirst().orElseThrow();
    }

    Set<Node> subtreeOf(int radius) { // TODO: do BFS instead
        Set<Node> subtree = new HashSet<>();
        if(this.degree() == 1) { // special case where we start on an edge.
            Node parent = neighbors.stream().findFirst().orElseThrow();
            if(radius == 1) { // 2 node case
                parent.canonicals.add("01");
                return Set.of(parent);
            }
            return parent.subtreeOf(radius - 1);
        }
        visited = true;
        subtree.add(this);
        for (Node child : neighbors) {
            if (child.visited)
                continue;
            if (child.degree() <= 1 || radius <= 1) {
                canonicals.add("01");
            } else {
                adjacent.add(child);
                child.adjacent.add(this);
                subtree.addAll(child.subtreeOf(radius - 1));
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
    Graph graph = new Graph();
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

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/tmp/input.txt")));

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

        long start = System.currentTimeMillis();
        int result = Result.jennysSubtrees(n, r, edges);
        System.out.println(result);
        System.out.println((System.currentTimeMillis() - start) / 1000.0 + " sec");

        bufferedReader.close();
    }
}
