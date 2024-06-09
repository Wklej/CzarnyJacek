package com.czarnyjacek;

import com.czarnyjacek.engine.Simulation;
import com.czarnyjacek.objects.Strategies;

import java.util.List;

public class MainGame {
    public static void main(String[] args) {
        var simulation = new Simulation(10,
                List.of(
//                    Strategies::drawUntil17,
                        Strategies::drawUntil18
                ),
                List.of(
//                        Strategies::splitAceAnd8,
                        Strategies::splitAceAnd8
                ),
                100
                );
        simulation.run();
    }
}
