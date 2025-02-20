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

        if (gameScreen.isDiceClicked(touch)) {
            gameManager.rollTwoDices();
            gameScreen.updatePieces();
            return true;
        }

        int clickedPoint = getClickedPoint(touch);
        if (clickedPoint != -1) {
            if (selectedPoint == -1) {
                if (isValidSelection(clickedPoint)) {
                    selectedPoint = clickedPoint;
                }
            } else {
                if (gameManager.getPossibleMoves().contains(new intPair(selectedPoint, clickedPoint))) {
                    gameManager.makeMove(selectedPoint, clickedPoint);
                    gameManager.nextTurn();
                    gameScreen.updatePieces();
                }
                selectedPoint = -1;
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
}
