import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day7 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input/7.input");
        Scanner scanner = new Scanner(file);
        long startTime = System.currentTimeMillis();

        ArrayList<Hand> hands = new ArrayList<>();
        while (scanner.hasNext()) {
            String cards = scanner.next();
            int bid = scanner.nextInt();
            Hand hand = new Hand(cards, bid);
            hands.add(hand);
        }
        scanner.close();

        System.out.printf("Part one: %d\n", partOne(hands));
        System.out.printf("Part two: %d\n", partTwo(hands));
        System.out.printf("Execution time: %d ms\n", System.currentTimeMillis() - startTime);
    }

    private static int partOne(ArrayList<Hand> hands) {
        char[] order = {'A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2', '1'};
        for (Hand hand : hands) {
            findStrengthOne(hand);
        }
        return solve(hands, order);
    }

    private static int partTwo(ArrayList<Hand> hands) {
        char[] order = {'A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', '1', 'J'};
        for (Hand hand : hands) {
            findStrengthTwo(hand);
        }
        return solve(hands, order);
    }

    private static int solve(ArrayList<Hand> hands, char[] order) {
        hands.sort((hand1, hand2) -> {
            int strengthComparison = Integer.compare(hand1.strength, hand2.strength);
            if (strengthComparison != 0) {
                return strengthComparison;
            }
            return compareCards(hand1.cards, hand2.cards, order);
        });

        int sum = 0;
        int multiplier = 1;
        for (Hand hand : hands) {
            sum = sum + multiplier++ * hand.bid;
        }
        return sum;
    }

    private static int compareCards(String handOne, String handTwo, char[] order) {
        for (int i = 0; i < Math.min(handOne.length(), handTwo.length()); i++) {
            char cardOne = handOne.charAt(i);
            char cardTwo = handTwo.charAt(i);

            int indexOne = getIndex(cardOne, order);
            int indexTwo = getIndex(cardTwo, order);

            int comparison = Integer.compare(indexOne, indexTwo);
            if (comparison != 0) {
                return -comparison;
            }
        }

        return Integer.compare(handTwo.length(), handOne.length());
    }

    private static int getIndex(char card, char[] order) {
        for (int i = 0; i < order.length; i++) {
            if (order[i] == card) {
                return i;
            }
        }
        return -1;
    }

    private static void findStrengthOne(Hand hand) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char card : hand.cards.toCharArray()) {
            counts.put(card, counts.getOrDefault(card, 0) + 1);
        }

        if (counts.containsValue(5)) {
            hand.strength = 7;
        } else if (counts.containsValue(4)) {
            hand.strength = 6;
        } else if (counts.containsValue(3) && counts.containsValue(2)) {
            hand.strength = 5;
        } else if (counts.containsValue(3)) {
            hand.strength = 4;
        } else {
            long pairs = counts.values().stream().filter(count -> count == 2).count();
            if (pairs == 2) {
                hand.strength = 3;
            } else if (pairs == 1) {
                hand.strength = 2;
            } else {
                hand.strength = 1;
            }
        }
    }

    private static void findStrengthTwo(Hand hand) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char card : hand.cards.toCharArray()) {
            counts.put(card, counts.getOrDefault(card, 0) + 1);
        }

        int jokers = counts.getOrDefault('J', 0);
        for (char key : counts.keySet()) {
            if (key != 'J') {
                counts.replace(key, counts.get(key) + jokers);
            }
        }

        if (counts.containsValue(5)) {
            hand.strength = 7;
        } else if (counts.containsValue(4)) {
            hand.strength = 6;
        } else if ((counts.containsKey('J') && counts.containsValue(3) && counts.size() < 4) ||
            (!counts.containsKey('J') && counts.containsValue(3) && counts.containsValue(2))) {
            hand.strength = 5;
        } else if (counts.containsValue(3)) {
            hand.strength = 4;
        } else {
            long pairs = counts.values().stream().filter(count -> count == 2).count();
            if (pairs == 2) {
                hand.strength = 3;
            } else if (pairs == 1 || counts.containsValue(2)) {
                hand.strength = 2;
            } else {
                hand.strength = 1;
            }
        }
    }

    private static class Hand {
        private final String cards;
        private final int bid;
        private int strength;
        public Hand(String cards, int bid) {
            this.cards = cards;
            this.bid = bid;
        }
    }
}