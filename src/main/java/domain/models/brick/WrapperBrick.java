package domain.models.brick;

import domain.Constants;
import domain.behaviors.movement.LinearMovementBehavior;
import domain.behaviors.movement.NoMovementBehavior;

import java.util.HashMap;

public class WrapperBrick extends Brick {
    private static HashMap<Constants.PowerupType, Boolean> powerupsInTheBricks = new HashMap<Constants.PowerupType, Boolean>() {{
        put(Constants.PowerupType.GANG_OF_BALLS, false);
        put(Constants.PowerupType.FIREBALL, false);
        put(Constants.PowerupType.DESTRUCTIVE_LASER_GUN, false);
        put(Constants.PowerupType.MAGNET, false);
        put(Constants.PowerupType.CHEMICAL_BALL, false);
        put(Constants.PowerupType.TALLER_PADDLE, false);
    }};
    private Constants.PowerupType powerupType;
    private String alienType;

    public WrapperBrick(double x, double y) {
        super(x, y, "WrapperBrick");
        this.setWidth(Constants.simpleBrickWidth);
        this.setHeight(Constants.simpleBrickHeight);
        this.setType("WrapperBrick");
        this.setColor("Yellow");

        if (this.getMoveable()) {
            setMoveBehavior(new LinearMovementBehavior(x, y, getVelocityX(), getVelocityY()));
        } else {
            setMoveBehavior(new NoMovementBehavior(x, y));
        }
    }

    public WrapperBrick(double x, double y, double width, double height, double velocityX, double velocityY, String color,
                        boolean isMovable, String type, int id, String powerupType, String alienType) {
        super(x, y, width, height, velocityX, velocityY, color, isMovable, type, id);
        this.powerupType = Constants.stringToPowerupType.get(powerupType);
        this.alienType = alienType;

        if (this.getMoveable()) {
            setMoveBehavior(new LinearMovementBehavior(x, y, getVelocityX(), getVelocityY()));
        } else {
            setMoveBehavior(new NoMovementBehavior(x, y));
        }
    }

    public Constants.PowerupType getPowerupType() {
        return powerupType;
    }

    public void setPowerupType(Constants.PowerupType powerupType) {
        this.powerupType = powerupType;
    }

    public String getAlienType() {
        return alienType;
    }

    public void setAlienType(String alienType) {
        this.alienType = alienType;
    }

    @Override
    public void setX(double x) {
        super.setX(x);
    }

    @Override
    public void setY(double y) {
        super.setY(y);
    }

    public String parseWB() {
        String result = "Brick";
        result += ":" + this.getX();
        result += ":" + this.getY();
        result += ":" + this.getWidth();
        result += ":" + this.getHeight();
        result += ":" + this.getVelocityX();
        result += ":" + this.getVelocityY();
        result += ":" + this.getColor();
        result += ":" + this.getIsMoveable();
        result += ":" + this.getType();
        result += ":" + this.getId();
        result += ":" + ((this.powerupType != null) ? this.powerupType : "null");
        result += ":" + ((this.alienType != null) ? this.alienType : "null");
        return result;
    }
}
