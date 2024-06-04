package com.czarnyjacek.objects;

import com.czarnyjacek.engine.Round;

import java.util.List;

public class Strategies {

    public static boolean drawUntil17(List<Card> hand) {
        return Round.calculateHand(hand) < 17;
    }

    public static boolean drawUntil18(List<Card> hand) {
        return Round.calculateHand(hand) < 18;
    }


}
