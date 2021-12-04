import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
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

            part1(callNumbers, expected1, boards);

            part2(callNumbers, expected2, boards);

        } catch (Exception e) {
            System.out.println("Oh shit! " + e);
        }
    }

    private static void part2(List<Integer> callNumbers, String expected2, List<int[][]> boards) {
        System.out.println("\nSolving part2");
        Map<int[][], Boolean> cardsWithBingo = boards.stream()
                .collect(Collectors.toMap(Function.identity(), b -> false));

        int calledNumberCount = 5;
        for (calledNumberCount = 5; calledNumberCount < callNumbers.size(); calledNumberCount++) {
            for (Entry<int[][], Boolean> cardEntry : cardsWithBingo.entrySet()) {
                if (!cardEntry.getValue()) {
                    cardsWithBingo.put(cardEntry.getKey(),
                            gotABingo(cardEntry.getKey(), callNumbers.subList(0, calledNumberCount)));
                }
            }
            long countOfWinningCards = cardsWithBingo.values().stream().filter(b -> b == true).count();
            // System.out.println(countOfWinningCards + " out of " + cardsWithBingo.size());
            if (countOfWinningCards == cardsWithBingo.size() - 1) {
                break;
            }
        }

        int[][] lastBingo = cardsWithBingo.entrySet().stream().filter(e -> !e.getValue()).map(e -> e.getKey())
                .findFirst().get();
        printBoard(lastBingo);
        int score = getBoardScore(callNumbers, calledNumberCount + 1, lastBingo);
        System.out.println(score);
        System.out.println(score == Integer.parseInt(expected2));
    }

    private static void part1(List<Integer> callNumbers, String expected1, List<int[][]> boards) {
        System.out.println("Solving part1");
        int bingoBoardNum = -1;
        int calledNumberCount = 5;
        for (calledNumberCount = 5; calledNumberCount < callNumbers.size(); calledNumberCount++) {
            bingoBoardNum = gotABingo(boards, callNumbers.subList(0, calledNumberCount));
            if (bingoBoardNum >= 0) {
                break;
            }
        }

        int[][] bingoBoard = boards.get(bingoBoardNum);
        // WHY????? can't i do Arrays.asList(bingoBoard) Java????
        List<int[][]> bingoBoards = new ArrayList<>();
        bingoBoards.add(bingoBoard);

        System.out.println(callNumbers.subList(0, calledNumberCount));
        printBoards(bingoBoards);

        int score = getBoardScore(callNumbers, calledNumberCount, bingoBoard);
        System.out.println(score == Integer.parseInt(expected1));
    }

    private static int getBoardScore(List<Integer> callNumbers, int calledNumberCount,
            int[][] bingoBoard) {
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
        System.out.println(sum + " * " + callNumbers.get(calledNumberCount - 1) + " = " + product);
        // System.out.println(product);
        return product;
    }

    private static int gotABingo(List<int[][]> boards, List<Integer> calledNumbers) {
        int cardNum = 0;
        for (int[][] board : boards) {
            if (gotABingo(board, calledNumbers)) {
                return cardNum;
            }
            cardNum++;
        }
        return -1;
    }

    private static boolean gotABingo(int[][] board, List<Integer> calledNumbers) {
        for (int i = 0; i < board.length; i++) {
            if (calledNumbers
                    .containsAll(Arrays.asList(board[i][0], board[i][1], board[i][2], board[i][3], board[i][4]))) {
                return true;
            }
            if (calledNumbers
                    .containsAll(Arrays.asList(board[0][i], board[1][i], board[2][i], board[3][i], board[4][i]))) {
                return true;
            }
        }
        return false;
    }

    private static void printBoards(List<int[][]> boards) {
        for (int[][] board : boards) {
            printBoard(board);
        }
    }

    private static void printBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
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