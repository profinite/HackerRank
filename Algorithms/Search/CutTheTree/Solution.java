import java.io.*;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Determine a single edge to cut so that the resulting trees have a minimal difference.
 * https://www.hackerrank.com/challenges/cut-the-tree/problem
 *
 * Follows three stages:
 * 1) "normalizes" the graph into a directed, rooted tree
 * 2) Computes the sums from the root
 * 3) Finds the consequent difference, once cut.
 * These could be combined more for efficiency.
 *
 * 𝚯(n) runtime
 */
class Result {
    public static int cutTheTree(List<Integer> data, List<List<Integer>> edges) {
        Map<Integer, Node> forest = new HashMap<>();
        int name = 1;
        for(Integer d : data)
            forest.put(name, new Node(name++, d));

        for(List<Integer> edge : edges) {
            Node source = forest.get(edge.get(0));
            Node sink = forest.get(edge.get(1));
            source.children.add(sink);
            sink.children.add(source);
        }
        Node start = forest.get(1);
        start.normalize(start);
        int total = start.sum();
        return start.minSpread(total);
    }
    static class Node {
        final int name;
        final int value;
        private int cache = -1;
        final Set<Node> children = new HashSet<>();
        public Node(int name, int value) {
            this.name = name;
            this.value = value;
        }
        public int sum() {
            if(cache == -1)
                cache = children.stream().map(Node::sum).reduce(value, Integer::sum);
            return cache;
        }
        public void normalize(Node parent) {
            children.remove(parent);
            children.forEach(n -> n.normalize(this));
        }
        public int minSpread(int total) {
            int min = children.stream().filter(Node::isParent).map(n -> n.minSpread(total)).min(Comparator.naturalOrder()).orElse(Integer.MAX_VALUE);
            for(Node child : children) {
                int diff = Math.abs(total - 2 * child.sum());
                min = Math.min(min, diff);
            }
            return min;
        }
        private boolean isParent() {
            return !children.isEmpty();
        }

    }
}

/* HackerRank Boilerplate with increased call stack */
public class Solution implements Runnable {
    public static void main(String[] args) {
        new Thread(null, new Solution(), "morecalls", 1<<26).start();
    }
    public void run() {
        String[] args = {"", ""};
        try {
            SolutionOld.main(args);
        } catch (IOException e) {
            throw new IllegalArgumentException("bad IO");
        }
    }
}
class SolutionOld {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> data = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Integer::parseInt)
                .collect(toList());

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

        int result = Result.cutTheTree(data, edges);
        System.out.println(result);

        bufferedReader.close();
    }
}
