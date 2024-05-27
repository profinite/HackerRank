    public static String caesarCipher(String s, int k) {
        final String alphabet = "abcdefghijklmnopqrstuvwxyz";
        final int length = alphabet.length();
        Map<Character, Character> caesar = range(0, length).boxed()
                .collect(toMap(alphabet::charAt, x -> alphabet.charAt(x + k % length)));
        for (Character c : caesar.keySet()) {
            Character d = caesar.get(c);
            caesar.put(Character.toUpperCase(c), Character.toUpperCase(d));
        }
        return s.chars().mapToObj(i -> (char) i)
                .map(c -> caesar.getOrDefault(c, c))
                .map(String::valueOf)
                .collect(joining());
    }

