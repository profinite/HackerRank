
/**
 * https://www.hackerrank.com/challenges/richie-rich/problem
 *
 * Fairly straight-forward
 */
class Result {
    public static String highestValuePalindrome(String s, int n, int k) {
        final String FAIL = "-1";
        StringBuilder result = new StringBuilder(s);
        int oddCount = palindromeCount(s);
        if(oddCount > k) return FAIL;
        int surplus = k - oddCount;
        for(int i = 0, j = n - 1; i < n / 2; i++, j--) {
            char start = s.charAt(i);
            char end = s.charAt(j);
            char larger = (char) Math.max((int)start, (int)end);
            if (start == end)
                if(start != '9' && surplus >= 2) {
                    larger = '9';
                    surplus -= 2;
                }
            else if(surplus > 0 && larger != '9') {
                larger = '9';
                surplus--;
                oddCount--;
            }
            else {
                oddCount--;
            }
            result.setCharAt(i, larger);
            result.setCharAt(j, larger);
        }
        return result.toString();
    }
    static private int palindromeCount(String s) {
        int count = 0;
        for(int i = 0, j = s.length() - 1; i < j / 2; i++, j--) {
            if(s.charAt(i) != s.charAt(j))
                count++;
        }
        return count;
    }
}
