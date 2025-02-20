package io.github.backgammon.util;

public class intPair {
    private final int first;
    private final int second;

    public intPair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof intPair) {
            intPair other = (intPair) obj;
            return first == other.first && second == other.second;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
