import java.math.*;
import java.util.*;
import static java.util.stream.Collectors.*;

/**
 * https://www.hackerrank.com/challenges/big-sorting/problem
 */
class Result {
    public static List<String> bigSorting(List<String> unsorted) {
        return unsorted.stream()
                .sorted(new BigIntComparator())
                .collect(toList());
    }
}
class BigIntComparator implements Comparator<String> {
    final int maxLength = String.valueOf(Long.MAX_VALUE).length();
    @Override
    public int compare(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        if(len1 == len2) {
            if(len1 < maxLength)
                return Long.compare(Long.parseLong(str1), Long.parseLong(str2));
            return new BigInteger(str1).compareTo(new BigInteger(str2));
        }
        else if(len1 > len2)
            return 1;
        else
            return -1;
    }
}
