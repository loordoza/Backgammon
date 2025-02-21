package io.github.backgammon.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
    private Label turnLabel;
    private Label noMovesLabel;
    private Image boardImage;
    private List<Image> whitePieces;
    private List<Image> blackPieces;

    private Texture whiteTexture, blackTexture, whiteSelectedTexture, blackSelectedTexture, whiteHouseTexture, blackHouseTexture;
    private List<Texture> diceTextures;

    private List<Highlight> highlights;

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

        Skin skin = new Skin();
        skin.add("default-font", new BitmapFont());
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        labelStyle.fontColor = Color.WHITE;

        turnLabel = new Label((gameManager.getCurrentPlayer() == Player.WHITE ? "WHITE" : "BLACK"), labelStyle);
        turnLabel.setFontScale(2f);
        turnLabel.setPosition(250, 330);
        stage.addActor(turnLabel);

        noMovesLabel = new Label("No moves", labelStyle);
        noMovesLabel.setFontScale(2f);
        noMovesLabel.setPosition(380, 330);
        noMovesLabel.setVisible(false);
        stage.addActor(noMovesLabel);

        whiteTexture = new Texture("white_piece.png");
        blackTexture = new Texture("black_piece.png");
        whiteSelectedTexture = new Texture("white_piece_selected.png");
        blackSelectedTexture = new Texture("black_piece_selected.png");
        whiteHouseTexture = new Texture("white_house.png");
        blackHouseTexture = new Texture("black_house.png");

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

        highlights = new ArrayList<>();

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
        updateLabel();
        updateNoMoves();
        stage.act(v);
        stage.draw();

        if (gameManager.hasWon(Player.WHITE)) {
            game.setScreen(new EndScreen(game, Player.WHITE));
        } else if (gameManager.hasWon(Player.BLACK)) {
            game.setScreen(new EndScreen(game, Player.BLACK));
        }
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

        List<Piece> whiteBarPieces = gameManager.getBoard().getBarPieces(Player.WHITE);
        for (int i = 0; i < whiteBarPieces.size(); i++) {
            Image pieceImage = whitePieces.get(whiteIndex++);
            float x = 546;
            float y = 500 - (i * PIECE_SIZE * 0.9f);
            pieceImage.setPosition(x, y);

            boolean isMovable = movablePieces.contains(-1);
            pieceImage.setDrawable(new TextureRegionDrawable(isMovable ? whiteSelectedTexture : whiteTexture));
        }

        List<Piece> blackBarPieces = gameManager.getBoard().getBarPieces(Player.BLACK);
        for (int i = 0; i < blackBarPieces.size(); i++) {
            Image pieceImage = blackPieces.get(blackIndex++);
            float x = 546;
            float y = 200 + (i * PIECE_SIZE * 0.9f);
            pieceImage.setPosition(x, y);

            boolean isMovable = movablePieces.contains(-1);
            pieceImage.setDrawable(new TextureRegionDrawable(isMovable ? blackSelectedTexture : blackTexture));
        }

        int whiteHousePieces = gameManager.howManyInHouse(Player.WHITE);
        for (int i = 0; i < whiteHousePieces; i++) {
            Image pieceImage = whitePieces.get(whiteIndex++);
            float x = 1135;
            float y = 92 + (i * 12);
            pieceImage.setPosition(x, y);
            pieceImage.setSize(55, 9);
            pieceImage.setDrawable(new TextureRegionDrawable(whiteHouseTexture));
        }

        int blackHousePieces = gameManager.howManyInHouse(Player.BLACK);
        for (int i = 0; i < blackHousePieces; i++) {
            Image pieceImage = blackPieces.get(blackIndex++);
            float x = 1135;
            float y = 424 + (i * 12);
            pieceImage.setPosition(x, y);
            pieceImage.setSize(55, 9);
            pieceImage.setDrawable(new TextureRegionDrawable(blackHouseTexture));
        }
    }

    private void updateDices() {
        List<Integer> values = gameManager.getDiceValues();
        List<Boolean> usedDices = gameManager.getUsedDices();
        dice1.setDrawable(new TextureRegionDrawable(diceTextures.get(values.get(0) - 1)));
        dice2.setDrawable(new TextureRegionDrawable(diceTextures.get(values.get(1) - 1)));

        if (usedDices.get(0)) {
            dice1.getColor().a = 0.5f;
        } else {
            dice1.getColor().a = 1f;
        }

        if (usedDices.get(1)) {
            dice2.getColor().a = 0.5f;
        } else {
            dice2.getColor().a = 1f;
        }
    }

    private void updateLabel() {
        Player currentPlayer = gameManager.getCurrentPlayer();
        turnLabel.setText(currentPlayer == Player.WHITE ? "WHITE" : "BLACK");
    }

    private void updateNoMoves() {
        if (gameManager.getPossibleMoves().isEmpty()) {
            noMovesLabel.setVisible(true);
        } else {
            noMovesLabel.setVisible(false);
        }
    }

    public void updateHighlights(int selectedPoint) {
        for (Highlight highlight : highlights) {
            highlight.remove();
        }
        highlights.clear();

        List<Integer> possibleMoves;
        if (gameManager.arePiecesInBar()) {
            possibleMoves = gameManager.getPossibleMovesForPiece(-1);
        } else if (selectedPoint != -1) {
            possibleMoves = gameManager.getPossibleMovesForPiece(selectedPoint);
        } else {
            return;
        }

        for (int target : possibleMoves) {
            if (target == -999) {
                Highlight highlight = new Highlight(1134, 424, 55, 172);
                highlights.add(highlight);
                stage.addActor(highlight);
                continue;
            } else if (target == 999) {
                Highlight highlight = new Highlight(1134, 92, 55, 172);
                highlights.add(highlight);
                stage.addActor(highlight);
                continue;
            }
            float x = POINT_X[target];
            float y = POINT_Y[target];
            Highlight highlight = new Highlight(x, y, PIECE_SIZE, PIECE_SIZE);
            highlights.add(highlight);
            stage.addActor(highlight);
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
        whiteTexture.dispose();
        blackTexture.dispose();
        whiteSelectedTexture.dispose();
        blackSelectedTexture.dispose();
        whiteHouseTexture.dispose();
        blackHouseTexture.dispose();
        diceTextures.forEach(Texture::dispose);
    }

    public Stage getStage() {
        return stage;
    }
}
