import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Puzzle20 {
    final static boolean IS_TEST = true;
    static Path input = Path.of("./src/20/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();
        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }
}
