import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
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

            Integer lowestCost = Integer.MAX_VALUE;
            int maxPosition = positions.stream().max(Integer::compareTo).get();
            
            for (int currentPosition = 0; currentPosition <= maxPosition; currentPosition++) {
                int currentCost = 0;
                for (Integer position : positions) {
                    currentCost += Math.abs(position - currentPosition);
                    if(currentCost > lowestCost){
                        break;
                    }
                }
                lowestCost = Math.min(lowestCost, currentCost);
                System.out.println( "After position " + currentPosition + " lowest cost is " + lowestCost );
            }

            System.out.println(lowestCost == Integer.parseInt(expected1));
        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }
}
