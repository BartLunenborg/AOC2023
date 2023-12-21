import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Day15 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/15.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        ArrayList<String> input = new ArrayList<>(Arrays.asList(scanner.nextLine().split(",")));
        scanner.close();

        System.out.printf("Part one: %d\n", partOne(input));
        System.out.printf("Part tow: %d\n", partTwo(input));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static int partOne(ArrayList<String> input) {
        int sum = 0;
        for (String str : input) {
            sum += hashSum(str);
        }
        return sum;
    }

    private static int hashSum(String str) {
        int sum = 0;
        for (char c : str.toCharArray()) {
            sum = (sum + (int) c) * 17 % 256;
        }
        return sum;
    }

    private static int partTwo(ArrayList<String> input) {
        ArrayList<ArrayList<Lens>> boxes = asciiBoxes();
        for (String str : input) {
            if (str.contains("-")) {
                minusSub(str, boxes);
            } else {
                equalSub(str, boxes);
            }
        }

        int two = 0;
        for (ArrayList<Lens> box : boxes) {
            for (Lens lens : box) {
                two += (boxes.indexOf(box) + 1) * (box.indexOf(lens) + 1) * lens.focal;
            }
        }
        return two;
    }

    private static void minusSub(String str, ArrayList<ArrayList<Lens>> boxes) {
        String label = str.split("-")[0];
        int hash = hashSum(label);
        boxes.get(hash).removeIf(lens -> lens.label.equals(label));
    }

    private static void equalSub(String str, ArrayList<ArrayList<Lens>> boxes) {
        Lens lens = new Lens(str);
        int hash = hashSum(lens.label);
        boolean found = false;
        for (int i = 0; i < boxes.get(hash).size(); i++) {
            if (boxes.get(hash).get(i).label.equals(lens.label)) {
                boxes.get(hash).set(i, lens);
                found = true;
                break;
            }
        }
        if (!found) {
            boxes.get(hash).add(lens);
        }
    }

    private static ArrayList<ArrayList<Lens>> asciiBoxes() {
        ArrayList<ArrayList<Lens>> boxes = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            ArrayList<Lens> box = new ArrayList<>();
            boxes.add(box);
        }
        return boxes;
    }

    private static class Lens {
        private final String label;
        private final int focal;
        public Lens(String str) {
            String[] split = str.split("=");
            label = split[0];
            focal = Integer.parseInt(split[1]);
        }
    }
}