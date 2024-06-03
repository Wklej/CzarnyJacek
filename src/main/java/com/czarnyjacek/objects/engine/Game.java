package com.czarnyjacek.objects.engine;

import com.czarnyjacek.objects.Card;
import com.czarnyjacek.objects.Deck;
import com.czarnyjacek.objects.enums.RANK;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Game {
    private final Deck deck;
    private final List<Card> playerHand;
    private final List<Card> dealerHand;
    private int playerScore;
    private int dealerScore;

    public Game() {
        deck = new Deck();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        playerScore = 0;
        dealerScore = 0;
        start();
    }

    private void start() {
        playerHand.addAll(List.of(deck.dealCard(), deck.dealCard()));
        dealerHand.addAll(List.of(deck.dealCard(), deck.dealCard()));
        playerScore = calculateHand(playerHand);
        dealerScore = calculateHand(dealerHand);
        dealerInfo();
        playerInfo();
    }

    public void simulation() {
        //one strategy - DRAW until >= 18
        while (playerScore <= 18 || isBlackJack(playerHand)) {
            playerDraw();
        }

        if (!isBust(playerScore)) {
            dealerDraw();
            callWinner(checkWinner());
        } else {
            callBust();
        }
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
        playerInfo();
    }

    private void dealerDraw() {
        while (dealerScore <= 17 || isBlackJack(dealerHand)) {
            dealerHand.add(deck.dealCard());
            dealerScore = calculateHand(dealerHand);
        }
        dealerInfo();
    }

    private Integer calculateHand(List<Card> hand) {
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
