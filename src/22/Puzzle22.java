import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Puzzle22 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/22/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            int minX = -50;
            int maxX = 50;
            int minY = -50;
            int maxY = 50;
            int minZ = -50;
            int maxZ = 50;

            lines.removeIf(l -> l.startsWith("answer") || l.startsWith("#"));
            List<Instruction> instructions = new ArrayList<>();
            for (var line : lines) {
                String onOff = line.split(" ")[0];
                String[] coords = line.split(" ")[1].split(",");
                String[] xs = coords[0].split("=")[1].split("\\.\\.");
                String[] ys = coords[1].split("=")[1].split("\\.\\.");
                String[] zs = coords[2].split("=")[1].split("\\.\\.");
                // System.out.println(xs + " " + ys + " " + zs);
                instructions.add(new Instruction(onOff,
                        Integer.parseInt(xs[0]), Integer.parseInt(xs[1]),
                        Integer.parseInt(ys[0]), Integer.parseInt(ys[1]),
                        Integer.parseInt(zs[0]), Integer.parseInt(zs[1])));
            }

            Map<Point, Boolean> cube = new HashMap<>();
            for (var instruction : instructions) {
                for (int x = Math.max(instruction.minX, minX); x <= Math.min(instruction.maxX, maxX); x++) {
                    for (int y = Math.max(instruction.minY, minY); y <= Math.min(instruction.maxY, maxY); y++) {
                        for (int z = Math.max(instruction.minZ, minZ); z <= Math.min(instruction.maxZ, maxZ); z++) {
                                cube.put(new Point(x, y, z), instruction.isOn());
                        }
                    }
                }
            }

            long on = cube.values().stream().filter(b -> b == true).count();
            System.out.println(on);
            System.out.println(on == Long.parseLong(expected1));

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    record Point(int x, int y, int z) {
    }

    record Instruction(String onOff, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        boolean isOn() {
            return onOff.equals("on");
        }
    }
}
