import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Puzzle24 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/24/" + (IS_TEST ? "example.txt" : "input.txt"));

    static final String INPUT = "inp";
    static final String ADD = "add";
    static final String MULTIPLY = "mul";
    static final String DIVIDE = "div";
    static final String MOD = "mod";
    static final String EQUAL = "eql";

    /*
     * inp a - Read an input value and write it to variable a.
     * add a b - Add the value of a to the value of b, then store the result in
     * variable a.
     * mul a b - Multiply the value of a by the value of b, then store the result in
     * variable a.
     * div a b - Divide the value of a by the value of b, truncate the result to an
     * integer, then store the result in variable a. (Here, "truncate" means to
     * round the value toward zero.)
     * mod a b - Divide the value of a by the value of b, then store the remainder
     * in variable a. (This is also called the modulo operation.)
     * eql a b - If the value of a and b are equal, then store the value 1 in
     * variable a. Otherwise, store the value 0 in variable a.
     */

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            lines.removeIf(l -> l.startsWith("answer"));

            ModelNumGenerator gen = new ModelNumGenerator("99911993949685");
            BigInteger modelNum = gen.getNext();
            while (!checkModelNum(lines, modelNum)) {
                modelNum = gen.getNext();
            }

            System.out.println("Highest working model num is " + modelNum);
            System.out.println(modelNum.equals(new BigInteger(expected1)));

            //62911941716111
            gen = new ModelNumGenerator("62911941716110");
            modelNum = gen.getPrevious();
            while (!checkModelNum(lines, modelNum)) {
                modelNum = gen.getNext();
            }

            System.out.println("Lowest working model num is " + modelNum);
            System.out.println(modelNum.equals(new BigInteger(expected2)));

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    static class ModelNumGenerator {
        BigInteger modelNum;

        ModelNumGenerator(String start) {
            this.modelNum = new BigInteger(start);
        }

        BigInteger getNext() {
            modelNum = modelNum.subtract(BigInteger.ONE);
            while (modelNum.toString().contains("0")) {
                String mnStr = modelNum.toString();
                int i = mnStr.indexOf("0");
                String next = Integer.toString(Integer.parseInt(mnStr.substring(i - 1, i + 1)) - 1);
                if (next.length() == 1) {
                    next = "0" + next;
                }
                modelNum = BigInteger.valueOf(Long.parseLong(mnStr.replace(mnStr.substring(i - 1, i + 1), next)));
            }
            return modelNum;
        }

        BigInteger getPrevious() {
            modelNum = modelNum.add(BigInteger.ONE);
            return modelNum;
        }
    }

    private static boolean checkModelNum(List<String> lines, BigInteger modelNum) {
        Map<String, Integer> vars = new HashMap<>();
        vars.put("w", 0);
        vars.put("x", 0);
        vars.put("y", 0);
        vars.put("z", 0);
        int current = 0;

        for (var line : lines) {
            var tokens = line.split(" ");
            String op = tokens[0];
            String varName = tokens[1];

            String token2 = "";
            if (tokens.length == 3) {
                token2 = tokens[2];
                if (token2.matches("\\D")) {
                    token2 = Integer.toString(vars.get(token2));
                }
            } else {
                token2 = String.valueOf(modelNum.toString().charAt(current));
            }
            int val = Integer.parseInt(token2);
            int priorVal = vars.get(varName);

            if (op.equals(INPUT)) {
                vars.put(varName, val);
                current++;
            } else if (op.equals(MULTIPLY)) {
                vars.put(varName, priorVal * val);
            } else if (op.equals(ADD)) {
                vars.put(varName, priorVal + val);
            } else if (op.equals(DIVIDE)) {
                vars.put(varName, priorVal / val);
            } else if (op.equals(MOD)) {
                vars.put(varName, priorVal % val);
            } else if (op.equals(EQUAL)) {
                vars.put(varName, priorVal == val ? 1 : 0);
            }

        }
        int z = vars.get("z");
        return z == 0;
    }

}
