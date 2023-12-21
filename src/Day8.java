import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day8 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/8.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        Map<String, Node> graph = new HashMap<>();
        ArrayList<String> starts = new ArrayList<>();
        char[] directions = scanner.nextLine().trim().toCharArray();
        getInstructions(scanner, graph, starts);

        System.out.printf("Part one: %d\n", solve(directions, graph, "AAA", "ZZZ"));
        System.out.printf("Part two: %d\n", partTwo(directions, graph, starts));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static void getInstructions(Scanner scanner, Map<String, Node> graph, ArrayList<String> starts) {
        scanner.nextLine();
        while(scanner.hasNext()) {
            String[] splits = scanner.nextLine().split("=");
            String node = splits[0].trim();
            String[] edges = splits[1].replaceAll("[()]", "").split(",");
            graph.put(node, new Node(node, edges[0].trim(), edges[1].trim()));
            if (node.endsWith("A")) {
                starts.add(node);
            }
        }
        scanner.close();
    }

    private static int solve(char[] directions, Map<String, Node> graph, String start, String end) {
        int steps = 0;
        while (!start.endsWith(end)) {
            if (directions[steps++ % directions.length] == 'L') {
                start = graph.get(start).left;
            } else {
                start = graph.get(start).right;
            }
        }
        return steps;
    }

    private static long partTwo(char[] directions, Map<String, Node> graph, ArrayList<String> starts) {
        long steps = 1;
        for (String start : starts) {
            steps = lcm(steps, solve(directions, graph, start, "Z"));
        }
        return steps;
    }

    private static long gcd(long a, long b) {
        if (a == 0) {
            return b;
        }
        return gcd(b % a, a);
    }

    private static long lcm(long a, long b) {
        return (a / gcd(a, b)) * b;
    }

    private record Node(String location, String left, String right) {}
}