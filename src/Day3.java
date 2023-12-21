import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day3 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/3.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        ArrayList<String> input = new ArrayList<>();
        while (scanner.hasNextLine()) {
            input.add(scanner.nextLine());
        }
        scanner.close();

        char[][] matrix = new char[input.size()][input.get(0).length()];
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.get(0).length(); j++) {
                matrix[i][j] = input.get(i).charAt(j);
            }
        }

        ArrayList<Part> parts = new ArrayList<>();
        ArrayList<Symbol> symbols = new ArrayList<>();
        parseMatrix(matrix, parts, symbols);

        System.out.printf("Part one: %d\n", partOne(parts));
        System.out.printf("Part two: %d\n", partTwo(symbols));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static int partOne(ArrayList<Part> parts) {
        int sum = 0;
        for (Part part : parts) {
            sum = part.adjacentSymbols.isEmpty() ? sum : sum + part.value;
        }
        return sum;
    }

    private static int partTwo(ArrayList<Symbol> symbols) {
        int sum = 0;
        for (Symbol symbol : symbols) {
            if (symbol.symbol == '*' && symbol.adjacentParts.size() == 2) {
                sum += symbol.adjacentParts.get(0).value * symbol.adjacentParts.get(1).value;
            }
        }
        return sum;
    }

    private static void parseMatrix(char[][] matrix, ArrayList<Part> parts, ArrayList<Symbol> symbols) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < cols; j++) {
                if (Character.isDigit(matrix[i][j])) {  // we have a number
                    int x_min = j;
                    int num = Character.getNumericValue(matrix[i][j]);
                    while (j + 1 < cols && Character.isDigit(matrix[i][j + 1])) {
                        j++;
                        num = num * 10 + Character.getNumericValue(matrix[i][j]);
                    }
                    parts.add(new Part(num, x_min, j, i));
                } else if (matrix[i][j] != '.') {  // we have a symbol (non '.')
                    symbols.add(new Symbol(matrix[i][j], j, i));
                }
            }
        }
        for (Symbol symbol : symbols) {  // update adjacency lists
            for (Part part : parts) {
                if (part.y - 1 <= symbol.y && part.y + 1 >= symbol.y &&
                    part.x_min - 1 <= symbol.x && part.x_max + 1 >= symbol.x) {
                    symbol.adjacentParts.add(part);
                    part.adjacentSymbols.add(symbol);
                }
            }
        }
    }

    private static class Part {
        private final int value;
        private final int x_min;
        private final int x_max;
        private final int y;
        private final ArrayList<Symbol> adjacentSymbols;
        public Part(int value, int x_min, int x_max, int y) {
            this.value = value;
            this.x_min = x_min;
            this.x_max = x_max;
            this.y = y;
            adjacentSymbols = new ArrayList<>();
        }
    }

    private static class Symbol {
        private final char symbol;
        private final int x;
        private final int y;
        private final ArrayList<Part> adjacentParts;
        public Symbol(char symbol, int x, int y) {
            this.symbol = symbol;
            this.x = x;
            this.y = y;
            adjacentParts = new ArrayList<>();
        }
    }
}