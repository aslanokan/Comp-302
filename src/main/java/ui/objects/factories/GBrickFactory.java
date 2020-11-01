package ui.objects.factories;

import domain.models.brick.Brick;
import ui.objects.brick.*;

public class GBrickFactory {

    private static GBrickFactory gBrickFactory;

    private GBrickFactory() {

    }

    public static GBrickFactory getInstance() {
        if (gBrickFactory == null) {
            gBrickFactory = new GBrickFactory();
        }
        return gBrickFactory;
    }

    public GBrick createBrick(Brick brick) {
        String type = brick.getType();
        if (type.equals("HalfMetalBrick")) {
            return new GHalfMetalBrick(brick);
        } else if (type.equals("MineBrick")) {
            return new GMineBrick(brick);
        } else if (type.equals("SimpleBrick")) {
            return new GSimpleBrick(brick);
        } else if (type.equals("WrapperBrick")) {
            return new GWrapperBrick(brick);
        }
        return null;
    }
}
