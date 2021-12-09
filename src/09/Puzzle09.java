import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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
            int[][] map = new int[lines.size()][lines.get(0).length()];
            int x = 0;
            for (String line : lines) {

                int y = 0;
                for (char c : line.toCharArray()) {
                    map[x][y] = Integer.parseInt(String.valueOf(c));
                    y++;
                }
                x++;
            }

            // printMap(map);
            int sum = 0;
            int[][] dims = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
            int maxI = map.length;
            for (int i = 0; i < maxI; i++) {
                int maxJ = map[i].length;
                for (int j = 0; j < maxJ; j++) {
                    int self = map[i][j];
                    boolean lowest = true;
                    for (int[] dim : dims) {
                        int nextI = i + dim[0];
                        int nextJ = j + dim[1];
                        if (nextI < maxI && nextI >= 0 && nextJ < maxJ && nextJ >= 0) {
                            // System.out.println("is " + map[nextI][nextJ] + " less than " + self + "?");
                            if (map[nextI][nextJ] <= self) {
                                lowest = false;
                                // System.out.println("true");
                                break;
                            }
                        }
                    }
                    if (lowest) {
                        sum += self + 1;
                    }
                }
            }
            System.out.println(sum);
            System.out.println(sum == Integer.parseInt(expected1));

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    private static void printMap(int[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }
}
