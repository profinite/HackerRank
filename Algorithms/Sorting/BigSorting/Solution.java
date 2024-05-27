
class Result {
    /** Sort a large list of very large integers. 
      * https://www.hackerrank.com/challenges/big-sorting/
      *
      * Too large to convert all to BigInteger so 
      * we selectively dispatch them based on length. */
    public static List<String> bigSorting(List<String> unsorted) {
        return unsorted.stream()
                .sorted(Comparator.comparing(String::length)
                        .thenComparing(Result::compareTo))
                .collect(toList());
    }
    private static int compareTo(String s1, String s2) {
        return new BigInteger(s1).compareTo(new BigInteger(s2));
    }
}

