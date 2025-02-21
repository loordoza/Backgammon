package io.github.backgammon.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import io.github.backgammon.model.GameManager;
import io.github.backgammon.model.Piece;
import io.github.backgammon.util.intPair;
import io.github.backgammon.views.GameScreen;

import java.util.List;

public class InputHandler extends InputAdapter {
    private final GameManager gameManager;
    private final GameScreen gameScreen;

    private int selectedPoint = -1;

    public InputHandler(GameManager gameManager, GameScreen gameScreen) {
        this.gameManager = gameManager;
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 touch = gameScreen.getStage().screenToStageCoordinates(new Vector2(screenX, screenY));

        gameManager.nextTurn();
        gameScreen.updatePieces();
        gameScreen.updateHighlights(selectedPoint);

        int clickedPoint = getClickedPoint(touch);
        boolean isHouseClicked = isHouseClicked(touch);

        if (gameManager.allPiecesInHomeArea() && selectedPoint != -1 && isHouseClicked) {
            gameManager.bearOffPiece(selectedPoint);
            gameScreen.updatePieces();
            selectedPoint = -1;
            gameScreen.updateHighlights(selectedPoint);
            return true;
        }

        if (clickedPoint != -1) {
            if (selectedPoint == -1) {
                if (gameManager.arePiecesInBar()) {
                    if (gameManager.getPossibleMoves().contains(new intPair(-1, clickedPoint))) {
                        gameManager.makeMoveFromBar(clickedPoint);
                        gameScreen.updatePieces();
                        gameScreen.updateHighlights(selectedPoint);
                    }
                } else if (isValidSelection(clickedPoint)) {
                    selectedPoint = clickedPoint;
                    gameScreen.updateHighlights(selectedPoint);
                }
            } else {
                if (gameManager.getPossibleMoves().contains(new intPair(selectedPoint, clickedPoint))) {
                    gameManager.makeMove(selectedPoint, clickedPoint);
                    gameScreen.updatePieces();
                }
                selectedPoint = -1;
                gameScreen.updateHighlights(selectedPoint);
            }
            return true;
        }

        return false;
    }

    private boolean isValidSelection(int point) {
        List<Piece> pieces = gameManager.getBoard().getPieces(point);
        return !pieces.isEmpty() && pieces.getLast().getOwner() == gameManager.getCurrentPlayer();
    }

    private int getClickedPoint(Vector2 touch) {
        for (int i = 0; i < GameScreen.POINT_X.length; i++) {
            float x = GameScreen.POINT_X[i];
            float y = GameScreen.POINT_Y[i];

            if (i < 12) {
                if (touch.x >= x && touch.x <= x + GameScreen.PIECE_SIZE &&
                    touch.y <= y + GameScreen.PIECE_SIZE && touch.y >= y - GameScreen.PIECE_SIZE * 4) {
                    return i;
                }
            } else {
                if (touch.x >= x && touch.x <= x + GameScreen.PIECE_SIZE &&
                    touch.y >= y && touch.y <= y + GameScreen.PIECE_SIZE * 5) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean isHouseClicked(Vector2 touch) {
        float houseX = 1135;
        float houseY = 91;
        float houseWidth = 55;
        float houseHeight = 504;

        return touch.x >= houseX && touch.x <= houseX + houseWidth &&
            touch.y >= houseY && touch.y <= houseY + houseHeight;
    }
}
