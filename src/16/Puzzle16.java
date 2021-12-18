import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Puzzle16 {
    final static boolean IS_TEST = false;
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
            System.out.println(p.getSumOfVersions());
            System.out.println(p.getSumOfVersions() == Integer.parseInt(expected1));

            System.out.println(p.getValue());
            System.out.println(p.getValue() == Long.parseLong(expected2));

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
        int bitLength;
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
                        String subpacketString = bin.substring(subCurrent, endIndex);
                        Packet subPacket = new Packet(subpacketString);
                        this.subPackets.add(subPacket);
                        subCurrent += subPacket.bitLength;
                        subPacketUsed += subPacket.bitLength;
                    }
                    current += subPacketBits;
                } else if (lengthTypeId == 1) {
                    int subPacketCount = Integer.parseInt(bin.substring(current, current + 11), 2);
                    current += 11;

                    for (int i = 0; i < subPacketCount; i++) {
                        String subpacketString = bin.substring(current);
                        Packet subPacket = new Packet(subpacketString);
                        this.subPackets.add(subPacket);
                        current += subPacket.bitLength;
                    }
                }
            }

            this.bitLength = current;
        }

        long getValue() {
            long value = 0;

            if (this.type == 0) {
                // sum
                for (Packet subPacket : this.subPackets) {
                    value += subPacket.getValue();
                }
            } else if (this.type == 1) {
                // product
                value = 1;
                for (Packet subPacket : this.subPackets) {
                    value *= subPacket.getValue();
                }
            } else if (this.type == 2) {
                // minimum
                value = Long.MAX_VALUE;
                for (Packet subPacket : this.subPackets) {
                    value = Math.min(value, subPacket.getValue());
                }
            } else if (this.type == 3) {
                // maximum
                value = Long.MIN_VALUE;
                for (Packet subPacket : this.subPackets) {
                    value = Math.max(value, subPacket.getValue());
                }
            } else if (this.type == 4) {
                return this.getLiteral();
            } else if (this.type == 5) {
                // greater than
                value = subPackets.get(0).getValue() > subPackets.get(1).getValue() ? 1 : 0;
            } else if (this.type == 6) {
                // less than
                value = subPackets.get(0).getValue() < subPackets.get(1).getValue() ? 1 : 0;
            } else if (this.type == 7) {
                // equal to
                value = subPackets.get(0).getValue() == subPackets.get(1).getValue() ? 1 : 0;
            }

            return value;
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

        public int getSumOfVersions() {
            int sum = version;
            for (Packet p : subPackets) {
                sum += p.getSumOfVersions();
            }
            return sum;
        }

        @Override
        public String toString() {
            return "version: " + version + ", lengthTypeId: " + lengthTypeId + ", subPackets:\n\t " + subPackets;
        }
    }
}
