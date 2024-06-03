package com.czarnyjacek.objects.engine;

import com.czarnyjacek.objects.Card;

import java.util.List;

public class Strategies {

    public static boolean drawUntil17(List<Card> hand) {
        return Game.calculateHand(hand) < 17;
    }

    public static boolean drawUntil18(List<Card> hand) {
        return Game.calculateHand(hand) < 18;
    }


}
