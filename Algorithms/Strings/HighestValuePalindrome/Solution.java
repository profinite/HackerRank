
/**
 * https://www.hackerrank.com/challenges/richie-rich/problem
 *
 * Fairly straight-forward
 */
class Result {
   public static String highestValuePalindrome(String s, int n, int k) {
        int surplus = budgetOf(s, k);
        if(surplus < 0)
            return "-1";
        StringBuilder largest = new StringBuilder(s);
        for(int i = 0, j = n - 1; i < n / 2; i++, j--) {
            int first = Character.getNumericValue(s.charAt(i));
            int last = Character.getNumericValue(s.charAt(j))
            int larger = Math.max(first, last);
            if(first == last) {
                if(first != 9 && surplus >= 2) {
                    larger = 9;
                    surplus -= 2;
                }
            } else if (surplus > 0 && larger != 9) {
                larger = 9;
                surplus--;
            }
            largest.setCharAt(i, Character.forDigit(larger, 10));
            largest.setCharAt(j, Character.forDigit(larger, 10));
        }
        if(n % 2 == 1 && surplus > 0) {
            int middle = n / 2;
            largest.setCharAt(middle, '9');
        } 
        return largest.toString();
    }
    static int budgetOf(String s, int k) {
        int n = s.length();
        for(int i = 0, j = n - 1; i < n / 2; i++, j--) {
            int first = Character.getNumericValue(s.charAt(i));
            int last = Character.getNumericValue(s.charAt(j))
            if(first != last)
                k--;
        }
        return k;
    }
}
