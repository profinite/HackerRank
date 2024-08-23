import javax.swing.text.Position;
import java.io.*;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class Result {
    /**
     * Find the minimum cost from A to B, but as a bitwise OR rather than sum.
     * Also includes loops and multi-edges.
     * https://www.hackerrank.com/challenges/beautiful-path/
     *
     * Here we simply use the shortest path algorithm but compute the running
     * cost with bitwise OR.
     *
     * @param edges - the graph in source, sink format
     * @param A - start
     * @param B - end
     * @return the minimum cost from A to B
     * @author Tyler de L <tyler@delaguna.org>
     */
    public static int beautifulPath(List<List<Integer>> edges, int A, int B) {
        Graph graph = new Graph();
        edges.forEach(e -> graph.drawEdge(e.get(0), e.get(1), e.get(2)));
        return (int) shortestPath(graph.get(A), graph.get(B));
    }

    static long shortestPath(Node start, Node target) {
        PriorityQueue<Position> queue = new PriorityQueue<>(Comparator.comparingLong(p -> p.cost));
        queue.add(new Position(start, 0, new ArrayList<>()));
        start.visited = Node.VISITED;
        while (!queue.isEmpty()) {
            Position current = queue.remove();
            current.node.visited |= current.cost;
            if (current.node.equals(target)) {
                System.out.println(current.path.size());
                current.path.sort(Comparator.comparingLong(e -> Collections.min(e.vertices.values(), Comparator.comparingInt((Node v) -> v.name)).name));
                for (Edge e : current.path) {
                    List<Node> ns = e.vertices.values().stream().toList();
                    System.out.println(ns.get(0) + " " + ns.get(1) + " " + e.weight);
                }

                return current.cost;
            }
            for (Edge e : current.node.edges) {
                Position p = new Position(e.vertices.get(current.node), e.weight | current.cost, new ArrayList<>(current.path));
                p.path.add(e);
                if (!p.isVisited())
                    queue.add(p);
            }
        }
        throw new IllegalStateException("Couldn't find: " + target);
    }

    private record Position(Node node, long cost, List<Edge> path) {
        boolean isVisited() {
            return (node.visited & cost) == cost;
        }
    }

    /**
     * Simulate an undirected graph
     */
    private final static class Graph extends HashMap<Integer, Node> {
        void drawEdge(int n1, int n2, int weight) {
            new Edge(computeIfAbsent(n1, Node::new), computeIfAbsent(n2, Node::new), weight);
        }
    }

    private static class Edge {
        final int weight;
        final Map<Node, Node> vertices = new HashMap<>();

        public Edge(Node source, Node sink, int weight) {
            this.weight = weight;
            if (source.equals(sink)) // ignore self-loops
                return;
            vertices.put(source, sink);
            vertices.put(sink, source);
            vertices.values().forEach(v -> v.edges.add(this));
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "weight=" + weight +
                    ", vertices=" + vertices +
                    '}';
        }
    }

    /**
     * Simulate a node (vertex) within a graph
     */
    private static class Node {
        final static long VISITED = 0xFFFF_FFFFL;
        long visited = 0;
        final int name;
        Set<Edge> edges = new HashSet<>();

        public Node(int name) {
            this.name = name;
        }
        @Override
        public String toString() {
            return '{' + name +
                ", visited=" + Long.toBinaryString(visited) +
                ", edges=" + edges.size() + '}';
        }
    }
}

class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/tmp/input.txt")));

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

        String[] secondMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int A = Integer.parseInt(secondMultipleInput[0]);

        int B = Integer.parseInt(secondMultipleInput[1]);

        int result = Result.beautifulPath(edges, A, B);

        System.out.println(result);
        BinaryTechniqueResult2.main(null);

        bufferedReader.close();
    }
}



class BinaryTechniqueResult2 {
    public static class Node {
        public boolean visited = false;
        public int penalty;
        public Node(int name) {
            this.name = name + 1;
        }
        int name;
        ArrayList<Edge> edges = new ArrayList<>();
        Set<Edge> path = new HashSet<>();
        @Override
        public String toString() {
            return "" + name;
        }
    }

    public static class Edge {
        public Node source, target;
        public int weight;

        public Edge(Node source, Node target, int weight) {
            this.source = source;
            this.target = target;
            this.weight = weight;
        }

        @Override
        public String toString() {
            List<Integer> nodes = List.of(source.name, target.name);
            return Collections.min(nodes) + " " + Collections.max(nodes) + " " + weight;
        }
    }

    static int N, M, mask;
    static ArrayList<Node> nodes;
    static Node start, finish;

    static boolean bfs(int number) {
        for (Node node : nodes) {
            node.visited = false;
            node.penalty = 0;
            node.path.clear();
        }
        LinkedList<Node> queue = new LinkedList<>();
        start.visited = true;
        queue.addFirst(start);

        while (!queue.isEmpty()) {
            Node node = queue.pollFirst();
            for (Edge edge : node.edges) {
                if ((edge.weight & ~number) != 0) continue;
                if (edge.target.visited) continue;
                edge.target.visited = true;

                edge.target.penalty = edge.source.penalty | edge.weight;
                if (edge.target == finish) {
                    List<Edge> ns = node.path.stream().distinct().sorted(Comparator.comparingLong(e -> Math.min(e.source.name, e.target.name))).toList();
                    ns.forEach(System.out::println);
                    return true;
                }
                edge.target.path.addAll(node.path);
                edge.target.path.add(edge);
                queue.addLast(edge.target);
            }
        }

        return false;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("/tmp/input.txt"));
        N = scanner.nextInt();
        M = scanner.nextInt();

        nodes = new ArrayList<>();
        for (int i = 0; i < N; i++) nodes.add(new Node(i));

        Edge edge;
        for (int i = 0; i < M; i++) {
            Node source = nodes.get(scanner.nextInt() - 1);
            Node destination = nodes.get(scanner.nextInt() - 1);
            int weight = scanner.nextInt();
            edge = new Edge(source, destination, weight);
            source.edges.add(edge);
            edge = new Edge(destination, source, weight);
            destination.edges.add(edge);
            mask |= weight;
        }
        start = nodes.get(scanner.nextInt() - 1);
        finish = nodes.get(scanner.nextInt() - 1);

        for (int i = 1; i <= mask; i++) {
            if (bfs(i)) {
                System.out.println(finish.penalty);
                return;
            }
        }
        System.out.println(-1);
    }
}
