class Result {
    /**
     * Find the number of squares in a given range (trivial)
     * https://www.hackerrank.com/challenges/sherlock-and-squares/problem
     */
    public static int squares(int a, int b) {
        return Stream.of(a, b).map(Math::sqrt)
            .map(Math::ceil)
            .mapToLong(Math::round)
            .mapToInt(Math::toIntExact)
            .reduce(0, (x, y) -> y - x);
    }
    // alternative (redundant but more readable perhaps)
    public static int squares2(int a, int b) {
        double start = Math.ceil(Math.sqrt(a));
        double end = Math.floor(Math.sqrt(b));
        return Math.toIntExact(Math.round(end - start + 1));
    }
}

// HackerRank boilerplate
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int q = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, q).forEach(qItr -> {
            try {
                String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

                int a = Integer.parseInt(firstMultipleInput[0]);

                int b = Integer.parseInt(firstMultipleInput[1]);

                int result = Result.squares(a, b);

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
