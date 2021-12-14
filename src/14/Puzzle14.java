import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Puzzle14 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/14/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            String template = lines.get(0);

            Map<String, String> rules = lines.stream()
                    .filter(l -> l.contains(" -> "))
                    .collect(Collectors.toMap(l -> l.split(" -> ")[0],
                            l -> l.split(" -> ")[0].charAt(0) + l.split(" -> ")[1]));

            long diff = runSteps(template, rules, 10);

            System.out.println(diff);
            System.out.println(diff == Long.parseLong(expected1));

            diff = runSteps(template, rules, 40);
            System.out.println(diff);
            System.out.println(diff == Long.parseLong(expected2));

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    private static Long runSteps(String template, Map<String, String> rules, int steps) {
        Map<String, Long> pairCount = new HashMap<>();
        Map<Character, Long> charCount = new HashMap<>();
        for (int i = 0; i < template.length() - 1; i++) {
            charCount.merge(template.charAt(i), 1l, (a, b) -> a + 1);

            String substr = template.substring(i, i + 2);
            pairCount.merge(substr, 1l, (a, b) -> a + 1);
        }
        charCount.merge(template.charAt(template.length() - 1), 1l, (a, b) -> a + 1);

        for (int step = 0; step < steps; step++) {
            Map<String, Long> newPairCount = new HashMap<>(pairCount);
            for (Entry<String, Long> pair : pairCount.entrySet()) {
                String pairKey = pair.getKey();
                if (rules.containsKey(pairKey)) {
                    String rightPairKey = rules.get(pairKey);
                    String leftPairKey = String.valueOf(rightPairKey.charAt(1)) + String.valueOf(pairKey.charAt(1));
                    long count = pair.getValue();
                    newPairCount.merge(pairKey, 0l, (a, b) -> a - count);
                    newPairCount.merge(rightPairKey, count, (a, b) -> a + count);
                    newPairCount.merge(leftPairKey, count, (a, b) -> a + count);
                    charCount.merge(rightPairKey.charAt(1), count, (a, b) -> a + count);
                }
            }
            pairCount = newPairCount;
        }
        long max = charCount.values().stream().max(Long::compareTo).get();
        long min = charCount.values().stream().min(Long::compareTo).get();
        return max - min;
    }
}
