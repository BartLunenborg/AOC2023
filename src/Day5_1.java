import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day5_1 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/5.input");
        Scanner scanner = new Scanner(file);

        ArrayList<Long> seeds = getSeeds(scanner);
        ArrayList<Long> updated = new ArrayList<>();
        processUpdates(scanner, seeds, updated);

        long min = Long.MAX_VALUE;
        for (Long seed : seeds) {
            min = seed < min ? seed : min;
        }
        System.out.printf("Part one: %d\n", min);
    }

    private static ArrayList<Long> getSeeds(Scanner scanner) {
        ArrayList<Long> seeds = new ArrayList<>();
        String line = scanner.nextLine();
        String[] split = line.split(" ");
        for (int i = 1; i < split.length; i++) {
            seeds.add(Long.parseLong(split[i]));
        }
        return seeds;
    }

    private static void processUpdates(Scanner scanner, ArrayList<Long> seeds, ArrayList<Long> updated) {
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (!line.isEmpty()) {
                update(line, seeds, updated);
            } else {
                scanner.nextLine();
                updated = new ArrayList<>();
            }
        }
        scanner.close();
    }

    private static void update(String line, ArrayList<Long> seeds, ArrayList<Long> updated) {
        String[] split = line.split(" ");
        long start = Long.parseLong(split[1]);
        long destination = Long.parseLong(split[0]);
        long range = Long.parseLong(split[2]);
        for (Long seed : seeds) {
            if (start <= seed && seed < start + range && !updated.contains(seed)) {
                long update = destination + (seed - start);
                seeds.set(seeds.indexOf(seed), update);
                updated.add(update);
            }
        }
    }
}