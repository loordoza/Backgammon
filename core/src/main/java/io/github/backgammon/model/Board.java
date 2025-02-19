package io.github.backgammon.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int NUM_POINTS = 24;

    private List<Piece>[] points;
    private List<Piece>[] bars;

    public Board() {
        points = new ArrayList[NUM_POINTS];
        for (int i = 0; i < NUM_POINTS; i++) {
            points[i] = new ArrayList<>();
        }

        bars = new ArrayList[2];
        bars[Player.WHITE.getId()] = new ArrayList<>();
        bars[Player.BLACK.getId()] = new ArrayList<>();

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
        if (points[to].size() == 1 && points[to].getLast().getOwner() != points[from].getLast().getOwner()) {
            movePieceToBar(to);
        }
        Piece piece = points[from].remove(points[from].size() - 1);
        points[to].add(piece);
    }

    public void movePieceToBar(int point) {
        Piece piece = points[point].getLast();
        points[point].remove(piece);
        int color = piece.getOwner().getId();
        bars[color].add(piece);
    }

    public void movePieceFromBar(int point, Player player) {
        int color = player.getId();
        Piece piece = bars[color].getLast();
        bars[color].remove(piece);
        points[point].add(piece);
    }

    public void bearOffPiece(int point) {
        points[point].remove(points[point].getLast());
    }

    public boolean isBarEmpty(Player player) {
        int color = player.getId();
        return bars[color].isEmpty();
    }

    public List<Piece> getPieces(int point) {
        return points[point];
    }

    public boolean isExitBarLegal(int to, Player player) {
        return points[to].isEmpty() || points[to].size() == 1 || points[to].getLast().getOwner().equals(player);
    }

    public boolean isMoveLegal(int from, int to, Player player) {
        if(points[to].isEmpty()) {
            return true;
        }
        Player colorTo = points[to].getLast().getOwner();
        return player == colorTo || points[to].size() == 1;
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

    public void printBoard() {
        System.out.println(points[0].getLast().getOwner().getId());
        for (int i = 0; i < NUM_POINTS/2; i++) {
            System.out.print(points[i].size() + " ");
        }
        System.out.println();
        for(int i=NUM_POINTS-1; i>=NUM_POINTS/2; --i) {
            System.out.print(points[i].size() + " ");
        }
        System.out.println();
    }
}
