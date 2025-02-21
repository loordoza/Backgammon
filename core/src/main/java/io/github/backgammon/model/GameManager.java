package io.github.backgammon.model;

import io.github.backgammon.util.intPair;

import java.util.*;

public class GameManager {
    private Board board;
    private Dice dice1, dice2;
    private Dice extraDice1, extraDice2;

    Player currentPlayer;

    public GameManager() {
        board = new Board();
        dice1 = new Dice();
        dice2 = new Dice();
        extraDice1 = new Dice();
        extraDice2 = new Dice();

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
        if (dice1.getValue() == dice2.getValue()) {
            extraDice1.setValue(dice1.getValue());
            extraDice2.setValue(dice2.getValue());
        }
    }

    public List<Integer> getDiceValues() {
        if (areExtraDices()) {
            return Arrays.asList(dice1.getValue(), dice2.getValue(), dice1.getValue(), dice2.getValue());
        }
        return Arrays.asList(dice1.getValue(), dice2.getValue());
    }

    public List<Boolean> getUsedDices() {
        List<Boolean> list = new ArrayList<>();
        list.add(dice1.isUsed());
        list.add(dice2.isUsed());
        if (areExtraDices()) {
            list.add(extraDice1.isUsed());
            list.add(extraDice2.isUsed());
        }
        return list;
    }

    public boolean areExtraDices() {
        return dice1.getValue() == dice2.getValue();
    }

    public List<Integer> getMovablePieces() {
        Set<Integer> whoCanMove = new HashSet<>();
        List<intPair> pairOfPossibleMoves = getPossibleMoves();

        for (intPair pair : pairOfPossibleMoves)
            whoCanMove.add(pair.getFirst());

        return new ArrayList<>(whoCanMove);
    }

    public List<Integer> getPossibleMovesForPiece(int from) {
        List<Integer> whereCanMove = new ArrayList<>();
        List<intPair> pairOfPossibleMoves = getPossibleMoves();

        for(intPair pair : pairOfPossibleMoves)
            if (pair.getFirst() == from)
                whereCanMove.add(pair.getSecond());

        return whereCanMove;
    }

    public List<intPair> getPossibleMoves() {
        List<intPair> possibleMoves = new ArrayList<>();
        int direction = (currentPlayer == Player.WHITE) ? 1 : -1;
        int start = (currentPlayer == Player.WHITE) ? 0 : 23;

        if (board.allPiecesInHomeArea(currentPlayer)) {
            for (Dice dice : new Dice[]{dice1, dice2, extraDice1, extraDice2}) {
                if (!dice.isUsed()) {
                    for (int i = start; i >= 0 && i < 24; i += direction) {
                        List<Piece> pieces = board.getPieces(i);
                        if (!pieces.isEmpty() && pieces.getLast().getOwner().equals(currentPlayer)) {
                            int target = i + (direction * dice.getValue());
                            if (target >= 24) {
                                possibleMoves.add(new intPair(i, 999));
                            }
                            if (target < 0) {
                                possibleMoves.add(new intPair(i, -999));
                            }
                        }
                    }
                }
            }
        }

        if (!board.isBarEmpty(currentPlayer)) {
            for (Dice dice : new Dice[]{dice1, dice2, extraDice1, extraDice2}) {
                if (!dice.isUsed()) {
                    int target = (currentPlayer == Player.WHITE) ? dice.getValue() - 1 : 24 - dice.getValue();
                    if (board.isExitBarLegal(target, currentPlayer)) {
                        possibleMoves.add(new intPair(-1, target));
                    }
                }
            }
        } else {
            for (Dice dice : new Dice[]{dice1, dice2, extraDice1, extraDice2}) {
                if (!dice.isUsed()) {
                    for (int i = start; i >= 0 && i < 24; i += direction) {
                        List<Piece> pieces = board.getPieces(i);
                        if (!pieces.isEmpty() && pieces.getLast().getOwner().equals(currentPlayer)) {
                            int target = i + (direction * dice.getValue());
                            if (target >= 0 && target < 24 && board.isMoveLegal(i, target, currentPlayer)) {
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
        for(Dice dice : new Dice[]{dice1, dice2, extraDice1, extraDice2}) {
            if(!dice.isUsed() && dice.getValue() == Math.abs(from-to)) {
                dice.use();
                break;
            }
        }
    }

    public void makeMoveFromBar(int to) {
        board.movePieceFromBar(to, currentPlayer);
        for(Dice dice : new Dice[]{dice1, dice2, extraDice1, extraDice2}) {
            if(currentPlayer == Player.WHITE && !dice.isUsed() && dice.getValue() == to+1
                || currentPlayer == Player.BLACK && !dice.isUsed() && dice.getValue() == 24-to) {
                dice.use();
                break;
            }
        }
    }

    public void bearOffPiece(int from) {
        board.bearOffPiece(from, currentPlayer);
        int to = (currentPlayer == Player.WHITE) ? 24 : 0;
        for(Dice dice : new Dice[]{dice1, dice2, extraDice1, extraDice2}) {
            if(!dice.isUsed() && dice.getValue() >= Math.abs(from-to)) {
                System.out.println(from + " " + to + " " + dice.getValue());
                dice.use();
                break;
            }
        }
    }

    public boolean arePiecesInBar() {
        return !board.isBarEmpty(currentPlayer);
    }

    public int howManyInHouse(Player player) {
        return board.howManyInHouse(player);
    }

    public void nextTurn() {
        if (getPossibleMoves().isEmpty()) {
            currentPlayer = (currentPlayer == Player.WHITE) ? Player.BLACK : Player.WHITE;
            rollTwoDices();
        }
    }

    public boolean allPiecesInHomeArea() {
        return board.allPiecesInHomeArea(currentPlayer);
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
