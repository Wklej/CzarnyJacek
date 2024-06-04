package com.czarnyjacek;

import com.czarnyjacek.objects.engine.Strategies;

import java.util.List;

public class MainGame {
    public static void main(String[] args) {
        Simulation.run(2, List.of(Strategies::drawUntil17, Strategies::drawUntil18));
    }
}
