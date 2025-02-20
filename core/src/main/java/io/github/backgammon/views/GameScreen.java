package io.github.backgammon.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.backgammon.Backgammon;
import io.github.backgammon.input.InputHandler;
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

    private Texture whiteTexture, blackTexture, whiteSelectedTexture, blackSelectedTexture;
    private List<Texture> diceTextures;

    private Image dice1, dice2;

    public static final float PIECE_SIZE = 51f;
    public static final float[] POINT_X = {1033, 951, 869, 787, 705, 623, 469, 387, 305, 223, 141, 59,
        59, 141, 223, 305, 387, 469, 623, 705, 787, 869, 951, 1033};
    public static final float[] POINT_Y = {592, 592, 592, 592, 592, 592, 592, 592, 592, 592, 592, 592,
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

        whiteTexture = new Texture("white_piece.png");
        blackTexture = new Texture("black_piece.png");
        whiteSelectedTexture = new Texture("white_piece_selected.png");
        blackSelectedTexture = new Texture("black_piece_selected.png");

        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            Image whitePiece = new Image(whiteTexture);
            whitePiece.setSize(PIECE_SIZE, PIECE_SIZE);
            whitePieces.add(whitePiece);
            stage.addActor(whitePiece);

            Image blackPiece = new Image(blackTexture);
            blackPiece.setSize(PIECE_SIZE, PIECE_SIZE);
            blackPieces.add(blackPiece);
            stage.addActor(blackPiece);
        }

        diceTextures = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            diceTextures.add(new Texture("dice_" + i + ".png"));
        }

        dice1 = new Image(diceTextures.get(0));
        dice2 = new Image(diceTextures.get(1));

        dice1.setSize(70, 70);
        dice2.setSize(70, 70);
        dice1.setPosition(650, 310);
        dice2.setPosition(750, 310);

        stage.addActor(dice1);
        stage.addActor(dice2);

        Gdx.input.setInputProcessor(new InputHandler(gameManager, this));
        updatePieces();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        updateDices();
        stage.act(v);
        stage.draw();
    }

    public void updatePieces() {
        int whiteIndex = 0, blackIndex = 0;
        List<Integer> movablePieces = gameManager.getMovablePieces();

        for (int i = 0; i < 24; i++) {
            List<Piece> pieces = gameManager.getBoard().getPieces(i);
            for (int j = 0; j < pieces.size(); j++) {
                Vector2 position = new Vector2(POINT_X[i], i < 12 ? POINT_Y[i] - (j * PIECE_SIZE * 0.9f) : POINT_Y[i] + (j * PIECE_SIZE * 0.9f));
                Piece piece = pieces.get(j);
                Image pieceImage = (piece.getOwner() == Player.WHITE) ? whitePieces.get(whiteIndex++) : blackPieces.get(blackIndex++);
                pieceImage.setPosition(position.x, position.y);

                boolean isMovable = movablePieces.contains(i) && j == pieces.size() - 1;
                pieceImage.setDrawable(new TextureRegionDrawable(isMovable ? (piece.getOwner() == Player.WHITE ? whiteSelectedTexture : blackSelectedTexture) : (piece.getOwner() == Player.WHITE ? whiteTexture : blackTexture)));
            }
        }
    }

    private void updateDices() {
        List<Integer> values = gameManager.getDiceValues();
        dice1.setDrawable(new TextureRegionDrawable(diceTextures.get(values.get(0) - 1)));
        dice2.setDrawable(new TextureRegionDrawable(diceTextures.get(values.get(1) - 1)));
    }

    public boolean isDiceClicked(Vector2 touch) {
        return isTouched(dice1, touch) || isTouched(dice2, touch);
    }

    private boolean isTouched(Image dice, Vector2 touch) {
        return touch.x >= dice.getX() && touch.x <= dice.getX() + dice.getWidth()
            && touch.y >= dice.getY() && touch.y <= dice.getY() + dice.getHeight();
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
        whiteTexture.dispose();
        blackTexture.dispose();
        whiteSelectedTexture.dispose();
        blackSelectedTexture.dispose();
        diceTextures.forEach(Texture::dispose);
    }

    public Stage getStage() {
        return stage;
    }
}
