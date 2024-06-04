package com.czarnyjacek.engine;

import com.czarnyjacek.objects.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Simulation {
    public static void run(int numberOfGames, List<Predicate<List<Card>>> strategies) {
        var executorService = Executors.newFixedThreadPool(strategies.size());
        var tempResults = new ArrayList<>(); //why not updating inside loop?

        IntStream.range(0, strategies.size()).forEach(i ->
                executorService.submit(() ->
                        IntStream.range(0, numberOfGames)
                                .forEach(x -> {
                                    var game = new Game(strategies.get(i));
                                    //TODO: send Result of simulation to kafka topic/DB
                                     tempResults.addAll(game.play());
                                })));

        executorService.shutdown();
    }
}
