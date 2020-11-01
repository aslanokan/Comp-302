package ui.objects.brick;

import domain.models.brick.Brick;
import ui.Drawable;

import java.awt.*;

public class GWrapperBrick extends GBrick implements Drawable {
    public GWrapperBrick(Brick brick) {
        super(brick);
    }

    public void draw(Graphics g) {
        g.fillRect((int) getX(), (int) getY(),
                (int) getWidth(), (int) getHeight());
    }
}
