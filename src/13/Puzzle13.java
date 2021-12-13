import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Puzzle13 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/13/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            lines.removeIf(l -> l.startsWith("answer"));

            Set<Point> points = lines.stream()
                    .takeWhile(l -> !l.isBlank() && !l.startsWith("fold"))
                    .map(l -> l.split(","))
                    .map(xy -> new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])))
                    .collect(Collectors.toSet());

            List<String> instructions = lines.stream()
                    .filter(l -> l.startsWith("fold"))
                    .map(l -> l.replace("fold along ", ""))
                    .toList();

            var count = 0;
            for (var instruction : instructions) {
                points = doFold(points, instruction);
                if (count == 0) {
                    System.out.println(points.size());
                    System.out.println(points.size() == Integer.parseInt(expected1));
                    count++;
                } else {
                    points = doFold(points, instruction);
                }
            }

            System.out.println(points);
            printPoints(points);

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    static void printPoints(Set<Point> points) {
        int minX = 0;
        int minY = 0;
        int maxX = points.stream().mapToInt(Point::x).max().getAsInt();
        int maxY = points.stream().mapToInt(Point::y).max().getAsInt();

        Map<Point, Character> pointMap = points.stream()
                .map(Function.identity())
                .collect(Collectors.toMap(Function.identity(), v -> '#'));

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                System.out.print(pointMap.getOrDefault(new Point(x, y), '.'));
            }
            System.out.println();
        }
    }

    static Set<Point> doFold(Set<Point> points, String instruction) {
        Set<Point> folded = new HashSet<>();
        if (instruction.startsWith("y")) {
            int axis = Integer.parseInt(instruction.replace("y=", ""));
            for (var point : points) {
                if (point.y > axis) {
                    int newY = point.y - ((point.y - axis) * 2);
                    folded.add(new Point(point.x, newY));
                } else {
                    folded.add(point);
                }
            }
        } else {
            int axis = Integer.parseInt(instruction.replace("x=", ""));
            for (var point : points) {
                if (point.x > axis) {
                    int newX = point.x - ((point.x - axis) * 2);
                    folded.add(new Point(newX, point.y));
                } else {
                    folded.add(point);
                }
            }
        }
        return folded;
    }

    record Point(int x, int y) {
    }
}
