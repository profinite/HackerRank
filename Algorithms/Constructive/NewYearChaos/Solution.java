
    /**
     * Find the minimum bribes for queue-jumpers at an event:
     * https://www.hackerrank.com/challenges/new-year-chaos/problem
     *
     * @param q final ordering of the queue.
     *          ----
     *          Steps over queue of people from front to end, counting those whose rank exceeds
     *          the current person. Reports if jump exceeds a certain threshold.
     *          <p>
     *          𝚯(n log n) runtime
     *          𝚯(n) space
     */
    public static void minimumBribes(List<Integer> q) {
        TreeSet<Integer> sorted = new TreeSet<>();
        long bribes = 0;
        int index = 0;
        for (Integer person : q) {
            bribes += sorted.tailSet(person, false).size();
            sorted.add(person);
            final int MAX_JUMP = 3;
            if (person - index > MAX_JUMP) {
                System.out.println("Too chaotic");
                System.out.flush();
                return;
            }
            index++;
        }
        System.out.println(bribes);
    }
