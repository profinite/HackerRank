import java.util.*;
import java.util.stream.Stream;
import static java.util.stream.IntStream.range;

class Result {

    /**
     * Find the lowest cost to a destination train station "Rapture", with
     * an ongoing voucher for all future fares that undershoot previous ones.
     * https://www.hackerrank.com/challenges/jack-goes-to-rapture/
     * We apply Dijkstra's shortest path, looking for the lowest-cost leg.
     * Nodes are ordered by cost in a priority queue.
     * 𝚯(E + V log V) runtime
     */
    final static int INFINITY = Integer.MAX_VALUE;
    public static void getCost(int size, List<Integer> from, List<Integer> to, List<Integer> weights) {
        final int edges = to.size();
        Graph graph = new Graph();
        range(0, edges).forEach(i -> graph.addEdge(from.get(i), to.get(i), weights.get(i)));
        graph.putIfAbsent(size, new Node(size)); // test case #14
        int cost = shortestPath(graph.get(1), graph.get(size));
        if(cost == INFINITY)
            System.out.println("NO PATH EXISTS");
        else
            System.out.println(cost);
    }
    /** Apply a variant of Shortest Path.
     * Sort stations in a heap to accelerate runtime
     * @see <a href="https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm"></a>
     *
     * @param start Station we start at (1)
     * @param target Destination
     * @return Cost to reach the target
     **/
    static int shortestPath(Node start, Node target) {
        start.cost = 0;
        PriorityQueue<Node> queue = new PriorityQueue<>(Set.of(start));
        while(!queue.isEmpty()) {
            Node current = queue.remove();
            if(current.visited) continue;
            current.visited = true;
            if(current.equals(target))
                return target.cost;
            for(Node next : current.neighbors.keySet()) {
                current.update(next);
                queue.add(next);
            }
        }
        return target.cost;
    }
    /** Simulate an undirected graph */
    static class Graph extends HashMap<Integer, Node> {
        void addEdge(int source, int sink, int weight) {
            Node current = this.computeIfAbsent(source, Node::new);
            Node next = this.computeIfAbsent(sink, Node::new);
            current.addEdge(next, weight);
            next.addEdge(current, weight);
        }
    }

    /** Simulate a node (vertex) within a graph */
    static class Node implements Comparable<Node> {
        Map<Node, Integer> neighbors = new HashMap<>();
        boolean visited = false;
        int cost = INFINITY;
        final int name;
        public Node(int name) {
            this.name = name;
        }
        public void addEdge(Node n, int weight) {
            neighbors.merge(n, weight, Math::min);
        }
        public void update(Node next) {
            int weight = neighbors.get(next);
            int cost = Math.max(weight, this.cost);
            next.cost = Math.min(next.cost, cost);
            next.neighbors.remove(this);
        }
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.cost, other.cost);
        }
    }
}

