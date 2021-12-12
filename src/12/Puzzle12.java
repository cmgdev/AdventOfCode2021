import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Puzzle12 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/12/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            lines.removeIf(l -> l.startsWith("answer"));

            Map<String, List<String>> caves = new HashMap<>();
            for (var line : lines) {
                String[] split = line.split("-");
                var caveA = split[0];
                var caveB = split[1];

                List<String> caveAConnected = caves.getOrDefault(caveA, new ArrayList<>());
                caveAConnected.add(caveB);
                caves.put(caveA, caveAConnected);

                List<String> caveBConnected = caves.getOrDefault(caveB, new ArrayList<>());
                caveBConnected.add(caveA);
                caves.put(caveB, caveBConnected);
            }

            long count = countPaths(caves, new LinkedList<>(), "start", false);
            System.out.println(count);
            System.out.println(count == Long.parseLong(expected1));

            count = countPaths(caves, new LinkedList<>(), "start", true);
            System.out.println(count);
            System.out.println(count == Long.parseLong(expected2));

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    public static long countPaths(Map<String, List<String>> caves, LinkedList<String> path, String nextCave,
            boolean isPart2) {
        if (nextCave.equals("end")) {
            // path.add(nextCave);
            // System.out.println(path);
            return 1l;
        }

        long count = 0l;
        path.addLast(nextCave);
        List<String> connected = caves.get(nextCave);

        Map<String, List<String>> smallCaves = path.stream().filter(c -> c.equals(c.toLowerCase()))
                .collect(Collectors.groupingBy(Function.identity()));
        int maxSmallCaves = smallCaves.values().stream().mapToInt(List::size).max().getAsInt();
        for (var cave : connected) {
            boolean isCaveAccessible = cave.equals(cave.toUpperCase());
            if (isPart2) {
                if (maxSmallCaves == 2) {
                    isCaveAccessible |= !path.contains(cave);
                } else {
                    isCaveAccessible |= !cave.equals("start");
                }
            } else {
                isCaveAccessible |= !path.contains(cave);
            }

            if (isCaveAccessible) {
                count += countPaths(caves, new LinkedList<>(path), cave, isPart2);
            }
        }
        return count;
    }

}
