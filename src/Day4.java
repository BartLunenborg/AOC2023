import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day4 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/4.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        ArrayList<Card> cards = new ArrayList<>();
        while (scanner.hasNext()) {
            String[] split = scanner.nextLine().split(":")[1].split(" \\| ");
            Card card = new Card(convertStringToList(split[0]), convertStringToList(split[1]));
            cards.add(card);
        }
        scanner.close();

        System.out.printf("Part one: %d\n", partOne(cards));
        System.out.printf("Part two: %d\n", partTwo(cards));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static int partOne(ArrayList<Card> cards) {
        int sum = 0;
        for (Card card : cards) {
            int matches = findMatches(card);
            if (matches > 0) {
                sum += (int) Math.pow(2, matches - 1);
            }
        }
        return sum;
    }

    private static int partTwo(ArrayList<Card> cards) {
        int sum = 0;
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            sum += card.multiplier;
            int matches = findMatches(card);
            for (int j = 1; j <= matches; j++) {
                cards.get(i + j).multiplier += card.multiplier;
            }
        }
        return sum;
    }

    private static int findMatches(Card card) {
        int matches = 0;
        for (Integer num : card.got) {
            matches += card.winning.contains(num) ? 1 : 0;
        }
        return matches;
    }

    private static ArrayList<Integer> convertStringToList(String input) {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (String number : input.split(" ")) {
            if (!number.isEmpty()) {
                numbers.add(Integer.parseInt(number));
            }
        }
        return numbers;
    }

    private static class Card {
        private final ArrayList<Integer> winning;
        private final ArrayList<Integer> got;
        private int multiplier;
        public Card(ArrayList<Integer> winning, ArrayList<Integer> got) {
            this.winning = winning;
            this.got = got;
            this.multiplier = 1;
        }
    }
}