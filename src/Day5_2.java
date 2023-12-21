import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day5_2 {
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        Day5_1.main(null);
        File file = new File("input/5.input");
        Scanner scanner = new Scanner(file);

        ArrayList<SeedRange> ranges = getSeedRanges(scanner);

        ArrayList<SeedRange> updated = new ArrayList<>();
        processUpdates(scanner, ranges, updated);

        System.out.printf("Part two: %d\n", findMinValue(ranges));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static ArrayList<SeedRange> getSeedRanges(Scanner scanner) {
        ArrayList<SeedRange> ranges = new ArrayList<>();
        String data = scanner.nextLine();
        String[] parts = data.split(" ");
        for (int i = 1; i < parts.length; i = i + 2) {
            long a = Long.parseLong(parts[i]);
            long b = Long.parseLong(parts[i + 1]);
            SeedRange range = new SeedRange(a, a + b - 1);
            ranges.add(range);
        }
        return ranges;
    }

    private static void processUpdates(Scanner scanner, ArrayList<SeedRange> ranges, ArrayList<SeedRange> updated) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.isEmpty()) {
                update(line, ranges, updated);
            } else {
                scanner.nextLine();
                updated = new ArrayList<>();
            }
        }
        scanner.close();
    }

    private static long findMinValue(ArrayList<SeedRange> ranges) {
        long minValue = Long.MAX_VALUE;
        for (SeedRange range : ranges) {
            if (range.start < minValue) {
                minValue = range.start;
            }
        }
        return minValue;
    }

    private static void update(String data, ArrayList<SeedRange> ranges, ArrayList<SeedRange> updated) {
        String[] nums = data.split(" ");
        long a = Long.parseLong(nums[0]);
        long b = Long.parseLong(nums[1]);
        long c = Long.parseLong(nums[2]);

        List<SeedRange> rangesToRemove = new ArrayList<>();
        List<SeedRange> rangesToAdd = new ArrayList<>();

        for (SeedRange range : ranges) {
            if (!updated.contains(range) && isRangeOverlap(range, b, c)) {
                if (isRangeFullyContained(range, b, c)) {
                    updateFullyContainedRange(range, a, b);
                    updated.add(range);
                } else if (isRangeSplitTwice(range, b, c)) {
                    splitRangeTwice(range, a, b, c, rangesToRemove, rangesToAdd, updated);
                } else {
                    splitRangeOnce(range, a, b, c, rangesToRemove, rangesToAdd, updated);
                }
            }
        }

        ranges.removeAll(rangesToRemove);
        ranges.addAll(rangesToAdd);
    }

    private static boolean isRangeOverlap(SeedRange range, long b, long c) {
        return range.end >= b && range.start < b + c;
    }

    private static boolean isRangeFullyContained(SeedRange range, long b, long c) {
        return range.start >= b && range.end < b + c;
    }

    private static boolean isRangeSplitTwice(SeedRange range, long b, long c) {
        return range.start < b && range.end > c;
    }

    private static void updateFullyContainedRange(SeedRange range, long a, long b) {
        range.start = a + (range.start - b);
        range.end = a + (range.end - b);
    }

    private static void splitRangeTwice(SeedRange range, long a, long b, long c,
                                        List<SeedRange> rangesToRemove, List<SeedRange> rangesToAdd, List<SeedRange> updated) {
        rangesToRemove.add(range);

        SeedRange one = new SeedRange(range.start, b - 1);
        SeedRange two = new SeedRange(a, a + c - 1);
        SeedRange three = new SeedRange(range.start + c, range.end);

        rangesToAdd.add(one);
        rangesToAdd.add(two);
        rangesToAdd.add(three);

        updated.add(two);
    }

    private static void splitRangeOnce(SeedRange range, long a, long b, long c,
                                       List<SeedRange> rangesToRemove, List<SeedRange> rangesToAdd, List<SeedRange> updated) {
        rangesToRemove.add(range);

        if (range.start < b) {
            SeedRange one = new SeedRange(range.start, b - 1);
            SeedRange two = new SeedRange(a, a + range.end - b);

            rangesToAdd.add(one);
            rangesToAdd.add(two);
            updated.add(two);
        } else {
            SeedRange one = new SeedRange(a + range.start - b, a + c - 1);
            SeedRange two = new SeedRange(c + b, range.end);

            rangesToAdd.add(one);
            rangesToAdd.add(two);
            updated.add(one);
        }
    }

    private static class SeedRange {
        private long start;
        private long end;
        public SeedRange(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }
}