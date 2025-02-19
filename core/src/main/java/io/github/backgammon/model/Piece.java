package io.github.backgammon.model;

public class Piece {
    private Player owner;
    boolean isCaptured;

    public Piece(Player owner) {
        this.owner = owner;
        isCaptured = false;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean isCaptured() {
        return isCaptured;
    }

    public void setCaptured(boolean captured) {
        isCaptured = captured;
    }
}
