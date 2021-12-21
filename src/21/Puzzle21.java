import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Puzzle21 {
    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/21/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);

            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            Player p1 = new Player(1, Integer.parseInt(lines.get(0).substring(28)));
            Player p2 = new Player(2, Integer.parseInt(lines.get(1).substring(28)));

            DetDie die = new DetDie();

            System.out.println(p1);
            System.out.println(p2);

            while (true) {
                p1.advance(die.next());
                if (p1.score >= 1000) {
                    break;
                }
                p2.advance(die.next());
                if (p2.score >= 1000) {
                    break;
                }
                // System.out.println(p1);
                // System.out.println(p2);
            }

            // System.out.println(die.rolls);
            int minScore = Math.min(p1.score, p2.score);
            int result = minScore * die.rolls;
            System.out.println(result);
            System.out.println(result == Integer.parseInt(expected1));

        } catch (Exception e) {
            System.out.println("Shit! " + e);
        }
    }

    static class Player {
        int id;
        int current;
        int score;

        Player(int id, int current) {
            this.id = id;
            this.current = current;
        }

        void advance(int amount) {
            this.current = this.current + amount;
            while (this.current > 10) {
                this.current -= 10;
            }
            this.score += this.current;
        }

        @Override
        public String toString() {
            return "Player[id=" + this.id + ", current=" + this.current + ", score=" + this.score + "]";
        }
    }

    static class DetDie {
        int current = 1;
        int rolls = 0;

        int next() {
            int next = 0;
            // if (current <= 98) {
            // next = (3 * current) + 3;
            // this.current += 3;
            // } else {
            int first = current;
            if (first > 100) {
                first -= 100;
            }
            int second = first + 1;
            if (second > 100) {
                second -= 100;
            }
            int third = second + 1;
            if (third > 100) {
                third -= 100;
            }
            next = first + second + third;
            this.current = third + 1;
            // }
            this.rolls += 3;
            return next;
        }
    }
}
