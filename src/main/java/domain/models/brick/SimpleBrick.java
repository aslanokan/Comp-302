package domain.models.brick;

import domain.Constants;
import domain.behaviors.movement.LinearMovementBehavior;
import domain.behaviors.movement.NoMovementBehavior;

public class SimpleBrick extends Brick {
    private static final double simpleBrickWidth = 40;
    private static final double simpleBrickHeight = 20;

    public SimpleBrick(double x, double y) {
        super(x, y, "SimpleBrick");
        super.setWidth(Constants.simpleBrickWidth);
        super.setHeight(Constants.simpleBrickHeight);
        this.setType("SimpleBrick");
        this.setColor("Blue");

        if (this.getMoveable()) {
            setMoveBehavior(new LinearMovementBehavior(x, y, getVelocityX(), getVelocityY()));
        } else {
            setMoveBehavior(new NoMovementBehavior(x, y));
        }
    }

    public SimpleBrick(double x, double y, double width, double height, double velocityX, double velocityY, String color, boolean isMovable, String type, int id) {
        super(x, y, width, height, velocityX, velocityY, color, isMovable, type, id);

        if (this.getMoveable()) {
            setMoveBehavior(new LinearMovementBehavior(x, y, getVelocityX(), getVelocityY()));
        } else {
            setMoveBehavior(new NoMovementBehavior(x, y));
        }
    }
}
