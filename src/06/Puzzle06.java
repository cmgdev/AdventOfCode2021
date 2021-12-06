import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Puzzle06 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/06/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            int currentDay = 0;
            int maxDays = 80;
            List<LanternFish> lanternFish = new ArrayList<>();
            for (String f : lines.get(0).split(",")) {
                lanternFish.add(new LanternFish(Integer.parseInt(f)));
            }

            while (currentDay < maxDays) {
                System.out.println("Day " + currentDay + ": " + lanternFish.size());
                List<LanternFish> newFish = new ArrayList<>();

                for (LanternFish fish : lanternFish) {
                    if (fish.age == 0) {
                        newFish.add(new LanternFish(6)); // reset fish
                        newFish.add(new LanternFish(8)); // new fish
                    } else {
                        newFish.add(new LanternFish(fish.age - 1) );
                    }
                }

                lanternFish = newFish;
                currentDay++;
            }
            System.out.println(lanternFish.size());
            System.out.println(lanternFish.size() == Integer.parseInt(expected1));

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    /**
     * LanternFish
     */
    public record LanternFish(int age) {
    }
}
