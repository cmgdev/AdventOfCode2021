import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Puzzle16 {
    final static boolean IS_TEST = true;
    static Path input = Path.of("./src/16/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            String hex = lines.get(0);
            StringBuilder sb = new StringBuilder();
            for (char h : hex.toCharArray()) {
                sb.append(getBin(h));
            }

            Packet p = new Packet(sb.toString());
            System.out.println(p);
            System.out.println(p.getSumOfVersions());
            System.out.println(p.getSumOfVersions() == Integer.parseInt(expected1));

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    private static String getBin(char hex) {
        return switch (hex) {
            case '0' -> "0000";
            case '1' -> "0001";
            case '2' -> "0010";
            case '3' -> "0011";
            case '4' -> "0100";
            case '5' -> "0101";
            case '6' -> "0110";
            case '7' -> "0111";
            case '8' -> "1000";
            case '9' -> "1001";
            case 'A' -> "1010";
            case 'B' -> "1011";
            case 'C' -> "1100";
            case 'D' -> "1101";
            case 'E' -> "1110";
            case 'F' -> "1111";
            default -> "xxxx";
        };
    }

    static class Packet {
        int version;
        int type;
        String literalValue = new String();
        int remainingBits;
        List<Packet> subPackets = new ArrayList<>();
        int lengthTypeId;

        Packet(String bin) {
            int current = 0;

            int version = Integer.parseInt(bin.substring(current, current + 3), 2);
            this.setVersion(version);
            current += 3;

            int type = Integer.parseInt(bin.substring(current, current + 3), 2);
            this.setType(type);
            current += 3;

            if (this.isLiteral()) {
                boolean cont = bin.substring(current, current + 1).equals("1");
                current++;
                while (cont) {
                    this.addToLiteralValue(bin.substring(current, current + 4));
                    current += 4;
                    cont = bin.substring(current, current + 1).equals("1");
                    current++;
                }
                this.addToLiteralValue(bin.substring(current, current + 4));
                current += 4;
            } else {
                int lengthTypeId = Integer.parseInt(bin.substring(current, current + 1));
                this.lengthTypeId = lengthTypeId;
                current++;
                if (lengthTypeId == 0) {
                    int subPacketBits = Integer.parseInt(bin.substring(current, current + 15), 2);
                    current += 15;
                    int subPacketUsed = 0;
                    int subCurrent = current;

                    while (subPacketUsed < subPacketBits) {
                        int endIndex = subCurrent + subPacketBits - subPacketUsed;
                        if(endIndex > bin.length()){
                            System.err.println("here's the problem");
                        }
                        String subpacketString = bin.substring(subCurrent, endIndex);
                        Packet subPacket = new Packet(subpacketString);
                        this.subPackets.add(subPacket);
                        subPacketUsed = subPacketBits - subPacket.remainingBits;
                        subCurrent += subPacketUsed;
                    }
                    current += subPacketUsed;
                } else if (lengthTypeId == 1) {
                    if( current + 11 > bin.length()){
                        System.err.println("here's the problem");
                        
                    }
                    int subPacketCount = Integer.parseInt(bin.substring(current, current + 11), 2);
                    current += 11;
                    int remainingBits = bin.substring(current).length();

                    for (int i = 0; i < subPacketCount; i++) {
                        String subpacketString = bin.substring(current);
                        Packet subPacket = new Packet(subpacketString);
                        this.subPackets.add(subPacket);
                        int subPacketBits = remainingBits - subPacket.remainingBits;
                        remainingBits -= subPacketBits;
                        current += subPacketBits;
                    }
                }
            }

            this.remainingBits = bin.length() - current;
            System.out.println(this);
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public void setType(int type) {
            this.type = type;
        }

        boolean isLiteral() {
            return type == 4;
        }

        void addToLiteralValue(String literal) {
            this.literalValue += literal;
        }

        Long getLiteral() {
            if (!literalValue.isBlank()) {
                return Long.parseLong(literalValue, 2);
            }
            return 0l;
        }

        public int getRemainingBits() {
            return remainingBits;
        }

        public int getSumOfVersions() {
            int sum = version;
            for (Packet p : subPackets) {
                sum += p.getSumOfVersions();
            }
            return sum;
        }

        @Override
        public String toString() {
            return "version: " + version + ", type: " + type + ", literalValue: " + literalValue + ", literal: "
                    + getLiteral() + ", lengthTypeId: " + lengthTypeId + ", subPackets:\n\t " + subPackets;
        }
    }
}
