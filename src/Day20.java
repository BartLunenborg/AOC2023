import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Day20 {
    private static final String START = "roadcaster";  // broadcaster (but the 'b' gets eaten)
    private static final String RX = "rx";
    private static String DEST = "";
    private static final int NO_SIGNAL = -1;
    private static final int LOW = 0;
    private static final int HIGH = 1;
    private static final int CONJUNCTION = 1;
    private static final int FLIP_FLOP = 0;

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/20.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        Map<String, Module> modules = getInput(scanner);
        Map<String, Integer> parents = new HashMap<>();  // for part 2
        setupModules(modules, parents);

        System.out.printf("Part one: %d\n", solve(modules, parents));
        System.out.printf("Part two: %d\n", lcm(parents.values()));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static Map<String, Module> getInput(Scanner scanner) {
        Map<String, Module> modules = new HashMap<>();
        while (scanner.hasNext()) {
            String[] split = scanner.nextLine().split("->");
            modules.put(split[0].substring(1).trim(), new Module(split[0], split[1]));
        }
        scanner.close();
        return modules;
    }

    private static void setupModules(Map<String, Module> modules, Map<String, Integer> parents) {
        modules.get(START).type = NO_SIGNAL;      // so it always sends a low signal
        for (Module module : modules.values()) {  // find parent of rx
            if (module.destinations.contains(RX)) {
                DEST = module.name;
                break;
            }
        }
        for (Map.Entry<String, Module> entry : modules.entrySet()) {
            String name = entry.getKey();
            Module module = entry.getValue();
            if (module.type == CONJUNCTION) {  // make it keep track of all that send it a signal
                for (Map.Entry<String, Module> enter : modules.entrySet()) {
                    if (enter.getValue().destinations.contains(name)) {
                        module.incoming.put(enter.getKey(), LOW);
                    }
                }
            }
            if (module.destinations.contains(DEST)) {
                parents.put(module.name, 0);
            }
        }
    }

    private static long solve(Map<String, Module> modules, Map<String, Integer> parents) {
        long low = 1000;
        long high = 0;

        int presses = 0;
        Queue<Module> queue = new LinkedList<>();
        while (!parents.values().stream().allMatch(val -> val != 0)) {
            presses++;
            queue.offer(modules.get(START));
            while (!queue.isEmpty()) {
                Module current = queue.poll();
                int sending = current.getSending();
                if (sending != NO_SIGNAL) {
                    if (presses <= 1000) {
                        if (sending == LOW) {
                            low += current.destinations.size();
                        } else {
                            high += current.destinations.size();
                        }
                    }
                    for (String destination : current.destinations) {
                        if (modules.containsKey(destination)) {
                            queue.offer(modules.get(destination));
                            modules.get(destination).setReceived(sending, current.name);
                        }
                    }
                }
                if (sending == HIGH && parents.containsKey(current.name) && parents.get(current.name) == 0) {
                    parents.put(current.name, presses);
                }
            }
        }
        return low * high;
    }

    public static long lcm(Collection<Integer> numbers) {
        BigInteger[] bigIntegers = numbers.stream()
            .map(BigInteger::valueOf)
            .toArray(BigInteger[]::new);

        BigInteger result = bigIntegers[0];
        for (int i = 1; i < bigIntegers.length; i++) {
            result = result.multiply(bigIntegers[i]).divide(result.gcd(bigIntegers[i]));
        }

        return result.longValue();
    }

    private static class Module {
        private int type;
        private final String name;
        private final Set<String> destinations;
        private int sending;
        private final Queue<Integer> received;
        private final Map<String, Integer> incoming;
        public Module (String name, String destinations) {
            this.type = name.charAt(0) == '%' ? FLIP_FLOP : CONJUNCTION;
            this.name = name.substring(1).trim();
            this.sending = LOW;
            this.destinations = new HashSet<>();
            for (String destination : destinations.split(",")) {
                this.destinations.add(destination.trim());
            }
            this.received = new LinkedList<>();
            this.incoming = this.type == CONJUNCTION ? new HashMap<>() : null;
        }
        public int getSending() {
            if (type == FLIP_FLOP && received.poll() == HIGH) {
                return NO_SIGNAL;
            } else if (type == FLIP_FLOP) {
                sending = (sending + 1) % 2;
            } else if (type == CONJUNCTION) {
                this.sending = LOW;
                for (int val : incoming.values()) {
                    if (val != HIGH) {
                        this.sending = HIGH;
                        break;
                    }
                }
            }
            return sending;
        }
        public void setReceived(int signal, String sender) {
            if (incoming != null) {
                incoming.put(sender, signal);
            } else {
                received.offer(signal);
            }
        }
    }
}