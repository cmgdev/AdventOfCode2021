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
                    .filter(v -> v.a.x == v.b.x || v.a.y == v.b.y)
                    .toList();
            // System.out.println(vents);
            
            Map<Point, Integer> pointCounts = new HashMap<>();
            for( Vent vent : vents){
                if( vent.a.x != vent.b.x){
                    int start = Math.min(vent.a.x, vent.b.x);
                    int end = Math.max(vent.a.x, vent.b.x);
                    for(int i = start; i <= end; i++){
                        int y = vent.a.y;
                        Point p = new Point(i, y);
                        int count = pointCounts.getOrDefault(p, 0) + 1;
                        pointCounts.put(p, count);
                    }
                }
                else if( vent.a.y != vent.b.y){
                    int start = Math.min(vent.a.y, vent.b.y);
                    int end = Math.max(vent.a.y, vent.b.y);
                    for(int i = start; i <= end; i++){
                        int x = vent.a.x;
                        Point p = new Point(x, i);
                        int count = pointCounts.getOrDefault(p, 0) + 1;
                        pointCounts.put(p, count);
                    }
                }
            }
            // System.out.println(pointCounts);
            long multi = pointCounts.values().stream().filter(c -> c > 1).count();
            System.out.println(multi);
            System.out.println(multi == Long.parseLong(expected1));

        } catch (Exception e) {
            System.out.println("Oh shit! " + e);
        }
    }

    record Point(int x, int y) {
    }

    record Vent(Point a, Point b) {
    }

}