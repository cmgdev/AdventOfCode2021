import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Puzzle05 {

    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/05/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            List<Vent> vents = getVents(lines);
            Map<Point, Integer> pointCounts = getPointCounts(vents, false);
            // System.out.println(pointCounts);
            long multi = pointCounts.values().stream().filter(c -> c > 1).count();
            System.out.println(multi);
            System.out.println(multi == Long.parseLong(expected1));

            pointCounts = getPointCounts(vents, true);
            // System.out.println(pointCounts);
            multi = pointCounts.values().stream().filter(c -> c > 1).count();
            System.out.println(multi);
            System.out.println(multi == Long.parseLong(expected2));

        } catch (Exception e) {
            System.out.println("Oh shit! " + e);
        }
    }

    private static Map<Point, Integer> getPointCounts(List<Vent> vents, boolean includeDiagonals) {
        Map<Point, Integer> pointCounts = new HashMap<>();
        for (Vent vent : vents) {
            if( !includeDiagonals && !(vent.a.x == vent.b.x || vent.a.y == vent.b.y)){
                continue;
            }

            if (vent.a.x != vent.b.x && vent.a.y == vent.b.y) {
                int start = Math.min(vent.a.x, vent.b.x);
                int end = Math.max(vent.a.x, vent.b.x);
                for (int i = start; i <= end; i++) {
                    int y = vent.a.y;
                    Point p = new Point(i, y);
                    int count = pointCounts.getOrDefault(p, 0) + 1;
                    pointCounts.put(p, count);
                }
            } else if (vent.a.y != vent.b.y && vent.a.x == vent.b.x) {
                int start = Math.min(vent.a.y, vent.b.y);
                int end = Math.max(vent.a.y, vent.b.y);
                for (int i = start; i <= end; i++) {
                    int x = vent.a.x;
                    Point p = new Point(x, i);
                    int count = pointCounts.getOrDefault(p, 0) + 1;
                    pointCounts.put(p, count);
                }
            }
            else if(includeDiagonals){
                int deltaX = (vent.b.x - vent.a.x) > 0 ? 1 : -1;
                int deltaY = (vent.b.y - vent.a.y) > 0 ? 1 : -1;
                int currX = vent.a.x;
                int currY = vent.a.y;

                while(currX != vent.b.x + deltaX && currY != vent.b.y + deltaY){
                    Point p = new Point(currX, currY);
                    int count = pointCounts.getOrDefault(p, 0) + 1;
                    pointCounts.put(p, count);
                    currX += deltaX;
                    currY += deltaY;
                }
            }
        }
        return pointCounts;
    }

    private static List<Vent> getVents(List<String> lines) {
        List<Vent> vents = lines.stream().filter(l -> !l.startsWith("answer"))
                .map(l -> {
                    String start = l.split(" -> ")[0];
                    String end = l.split(" -> ")[1];
                    String[] startPoint = start.split(",");
                    String[] endPoint = end.split(",");
                    return new Vent(
                            new Point(Integer.parseInt(startPoint[0]), Integer.parseInt(startPoint[1])),
                            new Point(Integer.parseInt(endPoint[0]), Integer.parseInt(endPoint[1])));
                })
                .toList();
        return vents;
    }

    record Point(int x, int y) {
    }

    record Vent(Point a, Point b) {
    }

}