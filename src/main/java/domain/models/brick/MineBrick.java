package domain.models.brick;

import domain.Constants;
import domain.behaviors.movement.CircularMovementBehavior;
import domain.behaviors.movement.NoMovementBehavior;

public class MineBrick extends Brick {
    public MineBrick(double x, double y) {
        super(x, y, "MineBrick");
        super.setWidth(Constants.mineBrickDiameter);
        super.setHeight(Constants.mineBrickDiameter);
        this.setType("MineBrick");
        this.setColor("Green");

        if (this.getMoveable()) {
            setMoveBehavior(new CircularMovementBehavior(x, y, getVelocityX(), getVelocityY()));
        } else {
            setMoveBehavior(new NoMovementBehavior(x, y));
        }
    }

    public MineBrick(double x, double y, double width, double height, double velocityX, double velocityY, String color, boolean isMovable, String type, int id) {
        super(x, y, width, height, velocityX, velocityY, color, isMovable, type, id);

        if (this.getMoveable()) {
            setMoveBehavior(new CircularMovementBehavior(x, y, getVelocityX(), getVelocityY()));
        } else {
            setMoveBehavior(new NoMovementBehavior(x, y));
        }
    }
}
