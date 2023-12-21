import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day9 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/9.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        int extrapolations = 0;
        int backExtrapolations = 0;

        while (scanner.hasNext()) {
            int[] intArray = getNumbersOnLine(scanner);
            extrapolations += extrapolate(intArray);
            backExtrapolations += backExtrapolate(intArray);
        }
        scanner.close();

        System.out.printf("Part one: %d\n", extrapolations);
        System.out.printf("Part two: %d\n", backExtrapolations);
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static int[] getNumbersOnLine(Scanner scanner) {
        String string = scanner.nextLine();
        String[] history = string.split(" ");
        int[] intArray = new int[history.length];
        for (int i = 0; i < history.length; i++) {
            intArray[i] = Integer.parseInt(history[i]);
        }
        return intArray;
    }

    private static int extrapolate(int[] nums) {
        Diffs diffs = findDiffs(nums);
        if (diffs.allZeroes) {
            return nums[nums.length - 1];
        } else {
            return nums[nums.length - 1] + extrapolate(diffs.nums);
        }
    }

    private static int backExtrapolate(int[] nums) {
        Diffs diffs = findDiffs(nums);
        if (diffs.allZeroes) {
            return nums[0];
        } else {
            return nums[0] - backExtrapolate(diffs.nums);
        }
    }

    private static Diffs findDiffs(int[] nums) {
        boolean allZeroes = true;
        int[] diffs = new int[nums.length - 1];
        for (int i = 0; i < nums.length - 1; i++) {
            diffs[i] = nums[i + 1] - nums[i];
            allZeroes = diffs[i] == 0 && allZeroes;
        }
        return new Diffs(allZeroes, diffs);
    }

    private record Diffs(boolean allZeroes, int[] nums) {}
}