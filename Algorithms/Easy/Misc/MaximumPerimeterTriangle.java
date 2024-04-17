// https://www.hackerrank.com/challenges/maximum-perimeter-triangle/
class Result {
    public static List<Integer> maximumPerimeterTriangle(List<Integer> sticks) {
        Collections.sort(sticks);
        Collections.reverse(sticks);
        List<Integer> triangle = List.of(-1);
        for(int i = 0; i < sticks.size() - 2; i++) {
            int hypotenuse = sticks.get(i);
            int leg1 = sticks.get(i + 1);
            int leg2 = sticks.get(i + 2);
            if(leg1 + leg2 > hypotenuse) {
                triangle = List.of(leg2, leg1, hypotenuse);
                break;
            }
        }
        return triangle;
    }
}
