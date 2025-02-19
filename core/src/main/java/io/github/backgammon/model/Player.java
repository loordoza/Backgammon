package io.github.backgammon.model;

public enum Player {
    WHITE(0),
    BLACK(1);

    private final int id;
    Player(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
