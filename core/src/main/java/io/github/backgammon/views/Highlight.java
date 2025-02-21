package io.github.backgammon.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Highlight extends Image {
    public Highlight(float x, float y, float width, float height) {
        super(new Texture("highlight.png"));
        setSize(width, height);
        setPosition(x, y);
        setColor(Color.YELLOW);
        getColor().a = 0.5f;
    }
}
