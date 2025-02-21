package io.github.backgammon.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int NUM_POINTS = 24;

    private List<Piece>[] points;
    private List<Piece>[] bars;

    private int whiteHouse, blackHouse;

    public Board() {
        points = new ArrayList[NUM_POINTS];
        for (int i = 0; i < NUM_POINTS; i++) {
            points[i] = new ArrayList<>();
        }

        bars = new ArrayList[2];
        bars[Player.WHITE.getId()] = new ArrayList<>();
        bars[Player.BLACK.getId()] = new ArrayList<>();

        whiteHouse = blackHouse = 0;

        initPieces();
    }

    private void initPieces() {
        addPieces(0, Player.WHITE, 2);
        addPieces(5, Player.BLACK, 5);
        addPieces(7, Player.BLACK, 3);
        addPieces(11, Player.WHITE, 5);
        addPieces(12, Player.BLACK, 5);
        addPieces(16, Player.WHITE, 3);
        addPieces(18, Player.WHITE, 5);
        addPieces(23, Player.BLACK, 2);
    }

    private void addPieces(int point, Player owner, int howMany) {
        for (int i = 0; i < howMany; i++) {
            points[point].add(new Piece(owner));
        }
    }

    public void movePiece(int from, int to) {
        if (points[to].size() == 1 && points[to].get(points[to].size() - 1).getOwner() != points[from].get(points[from].size() - 1).getOwner()) {
            movePieceToBar(to);
        }
        Piece piece = points[from].remove(points[from].size() - 1);
        points[to].add(piece);
    }

    public void movePieceToBar(int point) {
        Piece piece = points[point].get(points[point].size() - 1);
        points[point].remove(piece);
        int color = piece.getOwner().getId();
        bars[color].add(piece);
    }

    public void movePieceFromBar(int to, Player player) {
        if (points[to].size() == 1 && points[to].get(points[to].size() - 1).getOwner() != player) {
            movePieceToBar(to);
        }
        int color = player.getId();
        Piece piece = bars[color].get(bars[color].size() - 1);
        bars[color].remove(piece);
        points[to].add(piece);
    }

    public void bearOffPiece(int point, Player player) {
        points[point].remove(points[point].size() - 1);
        if(player.getId() == Player.WHITE.getId())
            whiteHouse++;
        else
            blackHouse++;
    }

    public int howManyInHouse(Player player) {
        if (player.getId() == Player.WHITE.getId())
            return whiteHouse;
        return blackHouse;
    }

    public boolean isBarEmpty(Player player) {
        int color = player.getId();
        return bars[color].isEmpty();
    }

    public List<Piece> getBarPieces(Player player) {
        int color = player.getId();
        return bars[color];
    }

    public List<Piece> getPieces(int point) {
        return points[point];
    }

    public boolean isExitBarLegal(int to, Player player) {
        return points[to].isEmpty() || points[to].size() == 1 || points[to].get(points[to].size() - 1).getOwner().equals(player);
    }

    public boolean isMoveLegal(int from, int to, Player player) {
        if (points[to].isEmpty())
            return true;

        Player ownerOfTargetPoint = points[to].get(points[to].size() - 1).getOwner();
        if (ownerOfTargetPoint == player)
            return true;

        if (points[to].size() == 1)
            return true;

        return false;
    }

    public boolean allPiecesInHomeArea(Player player) {
        if (player == Player.WHITE) {
            if (!bars[Player.WHITE.getId()].isEmpty())
                return false;
            for (int i = 0; i < NUM_POINTS-6; i++) {
                if (!points[i].isEmpty() && points[i].get(points[i].size() - 1).getOwner() == player)
                    return false;
            }
        } else {
            if (!bars[Player.BLACK.getId()].isEmpty())
                return false;
            for (int i = 6; i < NUM_POINTS; i++) {
                if (!points[i].isEmpty() && points[i].get(points[i].size() - 1).getOwner() == player)
                    return false;
            }
        }
        return true;
    }

    public boolean hasPieces(Player player) {
        for (int i = 0; i < NUM_POINTS; i++) {
            for (Piece piece : points[i]) {
                if (piece.getOwner() == player) {
                    return true;
                }
            }
        }
        return false;
    }
}
