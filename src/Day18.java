import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day18 {
    private static final int RIGHT = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int UP = 3;

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/18.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        ArrayList<Instruction> instructions = getInput(scanner);

        System.out.printf("Part one: %d\n", partOne(instructions));
        System.out.printf("Part two: %d\n", partTwo(instructions));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static ArrayList<Instruction> getInput(Scanner scanner) {
        ArrayList<Instruction> instructions = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] split = line.split(" ");
            instructions.add(new Instruction(split[0], split[1], split[2].replaceAll("[(#)]", "")));
        }
        scanner.close();
        return instructions;
    }

    private static long partOne(ArrayList<Instruction> instructions) {
        int x = 0;
        int y = 0;
        long steps = 0;
        int[] moves = {1, 1, -1, -1};
        ArrayList<Point> points = new ArrayList<>();
        for (Instruction instruction : instructions) {  // map out corner points
            points.add(new Point(x, y));
            if (instruction.direction == RIGHT || instruction.direction == LEFT) {
                x += moves[instruction.direction] * instruction.meters;
            } else {
                y += moves[instruction.direction] * instruction.meters;
            }
            steps += instruction.meters;
        }
        // Pick's theorem
        return shoelace(points) + steps / 2 + 1;
    }

    private static long partTwo(ArrayList<Instruction> instructions) {
        int x = 0;
        int y = 0;
        long steps = 0;
        int[] moves = {1, 1, -1, -1};
        ArrayList<Point> points = new ArrayList<>();
        for (Instruction instruction : instructions) {  // map out corner points
            points.add(new Point(x, y));
            if (instruction.hexMeters == RIGHT || instruction.hexMeters == LEFT) {
                x += moves[instruction.hexMeters] * instruction.hex;
            } else {
                y += moves[instruction.hexMeters] * instruction.hex;
            }
            steps += instruction.hex;
        }
        // pick's theorem
        return shoelace(points) + steps / 2 + 1;
    }

    private static long shoelace(ArrayList<Point> points) {
        // Shoelace method
        long x_prev = points.get(0).x;
        long y_prev = points.get(0).y;
        long area = 0;
        for (int i = 1; i < points.size(); i++) {
            long x_next = points.get(i).x;
            long y_next = points.get(i).y;
            area += x_prev * y_next - y_prev * x_next;
            x_prev = x_next;
            y_prev = y_next;
        }
        area += x_prev * points.get(0).y - y_prev * points.get(0).x;
        return Math.abs(area) / 2;
    }

    private record Point(int x, int y){}

    private static class Instruction {
        private final int direction;
        private final int meters;
        private final int hex;
        private final int hexMeters;
        public Instruction(String direction, String meters, String hex) {
            switch (direction) {
                case "R" -> this.direction = RIGHT;
                case "D" -> this.direction = DOWN;
                case "L" -> this.direction = LEFT;
                default  -> this.direction = UP;
            }
            this.meters = Integer.parseInt(meters);
            this.hex = Integer.parseInt(hex.substring(0, hex.length() - 1), 16);
            this.hexMeters = Integer.parseInt(hex.substring(hex.length() - 1));
        }
    }
}