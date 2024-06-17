package com.czarnyjacek.objects;

import com.czarnyjacek.engine.Round;
import com.czarnyjacek.objects.enums.RANK;

import java.util.List;

public class Strategies {

    public static boolean drawUntil17(List<Card> hand) {
        return Round.calculateHand(hand) < 17;
    }

    public static boolean drawUntil18(List<Card> hand) {
        return Round.calculateHand(hand) < 18;
    }

    public static boolean splitBetween4And6(List<Card> hand) {
        return hand.getFirst().value() >= 4 && hand.getFirst().value() <= 6;
    }

    public static boolean splitAceAnd8(List<Card> hand) {
        return hand.getFirst().rank() == RANK.ACE || hand.getFirst().rank() == RANK.EIGHT;
    }
}
