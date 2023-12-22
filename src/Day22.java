import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Day22 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/22.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        ArrayList<Brick> bricks = getInput(scanner);

        System.out.printf("Part one: %d\n", partOne(bricks));
        System.out.printf("Part two: %d\n", partTwo(bricks));  // bricks already fell in part 1
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static ArrayList<Brick> getInput(Scanner scanner) {
        ArrayList<Brick> bricks = new ArrayList<>();
        while (scanner.hasNext()) {
            String[] split = scanner.nextLine().split("~");
            String[] a = split[0].split(",");
            String[] b = split[1].split(",");
            bricks.add(new Brick(Integer.parseInt(a[0]), Integer.parseInt(a[1]), Integer.parseInt(a[2]),
                                 Integer.parseInt(b[0]), Integer.parseInt(b[1]), Integer.parseInt(b[2])));
        }
        return bricks;
    }

    private static long partOne(ArrayList<Brick> bricks) {
        simulateFall(bricks);   // simulate the falling bricks
        findSupports(bricks);   // for all bricks find the bricks it supports and that support it
        return bricks.stream()  // for all brick check if the bricks it supports are not only supported by itself
            .filter(brick -> brick.supports.stream().allMatch(support -> support.supportedBy.size() > 1))
            .count();
    }

    private static void simulateFall(ArrayList<Brick> bricks) {
        bricks.sort(Comparator.comparingInt(Brick::getZ));
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            int new_z = 1;
            for (int j = i - 1; j >= 0; j--) {
                Brick other = bricks.get(j);
                if (overlap(brick, other)) {
                    new_z = Math.max(other.z_1 + 1, new_z);
                }
            }
            brick.z_1 += new_z - brick.z_0;
            brick.z_0  = new_z;
        }
    }

    private static boolean overlap(Brick a, Brick b) {
        return a.x_0 <= b.x_1 && a.x_1 >= b.x_0 && a.y_0 <= b.y_1 && a.y_1 >= b.y_0;
    }

    private static void findSupports(ArrayList<Brick> bricks) {
        for (Brick brick : bricks) {
            for (Brick other : bricks) {
                if (other != brick && overlap(brick, other)) {
                    if (other.z_1 == brick.z_0 - 1) {
                        other.supports.add(brick);
                        brick.supportedBy.add(other);
                    } else if (other.z_0 == brick.z_1 + 1) {
                        other.supportedBy.add(brick);
                        brick.supports.add(other);
                    }
                }
            }
        }
    }

    private static int partTwo(ArrayList<Brick> bricks) {
        int count = 0;
        for (Brick brick : bricks) {
            count += countTotalSupportedBy(brick);
        }
        return count;
    }

    private static int countTotalSupportedBy(Brick brick) {
        int count = 0;
        Set<Brick> removed = new HashSet<>();
        removed.add(brick);
        Queue<Brick> queue = new LinkedList<>(brick.supports);
        while (!queue.isEmpty()) {
            Brick current = queue.poll();
            if (removed.containsAll(current.supportedBy)) {  // this brick will then also fall
                count++;
                removed.add(current);                   // make it 'fall'
                queue.addAll(current.supports.stream()  // add all unexplored (or to be) bricks it supports to the queue
                    .filter(supported -> !removed.contains(supported) && !queue.contains(supported)).toList());
            }
        }
        return count;
    }

    private static class Brick {
        private final int x_0;
        private final int y_0;
        private int z_0;
        private final int x_1;
        private final int y_1;
        private int z_1;
        private final Set<Brick> supportedBy;
        private final Set<Brick> supports;
        public Brick(int x_0, int y_0, int z_0, int x_1, int y_1, int z_1) {
            this.x_0 = Math.min(x_0, x_1);
            this.y_0 = Math.min(y_0, y_1);
            this.z_0 = Math.min(z_0, z_1);
            this.x_1 = Math.max(x_0, x_1);
            this.y_1 = Math.max(y_0, y_1);
            this.z_1 = Math.max(z_0, z_1);
            this.supportedBy = new HashSet<>();
            this.supports = new HashSet<>();
        }
        public int getZ() {
            return z_0;
        }
    }
}