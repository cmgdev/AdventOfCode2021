import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

            int maxDays = 80;
            Map<Integer, Long> lanternFishByAge = new HashMap<>();
            for (String f : lines.get(0).split(",")) {
                lanternFishByAge.merge(Integer.parseInt(f), 1l, (a, b) -> a + 1);
            }

            lanternFishByAge = reproduceForXDays(maxDays, lanternFishByAge);
            long numFish = lanternFishByAge.values().stream().mapToLong(v -> v).sum();
            System.out.println(numFish);
            System.out.println(numFish == Long.parseLong(expected1));

            // part 2
            maxDays = 256;
            lanternFishByAge = new HashMap<>();
            for (String f : lines.get(0).split(",")) {
                lanternFishByAge.merge(Integer.parseInt(f), 1l, (a, b) -> a + 1);
            }

            lanternFishByAge = reproduceForXDays(maxDays, lanternFishByAge);
            numFish = lanternFishByAge.values().stream().mapToLong(v -> v).sum();
            System.out.println(numFish);
            System.out.println(numFish == Long.parseLong(expected2));

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    private static Map<Integer, Long> reproduceForXDays(int maxDays, Map<Integer, Long> lanternFishByAge) {
        int currentDay = 0;
        while (currentDay < maxDays) {
            System.out.println("Day " + currentDay + ": " + lanternFishByAge);

            Map<Integer, Long> newMap = new HashMap<>();
            for (Entry<Integer, Long> ageGroup : lanternFishByAge.entrySet()) {
                if (ageGroup.getKey() == 0) {
                    newMap.put(6, ageGroup.getValue());
                    newMap.put(8, ageGroup.getValue());
                } else {
                    newMap.merge(ageGroup.getKey() - 1, ageGroup.getValue(), (a, b) -> a + b);
                }
            }
            lanternFishByAge = newMap;
            currentDay++;
        }
        return lanternFishByAge;
    }

    /**
     * LanternFish
     */
    public record LanternFish(int age) {
    }
}
