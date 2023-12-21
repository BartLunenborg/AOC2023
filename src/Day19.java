import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day19 {
    private static final String START = "in";

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/19.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        Map<String, Workflow> workflows = new HashMap<>();
        ArrayList<Part> parts = new ArrayList<>();
        getInput(scanner, workflows, parts);

        System.out.printf("Part one: %d\n", partOne(workflows, parts));
        System.out.printf("Part two: %d\n", partTwo(workflows));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static void getInput(Scanner scanner, Map<String, Workflow> workflows, ArrayList<Part> parts) {
        String line = scanner.nextLine();
        while (!line.isEmpty()) {
            String[] split = line.split("\\{");
            String instructions = split[1].substring(0, split[1].length()-1);
            workflows.put(split[0], new Workflow(instructions));
            line = scanner.nextLine();
        }
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            String[] split = line.split(",");
            parts.add(new Part(Integer.parseInt(split[0].split("=")[1]),
                Integer.parseInt(split[1].split("=")[1]),
                Integer.parseInt(split[2].split("=")[1]),
                Integer.parseInt(split[3].split("=")[1].replaceAll("}", ""))));
        }
        scanner.close();
    }

    private static int partOne(Map<String, Workflow> workflows, ArrayList<Part> parts) {
        int sum = 0;
        for (Part part : parts) {
            if (acceptPart(part, workflows)) {
                sum += part.getSum();
            }
        }
        return sum;
    }

    private static boolean acceptPart(Part part, Map<String, Workflow> instructions) {
        String current = START;
        while (!(current.equals("A") || current.equals("R"))) {
            current = instructions.get(current).ratePart(part);
        }
        return current.equals("A");
    }

    private static long partTwo(Map<String, Workflow> instructions) {
        long val = 0;
        ArrayList<Range> ranges = new ArrayList<>();
        solveRanges(instructions, ranges, new Range(1, 4000, 1, 4000, 1, 4000, 1, 4000), START);
        for (Range range : ranges) {
            val += (long) (range.x_max - range.x_min + 1) * (range.m_max - range.m_min + 1) * (range.a_max - range.a_min + 1) * (range.s_max - range.s_min + 1);
        }
        return val;
    }

    private static void solveRanges(Map<String, Workflow> instructions, ArrayList<Range> ranges, Range range, String next) {
        if (next.equals("A")) {
            ranges.add(range);
        } else if (!next.equals("R")) {
            Workflow workflow = instructions.get(next);
            for (Rule rule : workflow.rules) {
                if (rule.rule.isEmpty()) {
                    solveRanges(instructions, ranges, range, rule.next);
                } else {
                    String[] split = rule.rule.split("[<>]");
                    String letter = split[0];
                    int val = Integer.parseInt(split[1]);
                    Range newRange = new Range(range.x_min, range.x_max, range.m_min, range.m_max, range.a_min, range.a_max, range.s_min, range.s_max);
                    if (rule.lessThanRule) {
                        switch (letter) {
                            case "x" -> {
                                newRange.x_max = val - 1;
                                range.x_min = val;
                                solveRanges(instructions, ranges, newRange, rule.next);
                            }
                            case "m" -> {
                                newRange.m_max = val - 1;
                                range.m_min = val;
                                solveRanges(instructions, ranges, newRange, rule.next);
                            }
                            case "a" -> {
                                newRange.a_max = val - 1;
                                range.a_min = val;
                                solveRanges(instructions, ranges, newRange, rule.next);
                            }
                            case "s" -> {
                                newRange.s_max = val - 1;
                                range.s_min = val;
                                solveRanges(instructions, ranges, newRange, rule.next);
                            }
                        }
                    } else {
                        switch (letter) {
                            case "x" -> {
                                newRange.x_min = val + 1;
                                range.x_max = val;
                                solveRanges(instructions, ranges, newRange, rule.next);
                            }
                            case "m" -> {
                                newRange.m_min = val + 1;
                                range.m_max = val;
                                solveRanges(instructions, ranges, newRange, rule.next);
                            }
                            case "a" -> {
                                newRange.a_min = val + 1;
                                range.a_max = val;
                                solveRanges(instructions, ranges, newRange, rule.next);
                            }
                            case "s" -> {
                                newRange.s_min = val + 1;
                                range.s_max = val;
                                solveRanges(instructions, ranges, newRange, rule.next);
                            }
                        }
                    }
                }
            }
        }
    }

    private static class Workflow {
        private final ArrayList<Rule> rules;
        public Workflow(String instructions) {
            rules = new ArrayList<>();
            String[] split = instructions.split(",");
            for (String instruction : split) {
                rules.add(new Rule(instruction));
            }
        }
        public String ratePart(Part part) {
            String next = "R";
            for (Rule rule : rules) {
                if (rule.rule.isEmpty()) {
                    next = rule.next;
                } else {
                    String[] split;
                    if (rule.lessThanRule) {
                        split = rule.rule.split("<");
                        if (part.getValue(split[0]) < Integer.parseInt(split[1])) {
                            next = rule.next;
                            break;
                        }
                    } else {
                        split = rule.rule.split(">");
                        if (part.getValue(split[0]) > Integer.parseInt(split[1])) {
                            next = rule.next;
                            break;
                        }
                    }
                }
            }
            return next;
        }
    }

    private static class Rule {
        private final String rule;
        private final String next;
        private boolean lessThanRule;
        public Rule(String instruction) {
            String[] split = instruction.split(":");
            if (split.length == 1) {
                rule = "";
                next = split[0];
            } else {
                rule = split[0];
                next = split[1];
                lessThanRule = split[0].contains("<");
            }
        }
    }

    private record Part(int x, int m, int a, int s) {
        public int getSum() {
                return x + m + a + s;
            }
        public int getValue(String var) {
                return switch (var) {
                    case "x" -> x;
                    case "m" -> m;
                    case "a" -> a;
                    default -> s;
                };
            }
        }

    private static class Range {
        private int x_min;
        private int x_max;
        private int m_min;
        private int m_max;
        private int a_min;
        private int a_max;
        private int s_min;
        private int s_max;
        public Range(int x_min, int x_max, int m_min, int m_max, int a_min, int a_max, int s_min, int s_max) {
            this.x_min = x_min;
            this.x_max = x_max;
            this.m_min = m_min;
            this.m_max = m_max;
            this.a_min = a_min;
            this.a_max = a_max;
            this.s_min = s_min;
            this.s_max = s_max;
        }
    }
}