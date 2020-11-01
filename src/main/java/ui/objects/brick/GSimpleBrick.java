package ui.objects.brick;

import domain.models.brick.Brick;
import ui.Drawable;

import java.awt.*;

public class GSimpleBrick extends GBrick implements Drawable {
    private String color;
    private int id;

    public GSimpleBrick(Brick brick) {
        super(brick);
    }

    @Override
    public void draw(Graphics g) {
        g.fillRect((int) getX(), (int) getY(),
                (int) getWidth(), (int) getHeight());
    }
}