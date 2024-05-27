
class Result {
  /**
   * Find minimum alterations to convert first and second halves of a
   * string into anagrams.
   * https://www.hackerrank.com/challenges/anagram/
   *
   * Fairly straightforward. TODO: Simplify, DRY
   */
    public static int anagram(String s) {
        int mid = s.length() / 2;
        if(s.length() % 2 == 1)
            return -1;
        Map<Integer, Long> first = histogramOf(s.substring(0, mid));
        Map<Integer, Long> last = histogramOf(s.substring(mid));
        first.replaceAll((x, v) -> v - last.getOrDefault(x, 0L));
        return first.values()
                .stream()
                .filter(x -> x >= 1)
                .mapToInt(Math::toIntExact)
                .sum();
    }
    private static Map<Integer, Long> histogramOf(String s) {
        return s.chars()
                .boxed()
                .collect(Collectors.groupingBy(x -> x, Collectors.counting()));
    }
}
