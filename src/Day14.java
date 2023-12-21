import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day14 {
    private static final int CYCLES = 1000000000;

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/14.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        ArrayList<String> input = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            input.add(line);
        }
        scanner.close();

        System.out.printf("Part one: %d\n", partOne(input));
        System.out.printf("Part two: %d\n", partTwo(input));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static int partOne(ArrayList<String> input) {
        ArrayList<String> cpy = new ArrayList<>(input);
        moveUp(cpy);
        return findLoad(cpy);
    }

    private static void moveUp(ArrayList<String> input) {
        // Find all 'O' and roll them up
        for (int i = 0; i < input.get(0).length(); i++) {
            for (int j = 0; j < input.size(); j++) {
                if (input.get(j).charAt(i) == 'O') {
                    roll(input, i, j);
                }
            }
        }
    }

    private static void roll(ArrayList<String> input, int x, int y) {
        // roll up an 'O' as far as possible
        int originalY = y;
        while (y > 0 && input.get(y-1).charAt(x) == '.') {
            y--;
        }
        if (y < originalY) {
            String currentRow = input.get(y);
            String originalRow = input.get(originalY);
            input.set(y, currentRow.substring(0, x) + "O" + currentRow.substring(x + 1));
            input.set(originalY, originalRow.substring(0, x) + "." + originalRow.substring(x + 1));
        }
    }

    private static int findLoad(ArrayList<String> partOne) {
        int sum = 0;
        for (int i = 0; i < partOne.size(); i++) {
            for (int j = 0; j < partOne.get(0).length(); j++) {
                if (partOne.get(i).charAt(j) == 'O') {
                    sum += partOne.size() - i;
                }
            }
        }
        return sum;
    }

    private static int partTwo(ArrayList<String> input) {
        Map<ArrayList<String>, Integer> cycleMap = new HashMap<>();
        for (int i = 1; i < CYCLES; i++) {
            for (int j = 0; j < 4; j++) {
                moveUp(input);
                input = rotate(input);
            }
            if (cycleMap.containsKey(input) && (CYCLES - i) % (i - cycleMap.get(input)) == 0) {
                break;
            } else {
                cycleMap.put(new ArrayList<>(input), i);
            }
        }
        return findLoad(input);
    }

    private static ArrayList<String> rotate(ArrayList<String> input) {
        // rotate a matrix clockwise
        ArrayList<String> rotatedInput = new ArrayList<>();
        for (int i = 0; i < input.get(0).length(); i++) {
            String str = "";
            for (int j = input.size() - 1; j >= 0; j--) {
                str += input.get(j).charAt(i);
            }
            rotatedInput.add(str);
        }
        return rotatedInput;
    }
}