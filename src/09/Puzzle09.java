import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Puzzle09 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/09/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            lines.removeIf(l -> l.startsWith("answer"));
            Map<Point, Integer> map = new HashMap<>();
            int x = 0;
            for (String line : lines) {

                int y = 0;
                for (char c : line.toCharArray()) {
                    map.put(new Point(x, y), Integer.parseInt(String.valueOf(c)));
                    y++;
                }
                x++;
            }

            int maxX = map.keySet().stream().mapToInt(p -> p.x).max().getAsInt();
            int maxY = map.keySet().stream().mapToInt(p -> p.y).max().getAsInt();
            Map<Point, Integer> lowPoints = new HashMap<>();
            for (x = 0; x <= maxX; x++) {
                for (int y = 0; y <= maxY; y++) {
                    Point p = new Point(x, y);
                    int self = map.get(p);
                    boolean lowest = true;
                    for (Point nextPoint : getNeighboringPoints(p)) {
                        if (map.containsKey(nextPoint) && map.get(nextPoint) <= self) {
                            lowest = false;
                            break;
                        }
                    }
                    if (lowest) {
                        lowPoints.put(new Point(x, y), map.get(new Point(x, y)));
                    }
                }
            }
            int sum = lowPoints.values().stream().mapToInt(v -> v + 1).sum();
            System.out.println(sum);
            System.out.println(sum == Integer.parseInt(expected1));

            System.out.println("\npart 2");
            List<Map<Point, Integer>> basins = new ArrayList<>();

            for (Point lowPoint : lowPoints.keySet()) {
                Map<Point, Integer> basin = new HashMap<>();

                List<Point> visitedPoints = new ArrayList<>();
                Deque<Point> queue = new LinkedList<>();
                queue.add(lowPoint);

                while (!queue.isEmpty()) {
                    Point p = queue.pop();
                    int height = map.get(p);
                    if (height < 9) {
                        basin.put(p, height);

                        for (Point n : getNeighboringPoints(p)) {
                            if (map.containsKey(n) && !visitedPoints.contains(n)) {
                                queue.push(n);
                            }
                        }
                    }
                    visitedPoints.add(p);
                }
                // System.out.println(basin);
                // System.out.println("size: " + basin.size());
                basins.add(basin);
            }
            // System.out.println(basins);
            long product = basins.stream()
                    .sorted(Comparator.comparingInt(l -> l.size() * -1)) // reverse sort
                    .limit(3) // top 3
                    .mapToLong(l -> l.size())
                    .reduce((a, b) -> a * b).getAsLong();
            System.out.println(product);
            System.out.println(product == Long.parseLong(expected2));

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    private static List<Point> getNeighboringPoints(Point p) {
        int[][] dims = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        List<Point> neighbors = new ArrayList<>();
        for (int[] dim : dims) {
            neighbors.add(new Point(p.x + dim[0], p.y + dim[1]));
        }
        return neighbors;
    }

    public record Point(int x, int y) {
    }
}
