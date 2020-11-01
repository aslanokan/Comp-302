package domain.models.brick;

import domain.Constants;
import domain.behaviors.movement.LinearMovementBehavior;
import domain.behaviors.movement.NoMovementBehavior;

public class HalfMetalBrick extends Brick {
    public enum HalfMetalState {
        SOLID, CRACKED
    }

    public HalfMetalState halfMetalState;

    public HalfMetalBrick(double x, double y) {
        super(x, y, "HalfMetalBrick");
        this.setWidth(Constants.simpleBrickWidth);
        this.setHeight(Constants.simpleBrickHeight);
        this.setColor("Red");

        halfMetalState = HalfMetalState.SOLID;

        if (this.getMoveable()) {
            setMoveBehavior(new LinearMovementBehavior(x, y, getVelocityX(), getVelocityY()));
        } else {
            setMoveBehavior(new NoMovementBehavior(x, y));
        }
    }

    public HalfMetalBrick(double x, double y, double width, double height, double velocityX, double velocityY, String color, boolean isMovable, String type, int id) {
        super(x, y, width, height, velocityX, velocityY, color, isMovable, type, id);

        if (this.getMoveable()) {
            setMoveBehavior(new LinearMovementBehavior(x, y, getVelocityX(), getVelocityY()));
        } else {
            setMoveBehavior(new NoMovementBehavior(x, y));
        }
    }

}
