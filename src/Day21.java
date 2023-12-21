import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Day21 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("21/src/input.txt");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        char[][] matrix = getInput(scanner);
        System.out.printf("Part one: %d\n", solve(matrix, 64));
        System.out.printf("Part two: %d\n", solve(matrix, 26501365));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static char[][] getInput(Scanner scanner) {
        ArrayList<String> input = new ArrayList<>();
        while(scanner.hasNext()) {
            input.add(scanner.nextLine());
        }
        char[][] matrix = new char[input.size()][input.get(0).length()];
        int i = 0;
        for (String str : input) {
            int j = 0;
            for (char c : str.toCharArray()) {
                matrix[i][j++] = c;
            }
            i++;
        }
        scanner.close();
        return matrix;
    }

    private static long solve(char[][] matrix, int steps) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        int[] start = new int[2];
        findStart(matrix, start);

        ArrayList<Integer> values = new ArrayList<>();
        HashMap<Integer, Set<Integer>> tiles = new HashMap<>();  // map x to a set of y
        tiles.put(start[0], new HashSet<>());
        tiles.get(start[0]).add(start[1]);

        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};

        int sum = -1;
        for (int i = 1; i <= steps; i++) {
            sum = 0;
            HashMap<Integer, Set<Integer>> newTiles = new HashMap<>();
            for (int x : tiles.keySet()) {
                for (int y : tiles.get(x)) {
                    for (int j = 0; j < 4; j++) {
                        int new_x = x + dx[j];
                        int new_y = y + dy[j];
                        if (matrix[Math.floorMod(new_y, rows)][Math.floorMod(new_x, cols)] != '#') {
                            if (!newTiles.containsKey(new_x) || !newTiles.get(new_x).contains(new_y)) {
                                sum++;
                                newTiles.computeIfAbsent(new_x, key -> new HashSet<>()).add(new_y);
                            }
                        }
                    }
                }
            }
            tiles = newTiles;
            if (i % cols == steps % cols) {
                values.add(sum);
                if (values.size() == 3) {  // find nth term of quadratic sequence
                    int c = values.get(0);
                    int b = values.get(1) - values.get(0);
                    int a = values.get(2) - values.get(1);
                    int n = steps / rows;
                    return c + (long) b * n + ((long) n * (n - 1) / 2) * (a - b);
                }
            }
        }
        return sum;
    }

    private static void findStart(char[][] matrix, int[] start) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 'S') {
                    start[0] = j;
                    start[1] = i;
                    return;
                }
            }
        }
    }
}