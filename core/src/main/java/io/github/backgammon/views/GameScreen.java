package io.github.backgammon.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.backgammon.Backgammon;
import io.github.backgammon.model.GameManager;
import io.github.backgammon.model.Piece;
import io.github.backgammon.model.Player;

import java.util.*;

public class GameScreen implements Screen {
    private final Backgammon game;
    private final GameManager gameManager;

    private Stage stage;
    private Image boardImage;
    private List<Image> whitePieces;
    private List<Image> blackPieces;

    private Image dice1;
    private Image dice2;

    private final float PIECE_SIZE = 51f;
    private static final float[] POINT_X = {59, 141, 223, 305, 305, 469, 623, 705, 787, 869, 951, 1033,
        1033, 951, 869, 787, 705, 623, 469, 305, 305, 223, 141, 59};
    private static final float[] POINT_Y = {592, 592, 592, 592, 592, 592, 592, 592, 592, 592, 592, 592,
        40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40};

    public GameScreen(Backgammon backgammon) {
        this.game = backgammon;
        this.gameManager = new GameManager();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Texture backgroundTexture = new Texture("board.png");
        boardImage = new Image(backgroundTexture);
        boardImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(boardImage);

        Texture whiteTexture = new Texture("white_piece.png");
        Texture blackTexture = new Texture("black_piece.png");
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            List<Piece> pieces = gameManager.getBoard().getPieces(i);
            for (int j = 0; j < pieces.size(); j++) {
                Image pieceImage;
                if (pieces.get(j).getOwner() == Player.WHITE) {
                    pieceImage = new Image(whiteTexture);
                    whitePieces.add(pieceImage);
                } else {
                    pieceImage = new Image(blackTexture);
                    blackPieces.add(pieceImage);
                }

                pieceImage.setSize(PIECE_SIZE, PIECE_SIZE);
                stage.addActor(pieceImage);
            }
        }

        Texture diceTexture1 = new Texture("dice_1.png");
        Texture diceTexture2 = new Texture("dice_2.png");
        dice1 = new Image(diceTexture1);
        dice2 = new Image(diceTexture2);

        dice1.setSize(70, 70);
        dice2.setSize(70, 70);

        dice1.setPosition(650, 310);
        dice2.setPosition(750, 310);

        stage.addActor(dice1);
        stage.addActor(dice2);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        updatePieces();
        stage.act(v);
        stage.draw();
    }

    private void updatePieces() {
        int whiteIndex = 0, blackIndex = 0;

        for (int i = 0; i < 24; i++) {
            List<Piece> pieces = gameManager.getBoard().getPieces(i);
            for (int j = 0; j < pieces.size(); j++) {
                Vector2 position;
                if (i < 12) {
                    position = new Vector2(POINT_X[i], POINT_Y[i] - (j * PIECE_SIZE * 0.9f));
                } else {
                    position = new Vector2(POINT_X[i], POINT_Y[i] + (j * PIECE_SIZE * 0.9f));
                }

                if (pieces.get(j).getOwner() == Player.WHITE) {
                    whitePieces.get(whiteIndex).setPosition(position.x, position.y);
                    whiteIndex++;
                } else {
                    blackPieces.get(blackIndex).setPosition(position.x, position.y);
                    blackIndex++;
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        boardImage.setSize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        boardImage.remove();
    }
}
