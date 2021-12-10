import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Puzzle10 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/10/" + (IS_TEST ? "example.txt" : "input.txt"));

    /*
     * syntax error points
     * ): 3 points.
     * ]: 57 points.
     * }: 1197 points.
     * >: 25137 points.
     * 
     * auto-complete points
     * ): 1 point.
     * ]: 2 points.
     * }: 3 points.
     * >: 4 points.
     * 
     */
    public static void main(String... args) {
        List<Bracket> brackets = Arrays.asList(
                new Bracket('(', ')', 3, 1),
                new Bracket('[', ']', 57, 2),
                new Bracket('{', '}', 1197, 3),
                new Bracket('<', '>', 25137, 4));
        List<Character> openingChars = brackets.stream().map(Bracket::openingChar).toList();

        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            lines.removeIf(l -> l.startsWith("answer"));

            List<Long> incompleteScores = new ArrayList<>();

            int score = 0;
            for (String line : lines) {
                Deque<Character> stack = new LinkedList<>();
                boolean corrupt = false;
                for (char c : line.toCharArray()) {
                    if (openingChars.contains(c)) {
                        stack.push(c);
                    } else {
                        Bracket bracket = brackets.stream().filter(b -> b.closingChar == c).findFirst().get();
                        if (bracket.openingChar != stack.pop()) {
                            score += bracket.syntaxErrorPoints;
                            corrupt = true;
                            break;
                        }
                    }
                }
                if (!corrupt) {
                    long incompleteScore = 0l;
                    while (!stack.isEmpty()) {
                        char c = stack.pop();
                        Bracket bracket = brackets.stream().filter(b -> b.openingChar == c).findFirst().get();
                        incompleteScore = (incompleteScore * 5) + bracket.autoCompletePoints;
                    }
                    incompleteScores.add(incompleteScore);
                }
            }
            System.out.println(score);
            System.out.println(score == Integer.parseInt(expected1));

            incompleteScores.sort(Comparator.comparing(Function.identity()));
            long midScore = incompleteScores.get((incompleteScores.size() / 2));
            System.out.println(midScore);
            System.out.println(midScore == Long.parseLong(expected2));
        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    record Bracket(char openingChar, char closingChar, int syntaxErrorPoints, int autoCompletePoints) {
    }

}
