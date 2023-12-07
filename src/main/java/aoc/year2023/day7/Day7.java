package aoc.year2023.day7;

import aoc.utils.Utils;
import lombok.Getter;

import java.util.*;

public class Day7 {
    public static void main(String[] args) {
        List<String> inputList = Utils.readAllLinesFromResourceAsStream(2023, "input7.txt");

        List<Hand> hands = inputList.stream().map(Hand::createHand).toList();

        List<Hand> sortableHands = new ArrayList<>(hands);
        Collections.sort(sortableHands);

        sortableHands.forEach(System.out::println);

        var totalWinning = 0;
        for (int i = 0; i < sortableHands.size(); i++) {
            totalWinning += sortableHands.get(i).getBid() * (i + 1);
        }

        System.out.println("====");
        System.out.println("Answer1 : " + totalWinning);
        System.out.println("====");

        sortableHands.forEach(Hand::recalc);
        Collections.sort(sortableHands);

        sortableHands.forEach(System.out::println);

        totalWinning = 0;
        for (int i = 0; i < sortableHands.size(); i++) {
            totalWinning += sortableHands.get(i).getBid() * (i + 1);
        }

        System.out.println("====");
        System.out.println("Answer2 : " + totalWinning);
    }

    public static final class Hand implements Comparable<Hand> {

        private static final String CARD_ORDER = "23456789TJQKA";
        private static final String CARD_ORDER_JOKER = "J23456789TQKA";
        @Getter
        private final String cards;
        @Getter
        private final int bid;
        @Getter
        private HandType type;
        private boolean handleJokers;

        public static Hand createHand(String input) {
            String[] parts = input.split(" ");
            return new Hand(parts[0], parts[1]);
        }

        public Hand(String cards, String bid) {
            this.cards = cards;
            this.bid = Integer.parseInt(bid);
            type = calcType();
        }

        public void recalc() {
            this.handleJokers = true;
            this.type = calcType();
        }

        private HandType calcType() {
            char[] chars = this.cards.toCharArray();
            Arrays.sort(chars);
            String sorted = new String(chars);

            long numJokers = 0;
            if (handleJokers) {
                numJokers = sorted.chars().filter(ch -> ch == 'J').count();
            }

            String[] sc = sorted.split("");
            if (sc[0].equals(sc[1]) && sc[0].equals(sc[2]) && sc[0].equals(sc[3]) && sc[0].equals(sc[4])) {
                return HandType.FIVE_OF_A_KIND;
            } else if (
                    sc[0].equals(sc[1]) && sc[0].equals(sc[2]) && sc[0].equals(sc[3]) ||
                            sc[1].equals(sc[2]) && sc[1].equals(sc[3]) && sc[1].equals(sc[4])
            ) {
                if (numJokers == 1 || numJokers == 4) {
                    return HandType.FIVE_OF_A_KIND;
                }
                return HandType.FOUR_OF_A_KIND;
            } else if (
                    (sc[0].equals(sc[1]) && sc[0].equals(sc[2]) &&  sc[3].equals(sc[4])) ||
                            (sc[0].equals(sc[1]) && sc[2].equals(sc[3]) &&  sc[2].equals(sc[4]))
            ) {
                if (numJokers == 1) {
                    return HandType.FOUR_OF_A_KIND;
                } else if (numJokers == 2) {
                    return HandType.FIVE_OF_A_KIND;
                } else if (numJokers == 3) {
                    return  HandType.FIVE_OF_A_KIND;
                }
                return HandType.FULL_HOUSE;
            } else if (
                    (sc[0].equals(sc[1]) && sc[0].equals(sc[2])) ||
                            (sc[1].equals(sc[2]) && sc[1].equals(sc[3])) ||
                            (sc[2].equals(sc[3]) && sc[2].equals(sc[4]))
            ) {
                if (numJokers == 1) {
                    return HandType.FOUR_OF_A_KIND;
                } else if (numJokers == 2) {
                    return HandType.FIVE_OF_A_KIND;
                } else if (numJokers == 3) {
                    return HandType.FOUR_OF_A_KIND;
                }
                return HandType.THREE_OF_A_KIND;
            } else if (
                    (sc[0].equals(sc[1]) && sc[2].equals(sc[3])) ||
                            (sc[0].equals(sc[1]) && sc[3].equals(sc[4])) ||
                            (sc[1].equals(sc[2]) && sc[3].equals(sc[4]))
            ) {
                if (numJokers == 1) {
                    return HandType.FULL_HOUSE;
                } else if (numJokers == 2) {
                    return HandType.FOUR_OF_A_KIND;
                }
                return HandType.TWO_PAIR;
            } else if (
                    sc[0].equals(sc[1]) || sc[1].equals(sc[2]) || sc[2].equals(sc[3]) || sc[3].equals(sc[4])
            ) {
                if (numJokers == 1 || numJokers == 2) {
                    return HandType.THREE_OF_A_KIND;
                }
                return HandType.ONE_PAIR;
            }
            if (numJokers == 1) {
                return HandType.ONE_PAIR;
            }
            return HandType.HIGH_CARD;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Hand) obj;
            return Objects.equals(this.cards, that.cards) &&
                    Objects.equals(this.bid, that.bid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cards, bid);
        }

        @Override
        public String toString() {
            return "Hand[" +
                    "cards=" + cards + ", " +
                    "bid=" + bid + "," +
                    "type=" + type.toString() +
                    "]";
        }

        @Override
        public int compareTo(Hand o) {
            int typeCompare = Integer.compare(this.getType().getTypeValue(), o.getType().getTypeValue());

            if (typeCompare != 0) {
                return typeCompare;
            }

            String[] c = this.getCards().split("");
            String[] oc = o.getCards().split("");
            for (int i = 0; i <= c.length; i++) {
                int myIndex = handleJokers ? CARD_ORDER_JOKER.indexOf(c[i]) : CARD_ORDER.indexOf(c[i]);
                int otherIndex = handleJokers ? CARD_ORDER_JOKER.indexOf(oc[i]) : CARD_ORDER.indexOf(oc[i]);

                int compare = Integer.compare(myIndex, otherIndex);
                if (compare != 0) {
                    return compare;
                }
            }
            return 0;
        }
    }

    @Getter
    public enum HandType {
        HIGH_CARD(1),
        ONE_PAIR(2),
        TWO_PAIR(3),
        THREE_OF_A_KIND(4),
        FULL_HOUSE(5),
        FOUR_OF_A_KIND(6),
        FIVE_OF_A_KIND(7);

        private final int typeValue;
        HandType(int typeValue) {
            this.typeValue = typeValue;
        }

    }
}
