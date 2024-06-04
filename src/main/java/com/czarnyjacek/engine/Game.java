package com.czarnyjacek.engine;

import com.czarnyjacek.objects.Card;
import com.czarnyjacek.objects.Deck;
import com.czarnyjacek.objects.Result;
import com.czarnyjacek.objects.enums.RANK;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class Game {
    //TODO: game should hold deck?

    private final Round primaryRound;
    private final Predicate<List<Card>> strategy;

    public Game(Predicate<List<Card>> strategy) {
        primaryRound = new Round();
        this.strategy = strategy;
    }

    public List<Result> play() {
        List<Result> results = new ArrayList<>();

        //TODO: add split strategy
        if (isSplitAvailable(primaryRound.getPlayerHand())) {
            System.out.println("SPLITTING.....");
            var splitCard = primaryRound.getPlayerHand().removeLast();
            var splitRound = new Round(primaryRound.getDealerHand(), splitCard);

            results.add(splitRound.play(strategy));
        }

        results.add(primaryRound.play(strategy));

        return results;
    }

    private boolean isSplitAvailable(List<Card> hand) {
        return hand.getFirst().rank() == hand.getLast().rank();
    }
}
