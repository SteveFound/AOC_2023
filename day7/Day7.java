package day7;

import util.Day;
import util.FileLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Part 2 gives incorrect result.  Don't know why !!!
 */
public class Day7 implements Day {

    private class Hand implements Comparable {
        enum HandType { HIGH_CARD, PAIR, TWO_PAIR, THREE, FULL_HOUSE, FOUR, FIVE }
        private final HandType handType;
        private final String hand;
        private final int bet;
        private final boolean joker;

        /**
         * Create a hand
         * @param hand String representing the 5 cards in the hand
         * @param bet bet amount
         * @param joker if true, J cards are to be treated as Jokers, Jacks if false
         */
        public Hand(String hand, int bet, boolean joker) {
            this.hand = hand;
            this.bet = bet;
            this.joker = joker;
            this.handType = analyse(hand);
        }

        /**
         * Calculate the hand type for this hand
         * @param hand card faces as characters in a String
         * @return the hand type
         */
        private HandType analyse(String hand) {
            // Valid faces
            String faces = joker ? "J23456789TQKA" : "23456789TJQKA";
            int[] faceCount = new int[13];
            int jokers = 0;
            char[] cards = hand.toCharArray();

            // Count the number of each card we have
            for (Character card : cards) {
                int face = faces.indexOf(card);
                faceCount[face]++;
            }
            if( joker ) {
                jokers = faceCount[0];
                faceCount[0] = 0;
            }

            int total = 0;
            // search for fives
            for (int count : faceCount) {
                if (count + jokers == 5) return HandType.FIVE;
            }
            // search for fours
            for (int count : faceCount) {
                if (count + jokers == 4) return HandType.FOUR;
            }
            // search for the rest
            for (int count : faceCount) {
                // three of a kind (or full house) ignoring jokers
                if (count == 3) total += 3;
                // a pair... ignoring jokers
                if (count == 2) total += 2;
            }
            // If no pairs or threes found. Then it's a High card
            if( total == 0 ) total = 1;
            // Add the jokers to the total cards used
            total += jokers;

            // We have a three and a pair or two pairs and a joker
            if (total == 5) return HandType.FULL_HOUSE;
            // We have a three, a pair and joker or a high card and two jokers
            if (total == 3) return HandType.THREE;
            // We have two pairs we can't have any jokers or it would have made a full house
            if (total == 4) return HandType.TWO_PAIR;
            // We have a pair or a high card and a joker
            if (total == 2) return HandType.PAIR;
            // We have a high card and no jokers
            return HandType.HIGH_CARD;
        };

        /**
         * Get the bet value for this hand
         * @return bet value
         */
        public int getBet() {
            return bet;
        }

        /**
         * Card face comparator for sorting
         * @param o the object to be compared.
         * @return -1, 0 or 1 depending if faces are < = > the compared hand
         */
        @Override
        public int compareTo(Object o) {
            String faces = joker ? "J23456789TQKA" : "23456789TJQKA";
            Hand other = (Hand)o;
            int hType = handType.compareTo(other.handType);
            if( hType != 0) return hType;
            char[] thisHand = hand.toCharArray();
            char[] otherHand = other.hand.toCharArray();
            for( int idx = 0; idx < 5; idx++) {
                int thisCard = faces.indexOf(thisHand[idx]);
                int otherCard = faces.indexOf(otherHand[idx]);
                if( thisCard == otherCard ) continue;
                return thisCard > otherCard ? 1 : -1;
            }
            return 0;
        }

        @Override
        public String toString() {
            return '[' + hand + " (" + bet + ") " + handType + ']';
        }
    }

    private long part1(List<String> fileContent) {
        List<Hand> hands = new ArrayList<>();
        for( String line : fileContent ){
            String[] parts = line.split("\\s+");
            hands.add( new Hand(parts[0], Integer.parseInt(parts[1]),false));
        }
        Collections.sort(hands);
//        hands.forEach(System.out::println);
        int idx = 1;
        long sum = 0;
        for( Hand hand : hands ) {
            sum += (long) hand.getBet() * idx;
            idx++;
        }
        return sum;
    }

    private long part2(List<String> fileContent) {
        List<Hand> hands = new ArrayList<>();
        for( String line : fileContent ){
            String[] parts = line.split("\\s+");
            hands.add( new Hand(parts[0], Integer.parseInt(parts[1]),true));
        }
        Collections.sort(hands);
//        hands.forEach(System.out::println);
        int idx = 1;
        long sum = 0;
        for( Hand hand : hands ) {
            sum += (long) hand.getBet() * idx;
            idx++;
        }
        return sum;
    }


    @Override
    public void run() {
        List<String> fileContent = FileLoader.readFile("input/day7.txt");
        System.out.println("Day 7");
        System.out.println("Part 1: " + part1(fileContent));
        System.out.println("Part 2: " + part2(fileContent));
    }
}
