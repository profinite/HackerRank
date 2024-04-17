import java.util.List;
import static java.util.stream.Collectors.toList;

// Fairly straightforward
// https://www.hackerrank.com/challenges/palindrome-index/problem
class Result {
    public static int palindromeIndex(String s) {
        final int length = s.length();
        for (int i = 0; i < length / 2; i++) {
            int distal = length - i - 1;
            if (s.charAt(i) != s.charAt(distal)) {
                if (isPalindrome(s, i))
                    return i;
                else if (isPalindrome(s, distal))
                    return distal;
                break;
            }
        }
        return -1;
    }
    private static boolean isPalindrome(String str, int i) {
        String s = new StringBuilder(str).deleteCharAt(i).toString();
        int mid = s.length() / 2;
        String first = s.substring(0, mid);
        if(s.length() % 2 == 1)
            mid++;
        String last = s.substring(mid);
        return first.contentEquals(new StringBuilder(last).reverse());
    }
}
