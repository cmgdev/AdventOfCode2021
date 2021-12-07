import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Puzzle07 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/07/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            List<Integer> positions = Stream.of(lines.get(0).split(","))
                    .map(Integer::parseInt).toList();

            Long lowestCost = getLowestCost(positions, false);
            System.out.println(lowestCost == Long.parseLong(expected1));

            lowestCost = getLowestCost(positions, true);
            System.out.println(lowestCost == Long.parseLong(expected2));

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    private static Long getLowestCost(List<Integer> positions, boolean fuelPenalty) {
        Long lowestCost = Long.MAX_VALUE;
        int maxPosition = positions.stream().max(Integer::compareTo).get();
        Map<Integer, Long> cache = new HashMap<>();

        for (int currentPosition = 0; currentPosition <= maxPosition; currentPosition++) {
            Long currentCost = 0l;
            for (Integer position : positions) {
                int distance = Math.abs(position - currentPosition);
                if (fuelPenalty) {
                    currentCost += cache.compute(distance, (d,v) -> LongStream.rangeClosed(1, d).sum());

                } else {
                    currentCost += distance;
                }
                if (currentCost > lowestCost) {
                    break;
                }
            }
            if( currentCost < lowestCost){
                lowestCost = Math.min(lowestCost, currentCost);
                System.out.println("After position " + currentPosition + " lowest cost is " + lowestCost);
            }
        }
        return lowestCost;
    }
}
