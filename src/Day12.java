import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day12 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/12.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        ArrayList<Data> input = getInput(scanner);

        long one = 0;
        long two = 0;
        for (Data data : input) {
            one += numConfigs(dupeStr(data.str, 1), dupeInt(data.ints, 1), 0, new HashMap<>());
            two += numConfigs(dupeStr(data.str, 5), dupeInt(data.ints, 5), 0, new HashMap<>());
        }
        System.out.printf("Part one: %d\n", one);
        System.out.printf("Part two: %d\n", two);
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static ArrayList<Data> getInput(Scanner scanner) {
        ArrayList<Data> input = new ArrayList<>();
        while (scanner.hasNext()) {
            String[] split = scanner.nextLine().split(" ");
            ArrayList<Integer> ints = new ArrayList<>();
            for (String str : split[1].split(",")) {
                ints.add(Integer.parseInt(str));
            }
            input.add(new Data(split[0], ints));
        }
        scanner.close();
        return input;
    }

    private static long numConfigs(String str, ArrayList<Integer> ints, int runningCount, Map<String, Long> memo) {
        String key = runningCount + "_" + str;
        for (Integer num : ints) {
            key += "_" + num;
        }
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        long val;
        if (str.charAt(0) == '.') {
            if (runningCount > 0 && (ints.isEmpty() || runningCount != ints.get(0))) {
                val = 0;
            } else {
                if (runningCount != 0) {
                    ints = new ArrayList<>(ints);
                    ints.remove(0);
                }
                if (str.length() == 1) {
                    val = ints.isEmpty() ? 1 : 0;
                } else {
                    val = numConfigs(str.substring(1), ints, 0, memo);
                }
            }
        } else if (str.charAt(0) == '#') {
            val = numConfigs(str.substring(1), ints, ++runningCount, memo);
        } else {
            val = numConfigs("." + str.substring(1), ints, runningCount, memo) +
                   numConfigs("#" + str.substring(1), ints, runningCount, memo);
        }
        memo.put(key, val);
        return val;
    }

    private static String dupeStr(String str, int times) {
        String dupe = str;
        for (int i = 0; i < times - 1; i++) {
            dupe += "?" + str;
        }
        dupe += ".";  // extra dot for base case
        return dupe;
    }

    private static ArrayList<Integer> dupeInt(ArrayList<Integer> ints, int times) {
        ArrayList<Integer> dupe = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            dupe.addAll(ints);
        }
        return dupe;
    }

    private record Data(String str, ArrayList<Integer> ints) {}
}
