package ui.objects.brick;

import domain.models.brick.Brick;
import ui.Drawable;

import java.awt.*;

public class GMineBrick extends GBrick implements Drawable {

    public GMineBrick(Brick brick) {
        super(brick);
    }

    @Override
    public void draw(Graphics g) {
        g.fillOval((int) getX(), (int) getY(),
                (int) getWidth(), (int) getHeight());
    }
}
