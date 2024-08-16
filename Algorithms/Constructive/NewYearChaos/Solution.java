
    /**
     * Find the minimum bribes for queue-jumpers at an event:
     * https://www.hackerrank.com/challenges/new-year-chaos/problem
     *
     * @param q final ordering of the queue.
     *          ----
     *          Steps over queue of people from front to end, counting those whose rank exceeds
     *          the current person. Reports if jump exceeds a certain threshold. 1-based index.
     *          
     *          𝚯(n log n) runtime
     *          𝚯(n) space
     */
    public static void minimumBribes(List<Integer> q) {
        TreeSet<Integer> observed = new TreeSet<>();
        long bribes = 0;
        int index = 1;
        for (Integer person : q) {
            // Count the queue-jumpers observed ahead of this person.
            bribes += observed.tailSet(person, false).size();
            observed.add(person);
            final int MAX_JUMP = 2;
            if (person - index > MAX_JUMP) {
                System.out.println("Too chaotic");
                System.out.flush();
                return;
            }
            index++;
        }
        System.out.println(bribes);
    }
