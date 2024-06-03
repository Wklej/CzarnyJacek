package com.czarnyjacek;

import com.czarnyjacek.objects.engine.Game;
import com.czarnyjacek.objects.engine.Strategies;

public class MainGame {
    public static void main(String[] args) {
        new Game(Strategies::drawUntil18);
        new Game(Strategies::drawUntil17);
    }
}
