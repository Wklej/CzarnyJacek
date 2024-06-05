package com.czarnyjacek.engine;

import com.czarnyjacek.objects.Card;
import com.czarnyjacek.objects.Result;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class Game {
    //TODO: game should hold deck?

    private final Round primaryRound;
    private final Predicate<List<Card>> drawStrategy;
    private final Predicate<List<Card>> splitStrategy;

    public Game(Predicate<List<Card>> drawStrategy, Predicate<List<Card>> splitStrategy) {
        primaryRound = new Round();
        this.drawStrategy = drawStrategy;
        this.splitStrategy = splitStrategy;
    }

    public List<Result> play() {
        List<Result> results = new ArrayList<>();

        //TODO: add split strategy
        if (isSplitAvailable(primaryRound.getPlayerHand())) {
            var splitCard = primaryRound.getPlayerHand().removeLast();
            var splitRound = new Round(primaryRound.getDealerHand(), splitCard);

            results.add(splitRound.play(drawStrategy));
        }

        results.add(primaryRound.play(drawStrategy));

        return results;
    }

    private boolean isSplitAvailable(List<Card> hand) {
        return splitStrategy.test(hand);
    }
}
