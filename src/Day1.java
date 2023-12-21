import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day1 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/1.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        ArrayList<String> input = new ArrayList<>();
        while (scanner.hasNext()) {
            input.add(scanner.nextLine());
        }
        scanner.close();

        System.out.printf("Part one: %d\n", partOne(input));
        System.out.printf("Part two: %d\n", partTwo(input));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static int partOne(ArrayList<String> input) {
        int sum = 0;
        for (String str : input) {
            str = str.replaceAll("[^0-9]", "");
            sum += 10 * Character.getNumericValue(str.charAt(0)) + Character.getNumericValue(str.charAt(str.length()-1));
        }
        return sum;
    }

    private static int partTwo(ArrayList<String> input) {
        int sum = 0;
        for (String str : input) {
            str = str.replace("one", "o1e");
            str = str.replace("two", "t2");
            str = str.replace("three", "t3e");
            str = str.replace("four", "4");
            str = str.replace("five", "5e");
            str = str.replace("six", "6");
            str = str.replace("seven", "7n");
            str = str.replace("eight", "e8");
            str = str.replace("nine", "9");
            str = str.replaceAll("[^0-9]", "");
            sum += 10 * Character.getNumericValue(str.charAt(0)) + Character.getNumericValue(str.charAt(str.length()-1));
        }
        return sum;
    }
}