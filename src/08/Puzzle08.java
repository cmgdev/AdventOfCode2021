import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Puzzle08 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/08/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            long count = lines.stream().takeWhile(l -> !l.startsWith("answer"))
                    .map(s -> s.split(" \\| ")[1])
                    .map(s -> Arrays.asList(s.split(" ")))
                    .mapToLong(l -> l.stream()
                            .filter(s -> s.length() == 2 || s.length() == 3 || s.length() == 4 || s.length() == 7)
                            // .peek(s -> System.out.println(s))
                            .count())
                    .sum();
            System.out.println(count);
            System.out.println(count == Long.parseLong(expected1));
        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }
}
