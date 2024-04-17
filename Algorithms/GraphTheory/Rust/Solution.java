
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * https://www.hackerrank.com/challenges/rust-murderer/problem
 */
public class Solution {
    static class Node {
        final int name;
        int visited = 0;
        int distance = 0;
        Set<Node> neighbors = new HashSet<>();
        public Node(int name) {
            this.name = name;
        }
        public boolean isFresh(Node target) {
            return visited != target.name;
        }
        public void add(Node n) {
            neighbors.add(n);
            n.neighbors.add(this);
        }
    }
    static Map<Integer, Node> all = new HashMap<>();
    static Integer[] rustMurderer(int n, int s, int[][] roads) {
        all.clear();
        all = IntStream.rangeClosed(1, n).mapToObj(Node::new).collect(Collectors.toMap(x -> x.name, x -> x));
        for (int[] road : roads) {
            int source = road[0];
            int sink = road[1];
            Node x1 = all.computeIfAbsent(source, Node::new);
            Node x2 = all.computeIfAbsent(sink, Node::new);
            x1.add(x2);
        }
        Node start = all.get(s);
        List<Integer> result = Stream.generate(() -> 1).limit(n).collect(Collectors.toList());
        for (Node current : start.neighbors)
             result.set(current.name - 1, distanceTo(start, current));
        result.remove(s - 1);
        return result.toArray(new Integer[0]);
    }
    static int distanceTo(Node start, Node target) {
        Deque<Node> queue = new ArrayDeque<>();
        queue.add(start);
        start.visited = target.name;
        while(!queue.isEmpty()) {
            Node current = queue.pop();
            if(current.equals(target))
                return current.distance;
            Set<Node> complement = complementOf(current, all.values().stream().filter(n -> n.isFresh(target)));
            if(complement.contains(target))
                return current.distance + 1;
            for(Node next : complement) {
                if(!next.isFresh(target)) continue;
                next.distance = current.distance + 1;
                next.visited = target.name;
                queue.add(next);
                // TODO: use a heap
            }
        }
        throw new IllegalStateException("Could not reach: " + target.name);
    }
    static Set<Node> complementOf(Node n, Stream<Node> all) {
        Set<Node> complement = all.collect(Collectors.toSet());
        complement.removeAll(n.neighbors);
        complement.remove(n);
        return complement;
    }
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        int t = Integer.parseInt(scanner.nextLine().trim());

        for (int tItr = 0; tItr < t; tItr++) {
            String[] nm = scanner.nextLine().split(" ");

            int n = Integer.parseInt(nm[0].trim());

            int m = Integer.parseInt(nm[1].trim());

            int[][] roads = new int[m][2];

            for (int roadsRowItr = 0; roadsRowItr < m; roadsRowItr++) {
                String[] roadsRowItems = scanner.nextLine().split(" ");

                for (int roadsColumnItr = 0; roadsColumnItr < 2; roadsColumnItr++) {
                    int roadsItem = Integer.parseInt(roadsRowItems[roadsColumnItr].trim());
                    roads[roadsRowItr][roadsColumnItr] = roadsItem;
                }
            }

            int s = Integer.parseInt(scanner.nextLine().trim());

            Integer[] result = rustMurderer(n, s, roads);
            System.out.println(Arrays.toString(result));
        }
    }
}
