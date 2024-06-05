package com.czarnyjacek.engine;

import com.czarnyjacek.objects.Card;
import com.czarnyjacek.objects.Result;

import java.util.*;
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
        Map<Integer, List<Result>> results = Collections.synchronizedMap(new HashMap<>());

        IntStream.range(0, drawStrategies.size()).forEach(i ->
                executorService.submit(() ->
                        IntStream.range(0, numberOfGames)
                                .forEach(x -> {
                                    var game = new Game(drawStrategies.get(i), splitStrategies.get(i));
                                    var gameResult = game.play();
                                    //TODO: send Result of simulation to kafka topic/DB
                                     tempResults.addAll(gameResult);
                                     results.computeIfAbsent(i, k -> new ArrayList<>()).addAll(gameResult);
                                })));


        executorService.shutdown();
        try {
            // Wait for all tasks to finish
            if (executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                results.values().forEach(res -> System.out.println(
                        res.stream()
                                .filter(result -> result.winner() == "PLAYER")
                                .count())
                );
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
