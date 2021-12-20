import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Puzzle20 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/20/" + (IS_TEST ? "example.txt" : "input.txt"));
    final static int MAX_X = 0;
    final static int MIN_X = 1;
    final static int MAX_Y = 2;
    final static int MIN_Y = 3;

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            String enhancementAlgorithm = lines.get(0);
            List<String> input = lines.stream().skip(2).takeWhile(l -> !l.startsWith("answer")).toList();

            Map<Pixel, String> image = new HashMap<>();
            for (int x = 0; x < input.size(); x++) {
                String line = input.get(x);
                for (int y = 0; y < line.length(); y++) {
                    image.put(new Pixel(x, y), line.substring(y, y + 1));
                }
            }

            image = enhance(enhancementAlgorithm, image, 2);
            long lightPixels = image.values().stream().filter(v -> v.equals("#")).count();
            System.out.println(lightPixels);
            System.out.println(lightPixels == Long.parseLong(expected1));

            image = enhance(enhancementAlgorithm, image, 48);
            lightPixels = image.values().stream().filter(v -> v.equals("#")).count();
            System.out.println(lightPixels);
            System.out.println(lightPixels == Long.parseLong(expected2));
        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    private static Map<Pixel, String> enhance(String enhancementAlgorithm, Map<Pixel, String> image, int rounds) {
        image = expand(image, rounds);
        int[] minMax = getMinMax(image);
        for (int r = 0; r < rounds; r++) {
            Map<Pixel, String> newImage = new HashMap<>();
            int margin = rounds - r;
            for (int x = minMax[MIN_X] - margin; x <= minMax[MAX_X] + margin; x++) {
                for (int y = minMax[MIN_Y] - margin; y <= minMax[MAX_Y] + margin; y++) {
                    Pixel p = new Pixel(x, y);
                    int idx = p.getBinFromNeighbors(image);
                    String next = enhancementAlgorithm.substring(idx, idx + 1);
                    newImage.put(p, next);
                }
            }
            image = newImage;
            System.out.println("Round " + r);
            // printImage(image);
        }
        return image;
    }

    static Map<Pixel, String> expand(Map<Pixel, String> image, int amt) {
        int[] minMax = getMinMax(image);

        for (int x = minMax[MIN_X] - amt; x <= minMax[MAX_X] + amt; x++) {
            for (int y = minMax[MIN_Y] - amt; y <= minMax[MAX_Y] + amt; y++) {
                image.putIfAbsent(new Pixel(x, y), ".");
            }
        }
        return image;
    }

    static int[] getMinMax(Map<Pixel, String> image) {
        int[] minMax = new int[4];
        minMax[MAX_X] = Integer.MIN_VALUE;
        minMax[MIN_X] = Integer.MAX_VALUE;
        minMax[MAX_Y] = Integer.MIN_VALUE;
        minMax[MIN_Y] = Integer.MAX_VALUE;
        for (Pixel p : image.keySet()) {
            minMax[MAX_X] = Math.max(minMax[MAX_X], p.x);
            minMax[MIN_X] = Math.min(minMax[MIN_X], p.x);
            minMax[MAX_Y] = Math.max(minMax[MAX_Y], p.y);
            minMax[MIN_Y] = Math.min(minMax[MIN_Y], p.y);
        }
        return minMax;
    }

    static void printImage(Map<Pixel, String> image) {
        int[] minMax = getMinMax(image);
        for (int x = minMax[MIN_X]; x <= minMax[MAX_X]; x++) {
            for (int y = minMax[MIN_Y]; y <= minMax[MAX_Y]; y++) {
                System.out.print(image.getOrDefault(new Pixel(x, y), "."));
            }
            System.out.println();
        }
    }

    record Pixel(int x, int y) {
        List<Pixel> getNeighbors() {
            return Arrays.asList(
                    new Pixel(x - 1, y - 1), new Pixel(x - 1, y), new Pixel(x - 1, y + 1),
                    new Pixel(x, y - 1), this, new Pixel(x, y + 1),
                    new Pixel(x + 1, y - 1), new Pixel(x + 1, y), new Pixel(x + 1, y + 1));
        }

        int getBinFromNeighbors(Map<Pixel, String> image) {
            List<Pixel> neighbors = getNeighbors();
            // if (neighbors.stream().noneMatch(p -> image.containsKey(p))) {
            // return -1;
            // }
            String bin = neighbors.stream()
                    .map(p -> image.getOrDefault(p, ".").equals(".") ? "0" : "1")
                    .collect(Collectors.joining());
            return Integer.parseInt(bin, 2);
        }
    }
}
