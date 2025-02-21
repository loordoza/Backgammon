package io.github.backgammon.ai;

import io.github.backgammon.model.GameManager;

public abstract class AIPlayer {
    protected final GameManager gameManager;

    public AIPlayer(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public abstract void makeMove();
}
