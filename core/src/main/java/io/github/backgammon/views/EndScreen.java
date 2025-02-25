package io.github.backgammon.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.backgammon.Backgammon;
import io.github.backgammon.model.Player;

public class EndScreen implements Screen {
    private final Stage stage;
    private final Label winLabel;
    private final TextButton backButton;

    public EndScreen(Backgammon game, Player winner) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        winLabel = new Label(winner + " WINS!", skin);
        winLabel.setAlignment(Align.center);
        winLabel.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 1.5f, Align.center);

        backButton = new TextButton("New Game", skin);
        backButton.setSize(300, 80);
        backButton.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 3f, Align.center);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });

        stage.addActor(winLabel);
        stage.addActor(backButton);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
