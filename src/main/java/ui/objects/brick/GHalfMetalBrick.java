package ui.objects.brick;

import domain.controllers.PlayGameHandler;
import domain.models.brick.Brick;
import domain.models.brick.HalfMetalBrick;
import ui.Drawable;

import java.awt.*;

import static domain.models.brick.HalfMetalBrick.*;

public class GHalfMetalBrick extends GBrick implements Drawable {

    private HalfMetalState state;

    public GHalfMetalBrick(Brick brick) {
        super(brick);
        state = ((HalfMetalBrick) brick).halfMetalState;
    }

    @Override
    public void update(Object o) {
        super.update(o);
        state = ((HalfMetalBrick) o).halfMetalState;
    }

    public void draw(Graphics g) {
        g.fillRect((int) getX(), (int) getY(),
                (int) getWidth(), (int) getHeight());
        if (state == HalfMetalState.CRACKED) {
            g.setColor(Color.BLACK);
            g.drawLine(
                    (int) getX(), (int) getY(),
                    (int) (getX() + getWidth()), (int) (getY() + getHeight()));
            g.drawLine(
                    (int) (getX() + getWidth()), (int) getY(),
                    (int) getX(), (int) (getY() + getHeight()));

            g.drawRect((int) getX(), (int) getY(),
                    (int) getWidth(), (int) getHeight());
        }
    }
}
