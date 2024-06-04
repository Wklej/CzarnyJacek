package com.czarnyjacek;

import com.czarnyjacek.objects.Card;
import com.czarnyjacek.objects.engine.Game;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Simulation {
    public static void run(int numberOfGames, List<Predicate<List<Card>>> strategies) {
        var executorService = Executors.newFixedThreadPool(strategies.size());

        IntStream.range(0, strategies.size()).forEach(i ->
                executorService.submit(() ->
                        IntStream.range(0, numberOfGames)
                                .forEach(x -> {
                                    var game = new Game();
                                    //TODO: send Result of simulation to kafka topic/DB
                                    game.simulation(strategies.get(i));
                                })));

        executorService.shutdown();
    }
}
