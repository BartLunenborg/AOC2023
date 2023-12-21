import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day11 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/11.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        ArrayList<Galaxy> galaxies = new ArrayList<>();
        ArrayList<Integer> emptyRows = new ArrayList<>();
        ArrayList<Integer> emptyCols = new ArrayList<>();
        parseInput(scanner, galaxies, emptyRows, emptyCols);

        int partOneSum = 0;
        long partTwoSum = 0;
        for (int i = 0; i < galaxies.size() - 1; i++) {
            int xFrom = galaxies.get(i).x;
            int yFrom = galaxies.get(i).y;
            for (int j = i + 1; j < galaxies.size(); j++) {
                int xTo = galaxies.get(j).x;
                int yTo = galaxies.get(j).y;
                int distance = Math.abs(xFrom - xTo) + Math.abs(yFrom - yTo);
                partOneSum += distance;
                partTwoSum += distance;
                for (int x : emptyCols) {
                    if ((x < xFrom && x > xTo) || (x > xFrom && x < xTo)) {
                        partOneSum += 1;
                        partTwoSum += 999999;
                    }
                }
                for (int y : emptyRows) {
                    if ((y < yFrom && y > yTo) || (y > yFrom && y < yTo)) {
                        partOneSum += 1;
                        partTwoSum += 999999;
                    }
                }
            }
        }

        System.out.printf("Part one: %d\n", partOneSum);
        System.out.printf("Part two: %d\n", partTwoSum);
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static void parseInput(Scanner scanner, ArrayList<Galaxy> galaxies, ArrayList<Integer> emptyRows, ArrayList<Integer> emptyCols) {
        ArrayList<String> input = new ArrayList<>();
        while (scanner.hasNext()) {
            input.add(scanner.nextLine());
        }
        scanner.close();

        int rows = input.size();
        int cols = input.get(0).length();
        for (int i = 0; i < rows; i++) {  // find galaxies and empty rows
            boolean emptyRow = true;
            for (int j = 0; j < cols; j++) {
                if (input.get(i).charAt(j) == '#') {
                    emptyRow = false;
                    galaxies.add(new Galaxy(j, i));
                }
            }
            if (emptyRow) {
                emptyRows.add(i);
            }
        }
        for (int j = 0; j < cols; j++) {  // find empty columns
            boolean emptyColumn = true;
            for (int i = 0; i < rows; i++) {
                if (input.get(i).charAt(j) == '#') {
                    emptyColumn = false;
                    break;
                }
            }
            if (emptyColumn) {
                emptyCols.add(j);
            }
        }
    }

    private record Galaxy(int x, int y) {}
}