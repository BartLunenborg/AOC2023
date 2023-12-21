import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day6 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/6.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        ArrayList<Race> races = getInput(scanner);

        System.out.printf("Part one: %d\n", partOne(races));
        System.out.printf("Part two: %d\n", partTwo(races));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static ArrayList<Race> getInput(Scanner scanner) {
        ArrayList<Race> races = new ArrayList<>();
        int index = 0;

        String line = scanner.nextLine();
        String[] split = line.trim().split(" ");
        for (int i = 1; i < split.length; i++) {
            if (!split[i].isEmpty()) {
                races.add(new Race(Long.parseLong(split[i])));
            }
        }
        line = scanner.nextLine();
        split = line.trim().split(" ");
        for (int i = 1; i < split.length; i++) {
            if (!split[i].isEmpty()) {
                races.get(index++).record = Long.parseLong(split[i]);
            }
        }

        return races;
    }

    private static int partOne(ArrayList<Race> races) {
        int sum = 1;
        for (Race race : races) {
            sum *= solveRace(race);
        }
        return sum;
    }

    private static int partTwo(ArrayList<Race> races) {
        String combinedTime = "";
        String combinedRecord = "";
        for (Race race : races) {
            combinedTime += race.time;
            combinedRecord += race.record;
        }
        return solveRace(new Race(Long.parseLong(combinedTime), Long.parseLong(combinedRecord)));
    }

    private static int solveRace(Race race) {
        int sum = 0;
        for (int i = 1; i < race.time - 1; i++) {
            if ((race.time - i) * i > race.record) {
                sum++;
            }
        }
        return sum;
    }

    private static class Race {
        private final long time;
        private long record;
        public Race(long time) {
            this.time = time;
            record = 0;
        }
        public Race(long time, long record) {
            this.time = time;
            this.record = record;
        }
    }
}