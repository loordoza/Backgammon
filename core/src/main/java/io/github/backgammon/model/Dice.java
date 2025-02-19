package io.github.backgammon.model;

import java.util.Random;

public class Dice {
    private int value;
    private boolean isUsed;

    public Dice() {
        value = new Random().nextInt(6) + 1;
        isUsed = false;
    }

    public int roll() {
        isUsed = false;
        Random rand = new Random();
        value = rand.nextInt(6) + 1;
        return value;
    }

    public void use() {
        isUsed = true;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public int getValue() {
        return value;
    }
}
