import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class Day17 {
    private static final int DIRECTIONS = 4;
    private static final int RIGHT = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int UP = 3;

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/17.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        int[][] input = getInput(scanner);
        System.out.printf("Part one: %d\n", shortestPath(input, 1, 3));
        System.out.printf("Part two: %d\n", shortestPath(input, 4, 10));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static int[][] getInput(Scanner scanner) {
        ArrayList<String> input = new ArrayList<>();
        while (scanner.hasNext()) {
            input.add(scanner.nextLine());
        }
        scanner.close();
        int rows = input.size();
        int cols = input.get(0).length();
        int[][] inputMatrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                inputMatrix[i][j] = Character.getNumericValue(input.get(i).charAt(j));
            }
        }
        return inputMatrix;
    }

    public static int shortestPath(int[][] input, int min, int max) {
        min--;
        int rows = input.length;
        int cols = input[0].length;

        int[][][][] totalCosts = new int[DIRECTIONS][max][rows][cols];
        initTotalCosts(max, rows, totalCosts);

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost));
        priorityQueue.offer(new Node(0, 0, 0, RIGHT, 0));

        // possible moves (RIGHT, DOWN, LEFT, UP)
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};

        Set<String> visited = new HashSet<>();
        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();
            if (!visited.add(currentNode.getKey())) {
                continue;
            }
            // Check all possible moves
            for (int DIRECTION : possibleMoves(currentNode, max)) {
                int newX = currentNode.x + dx[DIRECTION];
                int newY = currentNode.y + dy[DIRECTION];
                int newCost = 0;
                if (DIRECTION != currentNode.direction) {
                    // move min (-1) more times in this direction
                    if (newX + min * dx[DIRECTION] < 0 || newX + min * dx[DIRECTION] >= cols ||
                        newY + min * dy[DIRECTION] < 0 || newY + min * dy[DIRECTION] >= rows) {
                        continue;
                    } else {
                        for (int j = 0; j < min; j++) {
                            newCost += input[newY][newX];
                            newX += dx[DIRECTION];
                            newY += dy[DIRECTION];
                        }
                    }
                }
                // Update the distance if it's shorter for this state and add to queue
                if (newX >= 0 && newX < cols && newY >= 0 && newY < rows) {
                    newCost += currentNode.cost + input[newY][newX];
                    int stepsMade = currentNode.direction == DIRECTION ? currentNode.consecutiveSteps + 1 : min + 1;
                    if (newCost < totalCosts[currentNode.direction][stepsMade-1][newY][newX]) {
                        totalCosts[currentNode.direction][stepsMade-1][newY][newX] = newCost;
                        priorityQueue.offer(new Node(newX, newY, newCost, DIRECTION, stepsMade));
                    }
                }
            }
        }
        return minCost(max, totalCosts, rows, cols);
    }

    private static void initTotalCosts(int max, int rows, int[][][][] totalCosts) {
        for (int i = 0; i < DIRECTIONS; i++) {
            for (int j = 0; j < max; j++) {
                for (int k = 0; k < rows; k++) {
                    Arrays.fill(totalCosts[i][j][k], Integer.MAX_VALUE);
                }
            }
        }
    }

    private static ArrayList<Integer> possibleMoves(Node node, int max) {
        ArrayList<Integer> moves = new ArrayList<>();
        if (!(node.consecutiveSteps == max && node.direction == RIGHT) && !(node.direction == LEFT)) {
            moves.add(RIGHT);
        }
        if (!(node.consecutiveSteps == max && node.direction == DOWN) && !(node.direction == UP)) {
            moves.add(DOWN);
        }
        if (!(node.consecutiveSteps == max && node.direction == LEFT) && !(node.direction == RIGHT)) {
            moves.add(LEFT);
        }
        if (!(node.consecutiveSteps == max && node.direction == UP) && !(node.direction == DOWN)) {
            moves.add(UP);
        }
        return moves;
    }

    private static int minCost(int max, int[][][][] totalCosts, int rows, int cols) {
        int result = Integer.MAX_VALUE;
        for (int j = 0; j < max; j++) {
            result = Math.min(totalCosts[RIGHT][j][rows - 1][cols - 1], result);
            result = Math.min(totalCosts[DOWN][j][rows - 1][cols - 1], result);
        }
        return result;
    }

    private record Node(int x, int y, int cost, int direction, int consecutiveSteps) {
        private String getKey() {
            String key = "";
            return key + x + "_" + y + "_" + direction + "_" + consecutiveSteps;
        }
    }
}