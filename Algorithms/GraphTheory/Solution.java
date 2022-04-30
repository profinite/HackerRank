import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Solution {
    /*
     * Find how many edges are removable from this graph to leave each
     * tree with an even size (number of nodes).
     * 
     * Key insight: an even subtree is cuttable, but not an odd one.
     * 
     * 0(n) runtime, for n edges
     */
    static int evenForest(int t_nodes, int t_edges, List<Integer> starts, List<Integer> ends) {
        Map<Integer, Node> forest = createForest(starts, ends);
        Node root = forest.get(1);
        root.measure();
        // Now count the feasible cuts for even trees
        return countCuts(root);
    }
  
    /* Recursively count the cuts */
    private static int countCuts(Node root) {
        int count = 0;
        for(Node n : root.children) {
            if(n.isCuttable()) {
                count++;
            }
            count += countCuts(n);
        }
        return count;
    }
  
    /* Parse adjacency pair matrix into a graph. */
    static Map<Integer, Node> createForest(List<Integer> sources, List<Integer> sinks) {
        Map<Integer, Node> forest = new HashMap<>();
        for(int i = 0; i < sources.size(); i++) {
            Node source = growNode(sources.get(i), forest);
            Node sink = growNode(sinks.get(i), forest);
            source.children.add(sink);
        }
        return forest;
    }
  
    private static Node growNode(int j, Map<Integer, Node> forest) {
        forest.putIfAbsent(j, new Solution().new Node(j));
        return forest.get(j);
    }

    /* Inner class to represent each vertex */
    class Node {
        final int data;
        private int size;
        List<Node> children;
        Node(int data) {
            this.data = data;
            children = new ArrayList<>();
        }
        /* Tally the size of this subtree */
        public int measure() {
            size = children.stream().map(Node::measure).reduce(1, Integer::sum);
            return size;
        }
        public boolean isCuttable() {
            return size % 2 == 0;
        }
    }

  
  
  

    /* Standard Hackerrank boiler plate for Java. */
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        String[] tNodesEdges = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");
        int tNodes = Integer.parseInt(tNodesEdges[0]);
        int tEdges = Integer.parseInt(tNodesEdges[1]);
        List<Integer> tFrom = new ArrayList<>();
        List<Integer> tTo = new ArrayList<>();
        IntStream.range(0, tEdges).forEach(i -> {
            try {
                String[] tFromTo = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

                tFrom.add(Integer.parseInt(tFromTo[0]));
                tTo.add(Integer.parseInt(tFromTo[1]));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        int res = evenForest(tNodes, tEdges, tTo, tFrom);
        bufferedWriter.write(String.valueOf(res));
        bufferedWriter.newLine();
        bufferedReader.close();
        bufferedWriter.close();
    }
}
