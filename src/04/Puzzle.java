import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Puzzle {

    final static boolean IS_TEST = false;
    static Path input = Path.of("./src/04/" + (IS_TEST ? "example.txt" : "input.txt"));

    public static void main(String... args) {
        try {
            List<String> lines = Files.readAllLines(input);
            List<Integer> callNumbers = Stream.of(lines.get(0).split(","))
                    .map(Integer::parseInt)
                    .toList();
            String expected1 = lines.stream().filter(l -> l.startsWith("answer1:")).map(l -> l.replace("answer1:", ""))
                    .findFirst().get();
            String expected2 = lines.stream().filter(l -> l.startsWith("answer2:")).map(l -> l.replace("answer2:", ""))
                    .findFirst().get();

            System.out.println(callNumbers);

            List<int[][]> boards = getBoards(lines);
            // printBoards(boards);

            int[][] bingoBoard = null;
            int calledNumberCount = 5;
            for (calledNumberCount = 5; calledNumberCount < callNumbers.size(); calledNumberCount++) {
                bingoBoard = gotABingo(boards, callNumbers.subList(0, calledNumberCount));
                if (bingoBoard != null) {
                    break;
                }
            }

            // WHY????? can't i do Arrays.asList(bingoBoard) Java????
            List<int[][]> bingoBoards = new ArrayList<>();
            bingoBoards.add(bingoBoard);

            System.out.println(callNumbers.subList(0, calledNumberCount));
            printBoards(bingoBoards);

            int sum = 0;
            for (int i = 0; i < bingoBoard.length; i++) {
                for (int j = 0; j < bingoBoard[i].length; j++) {
                    int curr = bingoBoard[i][j];
                    if (!callNumbers.subList(0, calledNumberCount).contains(curr)) {
                        sum += curr;
                    }
                }
            }

            int product = sum * callNumbers.get(calledNumberCount - 1);
            System.out.println(product);
            System.out.println(product == Integer.parseInt(expected1));

        } catch (Exception e) {
            System.out.println("Oh shit! " + e);
        }
    }

    private static int[][] gotABingo(List<int[][]> boards, List<Integer> calledNumbers) {
        for (int[][] board : boards) {
            for (int i = 0; i < board.length; i++) {
                if (calledNumbers
                        .containsAll(Arrays.asList(board[i][0], board[i][1], board[i][2], board[i][3], board[i][4]))) {
                    return board;
                }
                if (calledNumbers
                        .containsAll(Arrays.asList(board[0][i], board[1][i], board[2][i], board[3][i], board[4][i]))) {
                    return board;
                }
            }
        }
        return null;
    }

    private static void printBoards(List<int[][]> boards) {
        for (int[][] board : boards) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    System.out.print(board[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    private static List<int[][]> getBoards(List<String> lines) {
        List<int[][]> boards = new ArrayList<>();
        List<String> boardInter = lines.stream().skip(2).takeWhile(s -> !s.startsWith("answer")).toList();
        for (int i = 0; i < boardInter.size(); i++) {
            int[][] board = new int[5][5];
            int j = 0;
            int k = 0;
            while (!boardInter.get(i).isBlank()) {
                for (String s : boardInter.get(i).split(" ")) {
                    if (!s.isBlank()) {
                        board[j][k] = Integer.parseInt(s);
                        k++;
                    }
                }
                k = 0;
                j++;
                i++;
            }
            boards.add(board);
        }
        return boards;
    }
}