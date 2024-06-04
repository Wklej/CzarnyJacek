package com.czarnyjacek.engine;

import com.czarnyjacek.objects.Card;
import com.czarnyjacek.objects.Deck;
import com.czarnyjacek.objects.Result;
import com.czarnyjacek.objects.enums.RANK;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class Round {
    private static Deck deck;
    @Getter
    private List<Card> playerHand;
    @Getter
    private List<Card> dealerHand;
    private int playerScore;
    private int dealerScore;

    public Round() {
        deck = new Deck();
        playerHand = new ArrayList<>(){{addAll(List.of(deck.dealCard(), deck.dealCard()));}};
        dealerHand = new ArrayList<>(){{addAll(List.of(deck.dealCard(), deck.dealCard()));}};
    }

    public Round(List<Card> dealerHand, Card splitCard) {
        this();
        this.dealerHand = dealerHand;
        this.playerHand = new ArrayList<>(){{add(splitCard);}};
    }


    public Result play(Predicate<List<Card>> strategy) {
        this.playerScore = calculateHand(playerHand);
        this.dealerScore = calculateHand(dealerHand);
        playerInfo();
        dealerInfo();
        while (strategy.test(playerHand) || isBlackJack(playerHand)) {
            playerDraw();
        }

        if (!isBust(playerScore)) {
            dealerDraw();
            callWinner(checkWinner());
        } else {
            callBust();
            return new Result("DEALER", playerScore, dealerScore, playerHand, dealerHand);
        }

        playerInfo();
        dealerInfo();

        return new Result(checkWinner(), playerScore, dealerScore, playerHand, dealerHand);
    }

    private String checkWinner() {
        if (isBust(dealerScore)) return "PLAYER";
        else if (playerScore == dealerScore) return "DRAW";
        else if (playerScore > dealerScore) return "PLAYER";
        else return "DEALER";
    }

    private void callWinner(String winner) {
        log.info("PLAYER score: " + playerScore + " DEALER score: " + dealerScore);
        log.info("The winner is: " + winner);
    }

    private void callBust() {
        log.info("The winner is: DEALER - PLAYER BUSTED with score: " + playerScore);
    }

    private void playerDraw() {
        playerHand.add(deck.dealCard());
        playerScore = calculateHand(playerHand);
    }

    private void dealerDraw() {
        while (dealerScore <= 17 || isBlackJack(dealerHand)) {
            dealerHand.add(deck.dealCard());
            dealerScore = calculateHand(dealerHand);
        }
    }

    public static Integer calculateHand(List<Card> hand) {
        int sum = hand.stream()
                .map(Card::value)
                .reduce(0, Integer::sum);

        long aceCount = hand.stream()
                .filter(card -> card.rank() == RANK.ACE)
                .count();

        while (sum > 21 && aceCount > 0) {
            sum -= 10;
            aceCount--;
        }

        return sum;
    }

    private void playerInfo() {
        log.info("player: " + playerHand);
    }

    private void dealerInfo() {
        log.info("dealer: " + dealerHand);
    }

    private boolean isBust(int playerScore) {
        return playerScore > 21;
    }

    private boolean isBlackJack(List<Card> hand) {
        return hand.size() == 2 && calculateHand(hand) == 21;
    }
}
