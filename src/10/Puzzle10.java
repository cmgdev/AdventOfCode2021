import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Puzzle10 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/10/" + (IS_TEST ? "example.txt" : "input.txt"));

    /*
     * 
     * ): 3 points.
     * ]: 57 points.
     * }: 1197 points.
     * >: 25137 points.
     */
    public static void main(String... args) {
        List<Bracket> brackets = Arrays.asList(
                new Bracket('(', ')', 3),
                new Bracket('[', ']', 57),
                new Bracket('{', '}', 1197),
                new Bracket('<', '>', 25137));
        List<Character> openingChars = brackets.stream().map(Bracket::openingChar).toList();
        List<Character> closingChars = brackets.stream().map(Bracket::closingChar).toList();
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            lines.removeIf(l -> l.startsWith("answer"));

            int score = 0;
            Deque<Character> stack = new LinkedList<>();
            for (String line : lines) {
                for (char c : line.toCharArray()) {
                    if (openingChars.contains(c)) {
                        stack.push(c);
                    } else {
                        Bracket bracket = brackets.stream().filter(b -> b.closingChar == c).findFirst().get();
                        if (bracket.openingChar != stack.pop()) {
                            System.out.println(line + " is corrupted. Was " + c);
                            score += bracket.points;
                            break;
                        }
                    }
                }
            }
            System.out.println(score);
            System.out.println(score == Integer.parseInt(expected1));
        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    record Bracket(char openingChar, char closingChar, int points) {
    }

}
