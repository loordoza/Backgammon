package io.github.backgammon.model;

import io.github.backgammon.util.intPair;

import java.util.*;

public class GameManager {
    private Board board;
    private Dice dice1, dice2;

    Player currentPlayer;

    public GameManager() {
        board = new Board();
        dice1 = new Dice();
        dice2 = new Dice();

        currentPlayer = whoStart();
    }

    private Player whoStart() {
        int v1, v2;
        do {
            v1 = dice1.roll();
            v2 = dice2.roll();
        } while (v1 == v2);
        if (v1 > v2) {
            return Player.WHITE;
        } else {
            return Player.BLACK;
        }
    }

    public void rollDice() {
        if (currentPlayer == Player.WHITE) {
            dice1.roll();
        } else {
            dice2.roll();
        }
    }

    public void rollTwoDices() {
        dice1.roll();
        dice2.roll();
    }

    public List<Integer> getDiceValues() {
        if (dice1.getValue() == dice2.getValue()) {
            return Arrays.asList(dice1.getValue(), dice2.getValue(), dice1.getValue(), dice2.getValue());
        }
        return Arrays.asList(dice1.getValue(), dice2.getValue());
    }

    public List<Integer> getMovablePieces() {
        Set<Integer> whoCanMove = new HashSet<>();
        List<intPair> pairOfPossibleMoves = getPossibleMoves();

        for (intPair pair : pairOfPossibleMoves)
            whoCanMove.add(pair.getFirst());

        return new ArrayList<>(whoCanMove);
    }

    public List<intPair> getPossibleMoves() {
        List<intPair> possibleMoves = new ArrayList<>();
        int direction = (currentPlayer == Player.WHITE) ? 1 : -1;
        int start = (currentPlayer == Player.WHITE) ? 0 : 23;

        if (!board.isBarEmpty(currentPlayer)) {
            for (Dice dice : new Dice[]{dice1, dice2}) {
                if (!dice.isUsed()) {
                    int target = (currentPlayer == Player.WHITE) ? dice.getValue() - 1 : 24 - dice.getValue();
                    if (board.isExitBarLegal(target, currentPlayer)) {
                        possibleMoves.add(new intPair(-1, target));
                    }
                }
            }
        } else {
            for (Dice dice : new Dice[]{dice1, dice2}) {
                if (!dice.isUsed()) {
                    for (int i = start; i >= 0 && i < 24; i += direction) {
                        List<Piece> pieces = board.getPieces(i);
                        if (!pieces.isEmpty() && pieces.getLast().getOwner().equals(currentPlayer)) {
                            int target = i + (direction * dice.getValue());
                            if (target >= 0 && target < 24 && board.isMoveLegal(i,currentPlayer)) {
                                possibleMoves.add(new intPair(i, target));
                            }
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }

    public void makeMove(int from, int to) {
        board.movePiece(from, to);
        for(Dice dice : new Dice[]{dice1, dice2}) {
            if(!dice.isUsed() && dice.getValue() == Math.abs(from-to)) {
                dice.use();
                break;
            }
        }
    }

    public void nextTurn() {
        if (getPossibleMoves().isEmpty()) {
            currentPlayer = (currentPlayer == Player.WHITE) ? Player.BLACK : Player.WHITE;
            rollTwoDices();
        }
    }

    public boolean hasWon(Player player) {
        return board.isBarEmpty(player) && !board.hasPieces(player);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }
}
