import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                    .collect(Collectors.toMap(l -> l.split(" -> ")[0], l -> l.split(" -> ")[1]));

            int steps = 10;
            for (int step = 0; step < steps; step++) {
                StringBuilder newTemplate = new StringBuilder();
                for (int i = 0; i < template.length() - 1; i++) {
                    String substr = template.substring(i, i + 2);
                    if (rules.containsKey(substr)) {
                        newTemplate.append(substr.charAt(0)).append(rules.get(substr));
                    } else {
                        newTemplate.append(substr);
                    }
                }
                template = newTemplate.append(template.charAt(template.length() - 1)).toString();
            }

            Map<Character, Integer> charCounts = new HashMap<>();
            for (char c : template.toCharArray()) {
                charCounts.merge(c, 0, (a, b) -> a + 1);
            }

            int max = charCounts.values().stream().max(Integer::compareTo).get();
            int min = charCounts.values().stream().min(Integer::compareTo).get();

            System.out.println(max - min);
            System.out.println((max - min) == Integer.parseInt(expected1));

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }
}
