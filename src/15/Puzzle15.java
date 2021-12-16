import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Puzzle15 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/15/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            lines.removeIf(l -> l.startsWith("answer"));

            Map<Point, Integer> map = buildMap(lines);

            int maxX = map.keySet().stream().mapToInt(Point::x).max().getAsInt();
            int maxY = map.keySet().stream().mapToInt(Point::y).max().getAsInt();

            int shortest = findShortestDistanceByThePowerOfDijkstra(new Point(maxX, maxY), map);
            System.out.println(shortest);
            System.out.println(shortest == Integer.parseInt(expected1));

            map = expandMap(map);
            maxX = map.keySet().stream().mapToInt(Point::x).max().getAsInt();
            maxY = map.keySet().stream().mapToInt(Point::y).max().getAsInt();
            shortest = findShortestDistanceByThePowerOfDijkstra(new Point(maxX, maxY), map);
            System.out.println(shortest);
            System.out.println(shortest == Integer.parseInt(expected2));

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    private static Map<Point, Integer> buildMap(List<String> lines) {
        Map<Point, Integer> map = new HashMap<>();
        int maxX = lines.size();
        int maxY = lines.get(0).length();
        for (int x = 0; x < maxX; x++) {
            String line = lines.get(x);
            for (int y = 0; y < maxY; y++) {
                map.put(new Point(x, y), Integer.parseInt(line.substring(y, y + 1)));
            }
        }
        return map;
    }

    private static Map<Point, Integer> expandMap(Map<Point, Integer> map) {
        Map<Point, Integer> expandedMap = new HashMap<>();

        int maxX = map.keySet().stream().mapToInt(Point::x).max().getAsInt() + 1;
        int maxY = map.keySet().stream().mapToInt(Point::y).max().getAsInt() + 1;

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                for (Entry<Point, Integer> e : map.entrySet()) {
                    Point p = e.getKey();
                    Point newPoint = new Point(p.x + (maxX * x), p.y + (maxY * y));
                    int newValue = e.getValue() + x + y;
                    if (newValue > 9) {
                        newValue -= 9;
                    }
                    expandedMap.put(newPoint, newValue);
                }
            }
        }
        // printMap(expandedMap);

        return expandedMap;
    }

    private static void printMap(Map<Point, Integer> map) {
        int maxX = map.keySet().stream().mapToInt(Point::x).max().getAsInt();
        int maxY = map.keySet().stream().mapToInt(Point::y).max().getAsInt();

        System.out.println();
        for (int x = 0; x <= maxX; x++) {
            for (int y = 0; y <= maxY; y++) {
                System.out.print(map.get(new Point(x, y)));
            }
            System.out.println();
        }
    }

    private static Integer findShortestDistanceByThePowerOfDijkstra(Point end, Map<Point, Integer> map) {
        Map<Point, Integer> mapWithWeightedDistances = map.keySet().stream()
                .collect(Collectors.toMap(k -> k, v -> Integer.MAX_VALUE));
        mapWithWeightedDistances.put(new Point(0, 0), 0);

        PriorityQueue<Point> queue = new PriorityQueue<>(Comparator.comparingInt(p -> mapWithWeightedDistances.get(p)));
        queue.add(new Point(0, 0));

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            List<Point> visitableNeighbors = current.getNeighbors()
                    .stream().filter(p -> map.containsKey(p)).toList();

            for (Point neighbor : visitableNeighbors) {
                int currentDistanceToNeighbor = mapWithWeightedDistances.get(current) + map.get(neighbor);

                if (mapWithWeightedDistances.get(neighbor) > currentDistanceToNeighbor) {
                    if (mapWithWeightedDistances.get(neighbor) != Integer.MAX_VALUE) {
                        queue.remove(neighbor);
                    }

                    mapWithWeightedDistances.put(neighbor, currentDistanceToNeighbor);
                    queue.add(neighbor);
                }
            }
        }

        return mapWithWeightedDistances.get(end);
    }

    record Point(int x, int y) {
        List<Point> getNeighbors() {
            return Arrays.asList(new Point(x + 1, y), new Point(x - 1, y), new Point(x, y + 1), new Point(x, y - 1));
        }
    }
}
