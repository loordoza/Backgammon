package io.github.backgammon.ai;

import io.github.backgammon.model.GameManager;
import io.github.backgammon.util.intPair;

import java.util.List;
import java.util.Random;

public class RandomPlayer extends AIPlayer {
    private final Random random;

    public RandomPlayer(GameManager gameManager) {
        super(gameManager);
        this.random = new Random();
    }

    @Override
    public void makeMove() {
        List<intPair> possibleMoves = gameManager.getPossibleMoves();

        if (!possibleMoves.isEmpty()) {
            intPair move = possibleMoves.get(random.nextInt(possibleMoves.size()));
            if (move.getFirst() == -1) {
                gameManager.makeMoveFromBar(move.getSecond());
            } else if (move.getSecond() == 999 || move.getSecond() == -999) {
                gameManager.bearOffPiece(move.getFirst());
            } else {
                gameManager.makeMove(move.getFirst(), move.getSecond());
            }
        }

        gameManager.nextTurn();
    }
}
