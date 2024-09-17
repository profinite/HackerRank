    public static int diagonalDifference(List<List<Integer>> arr) {
        final int N = arr.size();
        int RL = IntStream.range(0, N).map(i -> arr.get(i).get(i)).sum();
        int LR = IntStream.range(0, N).map(i -> arr.get(i).get(N - i - 1)).sum();
        return Math.abs(RL - LR);
    }
