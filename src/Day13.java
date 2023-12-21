import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day13 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/13.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        long one = 0;
        long two = 0;
        while (scanner.hasNext()) {
            ArrayList<String> pattern = getPattern(scanner);
            one += reflectedLines(pattern);
            two += reflectedLinesSmudge(pattern);
        }
        scanner.close();

        System.out.printf("Part one: %d\n", one);
        System.out.printf("Part two: %d\n", two);
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static ArrayList<String> getPattern(Scanner scanner) {
        ArrayList<String> pattern = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            pattern.add(line);
        }
        return pattern;
    }

    private static int reflectedLines(ArrayList<String> pattern) {
        // look in rows
        for (int i = 0; i < pattern.size() - 1; i++) {
            if (pattern.get(i).equals(pattern.get(i+1)) && isReflectingRows(pattern, i-1, i+2)) {
                return (i + 1) * 100;
            }
        }

        // look at columns
        for (int i = 0; i < pattern.get(0).length() - 1; i++) {
            int finalI = i;
            if (pattern.stream().allMatch(s -> s.charAt(finalI) == s.charAt(finalI + 1)) && isReflectingColumns(pattern, i-1, i+2)) {
                return i + 1;
            }
        }

        System.out.println("Should not be here!");
        return -1;
    }

    private static boolean isReflectingRows(ArrayList<String> pattern, int i, int j) {
        boolean reflecting = true;
        while (reflecting && i >= 0 && j < pattern.size()) {
            reflecting = pattern.get(i).equals(pattern.get(j));
            i--;
            j++;
        }
        return reflecting;
    }

    private static boolean isReflectingColumns(ArrayList<String> pattern, int i, int j) {
        boolean reflecting = true;
        while (reflecting && i >= 0 && j < pattern.get(0).length()) {
            int finalI = i;
            int finalJ = j;
            reflecting = pattern.stream().allMatch(s -> s.charAt(finalI) == s.charAt(finalJ));
            i--;
            j++;
        }
        return reflecting;
    }

    private static int reflectedLinesSmudge(ArrayList<String> pattern) {
        // look in columns
        for (int j = 0; j < pattern.get(0).length()-1; j++) {
            int diffs = 0;
            for (String s : pattern) {
                diffs = s.charAt(j) == s.charAt(j + 1) ? diffs : diffs + 1;
                if (diffs > 1) {
                    break;
                }
            }
            if (diffs < 2) {
                // check if solved
                int left  = j-1;
                int right = j+2;
                while (diffs < 2 && left >= 0 && right < pattern.get(0).length()) {
                    for (String s : pattern) {
                        diffs = s.charAt(left) == s.charAt(right) ? diffs : diffs + 1;
                    }
                    left--;
                    right++;
                }
                if (diffs == 1) {
                    return j + 1;
                }
            }
        }

        // look at rows
        for (int i = 0; i < pattern.size() - 1; i++) {
            int diffs = 0;
            for (int j = 0; j < pattern.get(0).length(); j++) {
                diffs = pattern.get(i).charAt(j) == pattern.get(i+1).charAt(j) ? diffs : diffs + 1;
                if (diffs > 1) {
                    break;
                }
            }
            if (diffs < 2) {
                // check if solved
                int top = i + 2;
                int bottom = i-1;
                while (diffs < 2 && bottom >= 0 && top < pattern.size()) {
                    for (int j = 0; j < pattern.get(0).length(); j++) {
                        diffs = pattern.get(bottom).charAt(j) == pattern.get(top).charAt(j) ? diffs : diffs + 1;
                    }
                    top++;
                    bottom--;
                }
                if (diffs == 1) {
                    return (i + 1) * 100;
                }
            }
        }

        System.out.println("Should not be here!");
        return -1;
    }
}