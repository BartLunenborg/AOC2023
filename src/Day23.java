import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day23 {
    private static final int NON = -1;
    private static final int DIRECTIONS = 4;
    private static final int RIGHT = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int UP = 3;

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/23.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        ArrayList<String> input = new ArrayList<>();
        while (scanner.hasNext()) {
            input.add(scanner.nextLine());
        }
        scanner.close();
        int[][] max = new int[input.size()][input.get(0).length()];
        Tile[][] matrix = new Tile[input.size()][input.get(0).length()];
        Tile[] startEnd = new Tile[2];  // for start tile and end tile
        parseInput(input, matrix, max, startEnd);

        System.out.printf("Part one: %d\n", partOne(matrix, max, startEnd));
        resetValues(matrix, max, startEnd);  // set all max[i][j] to 0, matrix[i][j] to seen, and make start 1 down
        System.out.printf("Part two: %d\n", partTwo(matrix, max, startEnd));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static void parseInput(ArrayList<String> input, Tile[][] matrix, int[][] max, Tile[] startEnd) {
        int y = 0;
        for (String str : input) {
            int x = 0;
            for (char c : str.toCharArray()) {
                max[y][x] = 0;
                matrix[y][x] = new Tile(c, x, y);
                x++;
            }
            y++;
        }
        for (int i = 0; i < matrix[0].length; i++) {
            startEnd[0] = matrix[0][i].type == '.' ? matrix[0][i] : startEnd[0];
            startEnd[1] = matrix[matrix.length-1][i].type == '.' ? matrix[matrix.length-1][i] : startEnd[1];
        }
    }

    private static void resetValues(Tile[][] matrix, int[][] max, Tile[] startEnd) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j].seen = false;
                max[i][j] = 0;
            }
        }
        startEnd[0].seen = true;  // we move start 1 down to avoid having to check for out of bounds
        startEnd[0] = matrix[startEnd[0].y+1][startEnd[0].x];
    }

    private static int partOne(Tile[][] matrix, int [][] max, Tile[] startEnd) {
        dfsDirectedMaxPath(matrix, startEnd[0], startEnd[1], DOWN, 0, max);
        return max[startEnd[1].y][startEnd[1].x];
    }

    private static void dfsDirectedMaxPath(Tile[][] matrix, Tile current, Tile end, int direction, int currentLength, int[][] max) {
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};

        current.seen = true;
        max[current.y][current.x] = Math.max(currentLength, max[end.y][end.x]);
        currentLength++;

        if (current.equals(end)) {
            current.seen = false;
            return;
        }
        if (direction == NON) {
            for (int i = 0; i < DIRECTIONS; i++) {
                int x = current.x + dx[i];
                int y = current.y + dy[i];
                Tile next = matrix[y][x];
                if (next.type != '#' && !next.seen) {
                    dfsDirectedMaxPath(matrix, next, end, next.direction(), currentLength, max);
                }
            }
        } else {
            int x = current.x + dx[direction];
            int y = current.y + dy[direction];
            Tile next = matrix[y][x];
            if (!next.seen) {
                dfsDirectedMaxPath(matrix, next, end, next.direction(), currentLength, max);
            }
        }
        current.seen = false;
    }

    private static int partTwo(Tile[][] matrix, int[][] max, Tile[] startEnd) {
        dfsMaxPath(matrix, startEnd[0], startEnd[1],1, max);
        return max[startEnd[1].y][startEnd[1].x];
    }

    // quite slow and memory inefficient, but with increased stack size it will work (took 2-3 min on my machine)
    private static void dfsMaxPath(Tile[][] matrix, Tile current, Tile end, int currentLength, int[][] max) {
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};

        current.seen = true;
        max[current.y][current.x] = Math.max(currentLength, max[end.y][end.x]);
        currentLength++;

        if (current.equals(end)) {
            current.seen = false;
            return;
        }
        for (int i = 0; i < DIRECTIONS; i++) {
            int x = current.x + dx[i];
            int y = current.y + dy[i];
            Tile next = matrix[y][x];
            if (next.type != '#' && !next.seen) {
                dfsMaxPath(matrix, next, end, currentLength, max);
            }
        }
        current.seen = false;
    }

    private static class Tile {
        private final char type;
        private boolean seen;
        private final int x;
        private final int y;
        public Tile(char type, int x, int y) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.seen = false;
        }
        public int direction() {
            return switch (type) {
                case '.' -> NON;
                case '>' -> RIGHT;
                case 'v' -> DOWN;
                case '<' -> LEFT;
                default  -> UP;
            };
        }
    }
}