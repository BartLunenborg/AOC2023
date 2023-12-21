import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day2 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/2.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        ArrayList<Game> games = new ArrayList<>();
        int id = 0;
        while (scanner.hasNext()) {
            id++;
            Game game = new Game(id);
            String[] sets = scanner.nextLine().split(":")[1].split(";");
            for (String set : sets) {
                Map<String, Integer> info = new HashMap<>();
                String[] elements = set.trim().split(",");
                for (String element : elements) {
                    String[] tokens = element.trim().split(" ");
                    info.put(tokens[1], Integer.parseInt(tokens[0]));
                    game.addSet(info);
                }
            }
            games.add(game);
        }
        scanner.close();

        System.out.printf("Part one: %d\n", partOne(games));
        System.out.printf("Part two: %d\n", partTwo(games));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static int partOne(ArrayList<Game> games) {
        Map<String, Integer> maxes = maxMap();
        int sum = 0;
        for (Game game : games) {
            boolean valid = true;
            for (Map<String, Integer> subset : game.subsets) {
                for (String key : subset.keySet()) {
                    if (maxes.get(key) < subset.get(key)) {
                        valid = false;
                        break;
                    }
                }
            }
            sum = valid ? sum + game.id : sum;
        }
        return sum;
    }

    private static int partTwo(ArrayList<Game> games) {
        int sum = 0;
        for (Game game : games) {
            Map<String, Integer> minimums = minMap();
            for (Map<String, Integer> subset : game.subsets) {
                for (Map.Entry<String, Integer> entry : subset.entrySet()) {
                    if (minimums.get(entry.getKey()) < entry.getValue()) {
                        minimums.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            sum += minimums.values().stream().reduce(1, (a, b) -> a * b);
        }
        return sum;
    }

    private static Map<String, Integer> minMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("red", 1);
        map.put("green", 1);
        map.put("blue", 1);
        return map;
    }

    private static Map<String, Integer> maxMap() {
        return Map.of(
            "red", 12,
            "green", 13,
            "blue", 14
        );
    }

    private static class Game {
        private final int id;
        private final ArrayList<Map<String, Integer>> subsets;
        public Game(int id) {
            this.id = id;
            subsets = new ArrayList<>();
        }
        public void addSet(Map<String, Integer> set) {
            subsets.add(set);
        }
    }
}