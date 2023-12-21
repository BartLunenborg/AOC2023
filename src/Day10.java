import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day10 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/10.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        Pipe[][] matrix = getInput(scanner);
        Pipe start = findStart(matrix);
        start.connector = '|';  // hardcoded (by looking at input) for ease
        start.seen = true;

        System.out.printf("Part one: %d\n", findMaxDistance(matrix, start));
        System.out.printf("part two: %d\n", findSumContained(matrix));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static Pipe[][] getInput(Scanner scanner) {
        ArrayList<String> input = new ArrayList<>();
        while (scanner.hasNext()) {
            input.add(scanner.nextLine());
        }
        int rows = input.size();
        int cols = input.get(0).length();
        Pipe[][] matrix = new Pipe[rows][cols];
        for (int i = 0; i < rows; i++) {
            String line = input.get(i);
            for (int j = 0; j < cols; j++) {
                Pipe pipe = new Pipe(line.charAt(j), j, i);
                matrix[i][j] = pipe;
            }
        }
        scanner.close();
        return matrix;
    }

    private static Pipe findStart(Pipe[][] matrix) {
        for (Pipe[] row : matrix) {
            for (Pipe pipe : row) {
                if (pipe.connector == 'S') {
                    return pipe;
                }
            }
        }
        return null;
    }

    private static int findMaxDistance(Pipe[][] matrix, Pipe start) {
        Pipe a = getNext(matrix, start);
        Pipe b = getNext(matrix, start);

        int dist = 1;
        while (a != b) {
            a = getNext(matrix, a);
            b = getNext(matrix, b);
            dist++;
        }
        return dist;
    }

    private static Pipe getNext(Pipe[][] matrix, Pipe pipe) {
        int x = pipe.x;
        int y = pipe.y;
        switch (pipe.connector) {
            case 'L' -> {
                return notSeenNext(matrix, x, y - 1, x + 1, y);
            }
            case 'F' -> {
                return notSeenNext(matrix, x + 1, y, x, y + 1);
            }
            case 'J' -> {
                return notSeenNext(matrix, x, y - 1, x - 1, y);
            }
            case '7' -> {
                return notSeenNext(matrix, x, y + 1, x - 1, y);
            }
            case '|' -> {
                return notSeenNext(matrix, x, y + 1, x, y - 1);
            }
            default -> {
                return notSeenNext(matrix, x - 1, y, x + 1, y);
            }
        }
    }

    private static Pipe notSeenNext(Pipe[][] matrix, int x_1, int y_1, int x_2, int y_2) {
        if (!matrix[y_1][x_1].seen) {
            matrix[y_1][x_1].seen = true;
            return matrix[y_1][x_1];
        } else {
            matrix[y_2][x_2].seen = true;
            return matrix[y_2][x_2];
        }
    }

    private static int findSumContained(Pipe[][] matrix) {
        augmentPipes(matrix);
        int sum = 0;
        for (Pipe[] row : matrix) {
            int interior = 0;
            for (Pipe pipe : row) {
                if (pipe.connector == '|') {
                    interior++;
                } else if (pipe.connector == '.' && interior % 2 == 1) {
                    sum++;
                }
            }
        }
        return sum;
    }

    private static void augmentPipes(Pipe[][] matrix) {
        int cols = matrix[0].length;
        for (Pipe[] pipes : matrix) {
            for (int j = 0; j < cols; j++) {
                Pipe pipe = pipes[j];
                if (!pipe.seen) {
                    pipe.connector = '.';
                } else if (pipe.connector == 'F' || pipe.connector == 'L') {
                    char con = pipe.connector;
                    do {
                        pipe.connector = '_';
                        j++;
                        pipe = pipes[j];
                    } while (pipe.connector == '-');
                    if (con == 'F' && pipe.connector == 'J' ||
                        con == 'L' && pipe.connector == '7') {
                        pipe.connector = '|';
                    } else {
                        pipe.connector = '_';
                    }
                }
            }
        }
    }

    private static class Pipe {
        private char connector;
        private final int x;
        private final int y;
        private boolean seen;
        public Pipe(char connector, int x, int y) {
            this.connector = connector;
            this.x = x;
            this.y = y;
            this.seen = false;
        }
    }
}