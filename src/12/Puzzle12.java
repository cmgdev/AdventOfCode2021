import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
            int count = countPaths(caves, new LinkedList<>(), "start");
            System.out.println(count);
            System.out.println(count == Integer.parseInt(expected1));

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    public static int countPaths(Map<String, List<String>> caves, LinkedList<String> path, String nextCave) {
        if (nextCave.equals("end")) {
            // path.add(nextCave);
            // System.out.println(path);
            return 1;
        }

        int count = 0;
        path.addLast(nextCave);
        List<String> connected = caves.get(nextCave);
        for (var cave : connected) {
            if (cave.equals(cave.toUpperCase()) || !path.contains(cave)) {
                count += countPaths(caves, new LinkedList<>(path), cave);
            }
        }
        return count;
    }

}
