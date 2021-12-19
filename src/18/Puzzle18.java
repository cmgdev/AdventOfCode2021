import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class Puzzle18 {
    final static boolean IS_TEST = true;
    static Path input = Path.of("./src/18/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            lines.removeIf(l -> l.startsWith("answer"));

            String sfNum = lines.get(0);
            StringBuilder sb = new StringBuilder();
            int nested = 0;

            LinkedList<Character> queue = new LinkedList<>();
            for (char c : sfNum.toCharArray()) {
                if (c != ',') {
                    queue.addLast(c);
                }
            }
            LinkedList<Character> visited = new LinkedList<>();

            while (!queue.isEmpty()) {
                char c = queue.pop();
                visited.push(c);
                if (c == '[') {
                    nested++;
                }
                if (c == ']') {
                    nested--;
                }

                if (nested >= 4) {
                    char left = queue.peek();
                    if (Character.isDigit(left)) {
                        int leftDigit = Character.digit(queue.pop(), 10);
                        System.out.println(leftDigit + " is gonna explode");
                        int newLeft = 0;
                        for (int i = 0; i < visited.size(); i++) {
                            char inspect = visited.get(i);
                            if (Character.isDigit(inspect)) {
                                newLeft = leftDigit + Character.digit(inspect, 10);
                                break;
                            }
                        }
                        System.out.println("new left is " + newLeft);
                        if (newLeft == 0) {
                            visited.pop();
                        }
                        visited.push(Character.forDigit(newLeft, 10));

                        int newRight = 0;
                        for(int i = 0; i < queue.size(); i++){
                            char inspect = queue.get(i);
                            if( Character.isDigit(inspect)){
                                
                            }
                        }
                        // char right = queue.
                    } else {
                        visited.push(left);
                    }
                }
            }
            // for (int i = 0; i < sfNum.length(); i++) {
            // char c = sfNum.charAt(i);
            // if (nested >= 4) {
            // char left = sfNum.charAt(i + 1);
            // if (Character.isDigit(left)) {
            // int leftDigit = Character.digit(left, 10);
            // System.out.println(left + " is gonna explode");
            // int newLeft = 0;

            // for (int j = i; j >= 0; j--) {
            // char nextLeft = sfNum.charAt(j);
            // if (Character.isDigit(nextLeft)) {
            // newLeft = leftDigit + Character.digit(nextLeft, 10);
            // break;
            // }
            // }

            // System.out.println(newLeft);

            // }
            // }
            // }

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }
}
