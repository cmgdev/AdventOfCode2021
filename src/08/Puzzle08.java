import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Puzzle08 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/08/" + (IS_TEST ? "example.txt" : "input.txt"));

    static final String ZERO = "abcefg";
    static final String ONE = "cf";
    static final String TWO = "acdeg";
    static final String THREE = "acdfg";
    static final String FOUR = "bcdf";
    static final String FIVE = "abdfg";
    static final String SIX = "abdefg";
    static final String SEVEN = "acf";
    static final String EIGHT = "abcdefg";
    static final String NINE = "abcdfg";

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            long count = lines.stream().takeWhile(l -> !l.startsWith("answer"))
                    .map(s -> s.split(" \\| ")[1])
                    .map(s -> Arrays.asList(s.split(" ")))
                    .mapToLong(l -> l.stream()
                            .filter(s -> s.length() == 2 || s.length() == 3 || s.length() == 4 || s.length() == 7)
                            // .peek(s -> System.out.println(s))
                            .count())
                    .sum();
            System.out.println(count);
            System.out.println(count == Long.parseLong(expected1));

            System.out.println("\nPart 2");
            long sum = 0;
            for (String line : lines) {
                if (line.startsWith("answer")) {
                    break;
                }
                List<String> allSegments = Arrays.asList(line.replace(" | ", " ").split(" "));
                Set<Character> oneSegments = allSegments.stream().filter(s -> s.length() == 2)
                        .map(s -> charsToSet(s))
                        .findFirst().orElse(null);
                Set<Character> fourSegments = allSegments.stream().filter(s -> s.length() == 4).map(s -> charsToSet(s))
                        .findFirst().orElse(null);
                Set<Character> sevenSegments = allSegments.stream().filter(s -> s.length() == 3).map(s -> charsToSet(s))
                        .findFirst().orElse(null);
                Set<Character> eightSegments = allSegments.stream().filter(s -> s.length() == 7).map(s -> charsToSet(s))
                        .findFirst().orElse(null);
                List<Set<Character>> zeroSixNineSegments = allSegments.stream().filter(s -> s.length() == 6)
                        .map(s -> charsToSet(s))
                        .distinct().toList();
                List<Set<Character>> twoThreeFiveSegments = allSegments.stream().filter(s -> s.length() == 5)
                        .map(s -> charsToSet(s))
                        .distinct().toList();
                // System.out
                //         .println(oneSegments + " " + fourSegments + " " + sevenSegments + " " + eightSegments + " "
                //                 + zeroSixNineSegments + " " + twoThreeFiveSegments);

                Character segmentA = sevenSegments.stream().filter(c -> !oneSegments.contains(c)).findFirst()
                        .orElse(null);
                Set<Character> threeSegments = twoThreeFiveSegments.stream().filter(s -> s.containsAll(oneSegments))
                        .findFirst().orElse(null);
                Character segmentD = fourSegments.stream().filter(
                        c -> !oneSegments.contains(c) && twoThreeFiveSegments.stream().allMatch(s -> s.contains(c)))
                        .findFirst().orElse(null);
                Character segmentB = fourSegments.stream().filter(c -> !oneSegments.contains(c) && !segmentD.equals(c))
                        .findFirst().orElse(null);
                Character segmentG = threeSegments.stream()
                        .filter(c -> !oneSegments.contains(c) && !segmentD.equals(c) && !segmentA.equals(c)).findFirst()
                        .orElse(null);
                Set<Character> zeroSegments = zeroSixNineSegments.stream().filter(s -> !s.contains(segmentD))
                        .findFirst().orElse(null);
                Set<Character> nineSegments = zeroSixNineSegments.stream()
                        .filter(s -> s.containsAll(
                                Set.of(segmentA, segmentB, segmentD, segmentG)) && s.containsAll(oneSegments))
                        .findFirst().orElse(null);
                Set<Character> sixSegments = zeroSixNineSegments.stream()
                        .filter(s -> !s.containsAll(zeroSegments) && !s.containsAll(nineSegments)).findFirst()
                        .orElse(null);
                Set<Character> cSegments = new HashSet<>(nineSegments);
                cSegments.retainAll(zeroSegments);
                cSegments.removeAll(sixSegments);
                Character segmentC = new ArrayList<>(cSegments).get(0);
                Character segmentF = oneSegments.stream().filter(c -> !c.equals(segmentC)).findFirst().orElse(null);
                Set<Character> eSegments = new HashSet<>(sixSegments);
                eSegments.removeAll(Arrays.asList(segmentA, segmentB, segmentD, segmentF, segmentG));
                Character segmentE = new ArrayList<>(eSegments).get(0);
                Set<Character> fiveSegments = Set.of(segmentA, segmentB, segmentD, segmentF, segmentG);
                Set<Character> twoSegments = Set.of(segmentA, segmentC, segmentD, segmentE, segmentG);

                // System.out.println("A: " + segmentA);
                // System.out.println("B: " + segmentB);
                // System.out.println("C: " + segmentC);
                // System.out.println("D: " + segmentD);
                // System.out.println("E: " + segmentE);
                // System.out.println("F: " + segmentF);
                // System.out.println("G: " + segmentG);

                // System.out.println("0: " + zeroSegments);
                // System.out.println("1: " + oneSegments);
                // System.out.println("2: " + twoSegments);
                // System.out.println("3: " + threeSegments);
                // System.out.println("4: " + fourSegments);
                // System.out.println("5: " + fiveSegments);
                // System.out.println("6: " + sixSegments);
                // System.out.println("7: " + sevenSegments);
                // System.out.println("8: " + eightSegments);
                // System.out.println("9: " + nineSegments);

                String[] outputs = line.split(" \\| ")[1].split(" ");
                String output = "";
                for( String o : outputs){
                    char[] c = o.toCharArray();
                    List<Character> cList = new ArrayList<>();
                    for ( char b : c){
                        cList.add(b);
                    }
                    if( cList.size() == zeroSegments.size() && cList.containsAll(zeroSegments)){
                        output += "0";
                    }
                    if( cList.size() == oneSegments.size() && cList.containsAll(oneSegments)){
                        output += "1";
                    }
                    if( cList.size() == twoSegments.size() && cList.containsAll(twoSegments)){
                        output += "2";
                    }
                    if( cList.size() == threeSegments.size() && cList.containsAll(threeSegments)){
                        output += "3";
                    }
                    if( cList.size() == fourSegments.size() && cList.containsAll(fourSegments)){
                        output += "4";
                    }
                    if( cList.size() == fiveSegments.size() && cList.containsAll(fiveSegments)){
                        output += "5";
                    }
                    if( cList.size() == sixSegments.size() && cList.containsAll(sixSegments)){
                        output += "6";
                    }
                    if( cList.size() == sevenSegments.size() && cList.containsAll(sevenSegments)){
                        output += "7";
                    }
                    if( cList.size() == eightSegments.size() && cList.containsAll(eightSegments)){
                        output += "8";
                    }
                    if( cList.size() == nineSegments.size() && cList.containsAll(nineSegments)){
                        output += "9";
                    }
                }
                sum += Long.parseLong(output);
            }
            System.out.println(sum);
            System.out.println(sum == Long.parseLong(expected2));
        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    private static Set<Character> charsToSet(String s) {
        char[] chars = s.toCharArray();
        Set<Character> cSet = new HashSet<>();
        for (char c : chars) {
            cSet.add(c);
        }
        return cSet;
    }

    private static String sortCharsInString(String s) {
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}
