import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Puzzle11 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/11/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            lines.removeIf(l -> l.startsWith("answer"));

            Map<Point, Integer> octopi = new HashMap<>();
            int maxY = lines.get(0).length();
            int x = 0;
            for (String line : lines) {
                for (int y = 0; y < maxY; y++) {
                    octopi.put(new Point(x, y), Integer.parseInt(line.substring(y, y + 1)));
                }
                x++;
            }

            int maxSteps = 100;
            int flashes = 0;
            for (int step = 0; step < maxSteps; step++) {
                flashes += runStep(octopi);
            }
            System.out.println(flashes);
            System.out.println(flashes == Integer.parseInt(expected1));

            System.out.println("\npart 2");
            flashes = 0;
            int step = 100;
            while( flashes < 100){
                flashes = runStep(octopi);
                step++;
            }

            System.out.println(step);
            System.out.println(step == Integer.parseInt(expected2));

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    private static int runStep(Map<Point, Integer> octopi ){
        Deque<Point> queue = new LinkedList<>();
        queue.addAll(octopi.keySet());
        Set<Point> flashedThisStep = new HashSet<>();
        int flashes = 0;

        while(!queue.isEmpty()){
            Point next = queue.removeFirst();
            if( flashedThisStep.contains(next)){
                continue;
            }
            int energy = octopi.get(next) + 1;
            if( energy > 9){
                flashes++;
                energy = 0;
                octopi.put(next, energy);
                flashedThisStep.add(next);

                for( Point p : next.getPossibleNeighbors()){
                    if(octopi.containsKey(p) && !flashedThisStep.contains(p)){
                        queue.addFirst(p);
                    }
                }
            }
            else{
                octopi.put(next, energy);
            }
        }
        return flashes;
    }

    private static void printOctopi(Map<Point, Integer> octopi) {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                System.out.print(octopi.get(new Point(x, y)));
            }
            System.out.println();
        }
    }

    record Point(int x, int y) {
        List<Point> getPossibleNeighbors(){
            return Arrays.asList(
                new Point(x - 1, y - 1), new Point(x - 1, y), new Point(x - 1, y + 1),
                new Point(x, y - 1), new Point(x, y + 1),
                new Point(x + 1, y - 1), new Point(x + 1, y), new Point(x + 1, y + 1));
        }
    }
}
