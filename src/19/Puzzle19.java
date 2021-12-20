import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Puzzle19 {
    final static boolean IS_TEST = true;
    static Path input = Path.of("./src/19/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            lines.removeIf(l -> l.startsWith("answer"));
            List<Scanner> scanners = new ArrayList<>();

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith("---")) {
                    i++;
                    List<Beacon> beacons = new ArrayList<>();
                    String line = lines.get(i);
                    while (!line.isBlank()) {
                        String[] xyz = line.split(",");
                        beacons.add(new Beacon(i, Integer.parseInt(xyz[0]), Integer.parseInt(xyz[1]),
                                Integer.parseInt(xyz[2])));
                        i++;
                        if (i >= lines.size()) {
                            break;
                        }
                        line = lines.get(i);
                    }
                    scanners.add(new Scanner(beacons));
                }
            }

            System.out.println(scanners);
            List<List<BeaconPair>> allBeaconPairs = new ArrayList<>();
            // Set<String> distances = new HashSet<>();
            for (Scanner scanner : scanners) {
                List<BeaconPair> beaconPairs = new ArrayList<>();
                for (int i = 0; i < scanner.beacons.size() - 1; i++) {
                    for (int j = i + 1; j < scanner.beacons.size(); j++) {
                        if (i != j) {
                            beaconPairs.add(new BeaconPair(scanner.beacons.get(i), scanner.beacons.get(j)));
                            // distances.add(scanner.beacons.get(i).getDistance(scanner.beacons.get(j)));
                        }
                    }
                }
                allBeaconPairs.add(beaconPairs);
            }

            List<BeaconPair> a = allBeaconPairs.get(0);
            List<BeaconPair> b = allBeaconPairs.get(1);

            

            // for (int i = 0; i < allBeaconPairs.size() - 1; i++) {
            //     for (int j = i + 1; j < allBeaconPairs.size(); j++) {

            //     }
            // }

            // System.out.println(allBeaconPairs);
            // Map<String, List<BeaconPair>> matches = allBeaconPairs.stream().flatMap(l ->
            // l.stream()).collect(Collectors.groupingBy(BeaconPair::getDistance));
            // for( var e : matches.entrySet()){
            // System.out.println(e.getKey() + " " + e.getValue());
            // }
            // Set<List<Integer>> matched = new HashSet<>();
            // for( List<BeaconPair> beaconPairs : matches.values()){
            // if(beaconPairs.size() > 1){
            // List<Integer> match1 = new ArrayList<>();
            // match1.add(beaconPairs.get(0).a.id);
            // match1.add(beaconPairs.get(1).a.id);
            // List<Integer> match2 = new ArrayList<>();
            // match2.add(beaconPairs.get(0).b.id);
            // match2.add(beaconPairs.get(1).b.id);
            // matched.add(match1);
            // matched.add(match2);
            // }
            // }
            // System.out.println(matched);
            // System.out.println(matched.size());

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    record BeaconPair(Beacon a, Beacon b) {
        String getDistance() {
            return Math.abs(a.x - b.x) + "," + Math.abs(a.y - b.y) + "," + Math.abs(a.z - b.z);
        }

        @Override
        public String toString() {
            return a + " -> " + b + " = " + getDistance();
        }
    }

    record Beacon(int id, int x, int y, int z) {
        String getDistance(Beacon b) {
            return Math.abs(x - b.x) + "," + Math.abs(y - b.y) + "," + Math.abs(z - b.z);
        }
    }

    record Scanner(List<Beacon> beacons) {
    }
}
