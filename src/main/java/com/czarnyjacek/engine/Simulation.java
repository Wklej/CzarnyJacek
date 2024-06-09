package com.czarnyjacek.engine;

import com.czarnyjacek.objects.Bet;
import com.czarnyjacek.objects.Card;
import com.czarnyjacek.objects.Result;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.IntStream;

@AllArgsConstructor
public class Simulation {
    private final int numberOfGames;
    private final List<Predicate<List<Card>>> drawStrategies;
    private final List<Predicate<List<Card>>> splitStrategies;
    @Setter
    private double money;

    public void run() {
        var executorService = Executors.newFixedThreadPool(drawStrategies.size());
        List<Result> tempResults = Collections.synchronizedList(new ArrayList<>());
        Map<Integer, List<Result>> results = Collections.synchronizedMap(new HashMap<>());

        IntStream.range(0, drawStrategies.size()).forEach(i ->
                executorService.submit(() ->
                        IntStream.range(0, numberOfGames)
                                .forEach(x -> {
                                    var game = new Game(drawStrategies.get(i), splitStrategies.get(i));
                                    var gameResult = game.play();
                                    setMoney(updateMoney(gameResult, money, Bet.MIN));
                                    //TODO: send Result of simulation to kafka topic/DB
                                     tempResults.addAll(gameResult);
                                     results.computeIfAbsent(i, k -> new ArrayList<>()).addAll(gameResult);
                                })));


        executorService.shutdown();
        try {
            // Wait for all tasks to finish
            if (executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                results.values().forEach(res -> System.out.println("wins: " +
                        res.stream()
                                .filter(result -> result.winner() == "PLAYER")
                                .count())
                );
                System.out.println("Money: " + money);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private double updateMoney(List<Result> results, double money, double bet) {
        return results.stream()
                .mapToDouble(result -> switch (result) {
                    case Result res when res.winner() == "PLAYER" && Round.isBlackJack(result.playerHand()) -> Bet.blackJack(bet);
                    case Result res when res.winner() == "PLAYER" -> bet;
                    case Result res when res.winner() == "DEALER" -> -bet;
                    default -> 0.0;
                })
                .sum() + money;
    }

//        var updatedMoney = money;
//        for (var result : results) {
//            updatedMoney += switch (result.winner()) {
//                case "PLAYER" -> Bet.MIN;
//                case "DEALER" -> -Bet.MIN;
//                default -> 0.0;
//            };
//        }
//        return updatedMoney;

}
