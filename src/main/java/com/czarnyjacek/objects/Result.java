package com.czarnyjacek.objects;

import java.util.List;

public record Result(String winner, int playerScore, int dealerScore, List<Card> playerHand, List<Card> dealerHand) {}
