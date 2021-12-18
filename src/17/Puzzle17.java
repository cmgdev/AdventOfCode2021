import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Puzzle17 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/17/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            String[] tokens = lines.get(0).split(",");
            String xs = tokens[0].replace("x=", "");
            int minX = Integer.parseInt(xs.split("\\.\\.")[0]);
            int maxX = Integer.parseInt(xs.split("\\.\\.")[1]);

            String ys = tokens[1].replace("y=", "");
            int minY = Integer.parseInt(ys.split("\\.\\.")[0]);
            int maxY = Integer.parseInt(ys.split("\\.\\.")[1]);

            TargetArea t = new TargetArea(minX, maxX, minY, maxY);

            int highestY = 0;
            int count = 0;
            for (int initXv = -1000; initXv < 1000; initXv++) {
                for (int initYv = -1000; initYv < 1000; initYv++) {
                    int probeX = 0;
                    int probeY = 0;
                    int xv = initXv;
                    int yv = initYv;
                    int thisHighestY = probeY;

                    while (!(t.isInTargetArea(probeX, probeY) || t.isPastTargetArea(probeX, probeY))) {
                        probeX += xv;
                        probeY += yv;
                        thisHighestY = Math.max(thisHighestY, probeY);
                        if (xv > 0) {
                            xv--;
                        } else if (xv < 0) {
                            xv++;
                        }
                        yv--;
                    }
                    if (t.isInTargetArea(probeX, probeY)) {
                        count++;
                        if (thisHighestY > highestY) {
                            highestY = thisHighestY;
                            System.out.println("highestY is now " + highestY);
                            System.out.println("init xv,yv is " + initXv + "," + initYv);
                        }
                    }
                }
            }

            System.out.println("highestY is " + highestY);
            System.out.println(highestY == Integer.parseInt(expected1));
            System.out.println(count);
            System.out.println(count == Integer.parseInt(expected2));

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    record TargetArea(int minX, int maxX, int minY, int maxY) {
        boolean isInTargetArea(int x, int y) {
            return x >= minX && x <= maxX && y >= minY && y <= maxY;
        }

        boolean isPastTargetArea(int x, int y) {
            return x > maxX || y < minY;
        }
    }
}
