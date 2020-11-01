package domain.factories;

import domain.models.brick.*;

public class BrickFactory {

    private static BrickFactory brickFactory;

    private BrickFactory() {

    }

    public static BrickFactory getInstance() {
        if (brickFactory == null) {
            brickFactory = new BrickFactory();
        }
        return brickFactory;
    }

    public Brick createBrick(String type, double x, double y) {
        if (type.equals("HalfMetalBrick")) {
            return new HalfMetalBrick(x, y);
        } else if (type.equals("MineBrick")) {
            return new MineBrick(x, y);
        } else if (type.equals("SimpleBrick")) {
            return new SimpleBrick(x, y);
        } else if (type.equals("WrapperBrick")) {
            return new WrapperBrick(x, y);
        }
        return null;
    }
}