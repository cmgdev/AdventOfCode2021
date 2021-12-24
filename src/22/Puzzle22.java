import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Puzzle22 {
    final static boolean IS_TEST = true;
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
            int id = 1;
            for (var line : lines) {
                String onOff = line.split(" ")[0];
                String[] coords = line.split(" ")[1].split(",");
                String[] xs = coords[0].split("=")[1].split("\\.\\.");
                String[] ys = coords[1].split("=")[1].split("\\.\\.");
                String[] zs = coords[2].split("=")[1].split("\\.\\.");
                instructions.add(new Instruction(onOff, false,
                        Integer.parseInt(xs[0]), Integer.parseInt(xs[1]),
                        Integer.parseInt(ys[0]), Integer.parseInt(ys[1]),
                        Integer.parseInt(zs[0]), Integer.parseInt(zs[1]),
                        Integer.toString(id++)));
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

            LinkedList<Instruction> instrQueue = new LinkedList<>(instructions);
            Map<Boolean, Set<Instruction>> allInstructions = new HashMap<>();
            allInstructions.put(true, new HashSet<>());
            allInstructions.put(false, new HashSet<>());

            while (!instrQueue.isEmpty()) {
                Instruction a = instrQueue.pop();
                allInstructions.get(a.isOn()).add(a);
                for (int j = 0; j < instructions.size(); j++) {
                    Instruction b = instructions.get(j);
                    Instruction intersect = a.getIntersection(b);
                    if (intersect != null && !instrQueue.contains(intersect)
                            && !allInstructions.get(intersect.isOn()).contains(intersect)) {
                        instrQueue.addLast(intersect);
                    }
                }
            }

            for (var instructionSet : allInstructions.values()) {
                for (var instr : instructionSet) {
                    System.out.println(instr);
                }
            }

            // long sumOnNoIntersect = allInstructions.get(true).stream().filter(i ->
            // !i.isIntersection()).mapToLong(i -> i.getVolume()).sum();
            // long sumOffNoIntersect = allInstructions.get(false).stream().filter(i ->
            // !i.isIntersection()).mapToLong(i -> i.getVolume()).sum();
            // long sumOnIntersect = allInstructions.get(true).stream().filter(i ->
            // i.isIntersection()).mapToLong(i -> i.getVolume()).sum();
            // long sumOffIntersect = allInstructions.get(false).stream().filter(i ->
            // i.isIntersection()).mapToLong(i -> i.getVolume()).sum();

            // System.out.println(sumOnNoIntersect);
            // System.out.println(sumOffNoIntersect);
            // System.out.println(sumOnIntersect);
            // System.out.println(sumOffIntersect);

            // long numOn = 0;
            // for(var instruction : allInstructions){
            // if(instruction.isOn()){
            // numOn += instruction.getVolume();
            // }
            // }

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    record Point(int x, int y, int z) {
    }

    record Instruction(String onOff, boolean isIntersection, int minX, int maxX, int minY, int maxY, int minZ,
            int maxZ, String id) {
        boolean isOn() {
            return onOff.equals("on");
        }

        long getVolume() {
            long x = Math.abs(Integer.valueOf(maxX).longValue() - Integer.valueOf(minX).longValue());
            long y = Math.abs(Integer.valueOf(maxY).longValue() - Integer.valueOf(minY).longValue());
            long z = Math.abs(Integer.valueOf(maxZ).longValue() - Integer.valueOf(minZ).longValue());
            // System.out.println(x + " * " + y + " * " + z);
            return x * y * z;
        }

        long getSizeOfIntersection(Instruction other) {
            long x = 0;
            long y = 0;
            long z = 0;

            if (this.maxX() >= other.minX() || other.maxX() >= this.minX()) {
                x = (long) Math.min(this.maxX(), other.maxX())
                        - (long) Math.max(this.minX(), other.minX()) + 1;
            }
            if (this.maxY() >= other.minY() || other.maxY() >= this.minY()) {
                y = (long) Math.min(this.maxY(), other.maxY())
                        - (long) Math.max(this.minY(), other.minY()) + 1;
            }
            if (this.maxZ() >= other.minZ() || other.maxZ() >= this.minZ()) {
                z = (long) Math.min(this.maxZ(), other.maxZ())
                        - (long) Math.max(this.minZ(), other.minZ()) + 1;
            }

            return Math.abs(x * y * z);
        }

        boolean instersects(Instruction other) {
            return (isBetween(this.maxX(), other.minX(), other.maxX())
                    || (isBetween(this.minX(), other.minX(), other.maxX()))
                    || (isBetween(other.maxX(), this.minX(), this.maxX()))
                    || (isBetween(other.minX(), this.minX(), this.maxX())))
                    && (isBetween(this.maxY(), other.minY(), other.maxY())
                            || isBetween(this.minY(), other.minY(), other.maxY())
                            || (isBetween(other.maxY(), this.minY(), this.maxY()))
                            || (isBetween(other.minY(), this.minY(), this.maxY())))
                    && (isBetween(this.maxZ(), other.minZ(), other.maxZ())
                            || isBetween(this.minZ(), other.minZ(), other.maxZ())
                            || (isBetween(other.maxZ(), this.minZ(), this.maxZ()))
                            || (isBetween(other.minZ(), this.minZ(), this.maxZ())));
        }

        Instruction getIntersection(Instruction other) {
            if (this.instersects(other)) {
                int minMaxX = Math.min(this.maxX(), other.maxX());
                int maxMinX = Math.max(this.minX(), other.minX());
                int minMaxY = Math.min(this.maxY(), other.maxY());
                int maxMinY = Math.max(this.minY(), other.minY());
                int minMaxZ = Math.min(this.maxZ(), other.maxZ());
                int maxMinZ = Math.max(this.minZ(), other.minZ());
                String onOff = this.isOn() && other.isOn() ? "on" : "off";
                return new Instruction(onOff, true, maxMinX, minMaxX, maxMinY, minMaxY, maxMinZ, minMaxZ,
                        this.id + "." + other.id);
            }
            return null;
        }

        @Override
        public boolean equals(Object obj) {
            Instruction other = (Instruction) obj;
            return this.isOn() == other.isOn()
                    && this.maxX == other.maxX
                    && this.minX == other.minX
                    && this.maxY == other.maxY
                    && this.minY == other.minY
                    && this.maxZ == other.maxZ
                    && this.minZ == other.minZ;
        }

        // List<Instruction> getIntersections(List<Instruction> others) {
        // List<Instruction> intersections = new ArrayList<>();
        // if (this.instersects(other)) {
        // int minMaxX = Math.min(this.maxX(), other.maxX());
        // int maxMinX = Math.max(this.minX(), other.minX());
        // int minMaxY = Math.min(this.maxY(), other.maxY());
        // int maxMinY = Math.max(this.minY(), other.minY());
        // int minMaxZ = Math.min(this.maxZ(), other.maxZ());
        // int maxMinZ = Math.max(this.minZ(), other.minZ());
        // String onOff = this.isOn() && other.isOn() ? "on" : "off";
        // intersections.add(new Instruction(onOff, maxMinX, minMaxX, maxMinY, minMaxY,
        // maxMinZ, minMaxZ);
        // }
        // return null;
        // }
    }

    static boolean isBetween(int a, int min, int max) {
        return a >= min && a <= max;
    }

}
