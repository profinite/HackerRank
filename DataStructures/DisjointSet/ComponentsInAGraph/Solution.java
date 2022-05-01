import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


class Result {

    /*
     * Detect the smallest and largest subgraphs within a forest. No particular
     * algorithm here, just be sure to only visit each node once for success.
     * 
     * 0(n) runtime since each node is only visited once.
     *
     */
    public static List<Integer> componentsInGraph(List<List<Integer>> gb) {
        final Map<Integer, Node> forest = growForest(gb);
        
        /* Compute sizes of each connected subgraph. */
        List<Integer> sizes = forest.values()
            .stream()
            .filter(n -> !n.isVisited)
            .map(Node::size)
            .collect(Collectors.toCollection(LinkedList::new));
            
        return Stream.of(Collections.min(sizes), Collections.max(sizes)).collect(toList());
    }
    
    /* Represent the adjacency pairs as a graph of Nodes
     * Link each source and destination node pair. */
    private static Map<Integer, Node> growForest(List<List<Integer>> gb) {
        Map<Integer, Node> m = new LinkedHashMap<>();
        for(List<Integer> edge : gb) {
            final int origin = edge.get(0);
            final int dest = edge.get(1);
            Stream.of(origin, dest).forEach(i -> m.putIfAbsent(i, new Node(i)));
            
            m.get(origin).link(m.get(dest));
            m.get(dest).link(m.get(origin));
        }
        return m;
    }
}

class Node {
    final int data;
    Set<Node> adjacents = new HashSet<>();
    boolean isVisited = false;
    public Node(int data) {
        this.data = data;
    }
    public boolean link(Node n) {
        return adjacents.add(n);
    }
    /* Compute size of the subgraph */
    public Integer size() {
        if(isVisited) 
            return 0;
        isVisited = true;
        return adjacents.stream()
            .map(Node::size)
            .reduce(1, Integer::sum);
    }
}


/* Standard Java boilerplate, with call stack extended. */
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
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<List<Integer>> gb = new ArrayList<>();

        IntStream.range(0, n).forEach(i -> {
            try {
                gb.add(
                    Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                        .map(Integer::parseInt)
                        .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        List<Integer> result = Result.componentsInGraph(gb);

        bufferedWriter.write(
            result.stream()
                .map(Object::toString)
                .collect(joining(" "))
            + "\n"
        );

        bufferedReader.close();
        bufferedWriter.close();
    }
}
