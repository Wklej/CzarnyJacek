package com.czarnyjacek.objects;

import com.czarnyjacek.objects.enums.RANK;
import com.czarnyjacek.objects.enums.SUIT;

public record Card(SUIT suit, RANK rank, int value) {
    @Override
    public String toString() {
        return rank.toString();
    }
}
