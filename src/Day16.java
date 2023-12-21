import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Day16 {
    private static final int RIGHT = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int UP = 3;

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/16.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        ArrayList<String> input = new ArrayList<>();
        while (scanner.hasNext()) {
            input.add(scanner.nextLine());
        }
        scanner.close();

        System.out.printf("Part 1: %d\n", solveForStart(input, 0, 0, RIGHT));
        System.out.printf("Part 2: %d\n", partTwo(input));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static int solveForStart(ArrayList<String> input, int x, int y, int direction) {
        Set<String> memo = new HashSet<>();
        boolean[][] energized = new boolean[input.size()][input.get(0).length()];
        solveEnergized(input, x, y, direction, memo, energized);
        return getSum(input.size(), input.get(0).length(), energized);
    }

    private static int partTwo(ArrayList<String> input) {
        int max = 0;
        for (int i = 0; i < input.size(); i++) {
            max = Math.max(solveForStart(input, 0, i, RIGHT), max);
            max = Math.max(solveForStart(input, input.get(0).length() - 1, i, LEFT), max);
        }
        for (int j = 0; j < input.get(0).length(); j++) {
            max = Math.max(solveForStart(input, j, 0, DOWN), max);
            max = Math.max(solveForStart(input, j, input.size() - 1, UP), max);
        }
        return max;
    }

    private static void solveEnergized(ArrayList<String> input, int x, int y, int direction, Set<String> memo, boolean[][] energized) {
        if (x < 0 || x >= input.get(0).length() || y < 0 || y >= input.size()) {
            return;  // out of bounds
        }

        String key = keyMaker(x, y, direction);
        if (memo.contains(key)) {
            return;  // solved for these inputs before
        } else {
            memo.add(key);
        }
        energized[y][x] = true;

        int[] move = {1, 1, -1, -1};
        int[] reflect = {DOWN, RIGHT, UP, LEFT};
        char c = input.get(y).charAt(x);
        if (c == '.') {
            // continue in same direction
            if (direction == RIGHT || direction == LEFT) {
                x += move[direction];
            } else {
                y += move[direction];
            }
            solveEnergized(input, x, y, direction, memo, energized);
        } else if (c == '\\') {
            // reflected 90 degrees
            if (direction == RIGHT || direction == LEFT) {
                y += move[direction];
            } else {
                x += move[direction];
            }
            direction = reflect[direction];
            solveEnergized(input, x, y, direction, memo, energized);
        } else if (c == '/') {
            // reflected 90 degrees
            if (direction == RIGHT || direction == LEFT) {
                y += move[(direction + 2) % 4];
            } else {
                x += move[(direction + 2) % 4];
            }
            direction = reflect[(direction + 2) % 4];
            solveEnergized(input, x, y, direction, memo, energized);
        } else if (c == '|') {
            if (direction == DOWN || direction == UP) {
                // continue in same direction
                y += move[direction];
                solveEnergized(input, x, y, direction, memo, energized);
            } else {
                // split
                solveEnergized(input, x, y + 1, DOWN, memo, energized);
                solveEnergized(input, x, y - 1, UP, memo, energized);
            }
        } else if (c == '-') {
            if (direction == RIGHT || direction == LEFT) {
                // continue in same direction
                x += move[direction];
                solveEnergized(input, x, y, direction, memo, energized);
            } else {
                // split
                solveEnergized(input, x + 1, y, RIGHT, memo, energized);
                solveEnergized(input, x - 1, y, LEFT, memo, energized);
            }
        }
    }

    private static String keyMaker(int x, int y, int direction) {
        String str = "";
        return str + x + "_" + y + "_" + direction;
    }

    private static int getSum(int rows, int cols, boolean[][] energized) {
        int sum = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sum += energized[i][j] ? 1 : 0;
            }
        }
        return sum;
    }
}