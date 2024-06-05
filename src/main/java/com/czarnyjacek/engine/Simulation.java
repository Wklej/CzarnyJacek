package com.czarnyjacek.engine;

import com.czarnyjacek.objects.Card;
import com.czarnyjacek.objects.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Simulation {
    public static void run(int numberOfGames,
                           List<Predicate<List<Card>>> drawStrategies,
                           List<Predicate<List<Card>>> splitStrategies) {
        var executorService = Executors.newFixedThreadPool(drawStrategies.size());
        List<Result> tempResults = Collections.synchronizedList(new ArrayList<>());

        IntStream.range(0, drawStrategies.size()).forEach(i ->
                executorService.submit(() ->
                        IntStream.range(0, numberOfGames)
                                .forEach(x -> {
                                    var game = new Game(drawStrategies.get(i), splitStrategies.get(i));
                                    //TODO: send Result of simulation to kafka topic/DB
                                     tempResults.addAll(game.play());
                                })));

        executorService.shutdown();
    }
}
