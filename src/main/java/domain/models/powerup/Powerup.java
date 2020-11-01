package domain.models.powerup;

import domain.Constants;
import domain.behaviors.movement.LinearMovementBehavior;
import domain.behaviors.movement.MoveBehavior;
import domain.listeners.PowerupListener;

import java.util.LinkedList;
import java.util.List;

public abstract class Powerup {

    public boolean isStackable;
    private boolean isActive;
    private double width;
    private double height;
    private double isActiveFor;
    private double maxTime;
    private double velocityX;
    private double velocityY;
    private List<PowerupListener> listeners = new LinkedList<PowerupListener>();
    private MoveBehavior moveBehavior;
    private String color;

    public Powerup(double x, double y) {
//        this.x = x;
//        this.y = y;
        this.width = Constants.powerupWidth;
        this.height = Constants.powerupHeight;
        isActiveFor = 0;
        this.maxTime = 0;
        moveBehavior = new LinearMovementBehavior(x, y, Constants.powerupVelocityX, Constants.powerupVelocityY);
        this.color = Constants.colors.get("Powerup");
    }

    /**
     * //MODIFIES: this.isActive - must set it to true
     */
    abstract public void activate();

    /**
     * //MODIFIES: this.isActive - must set it to false
     */
    abstract public void deactivate();

    public boolean isActive() {
        return isActive;
    }

    public boolean isStackable() {
        return isStackable;
    }

    public double getX() {
        return moveBehavior.getX();
    }

    public void setX(double x) {
        this.moveBehavior.setX(x);
    }

    public double getY() {
        return moveBehavior.getY();
    }

    public void setY(double y) {
        this.moveBehavior.setY(y);
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getVelocityX() {
        return moveBehavior.getVelocityX();
    }

    public void setVelocityX(double velocityX) {
        this.moveBehavior.setVelocityX(velocityX);
    }

    public double getVelocityY() {
        return this.moveBehavior.getVelocityY();
    }

    public void setVelocityY(double velocityY) {
        this.moveBehavior.setVelocityY(velocityY);
    }

    public abstract Constants.PowerupType getType();

    public double isActiveFor() {
        return isActiveFor;
    }

    public void increaseActiveTime(double deltaTime) {
        isActiveFor += deltaTime;
    }

    public void subscribe(PowerupListener powerupListener) {
        this.listeners.add(powerupListener);
    }

    public MoveBehavior getMoveBehavior() {
        return moveBehavior;
    }

    public void performMove() {
        this.moveBehavior.move();
        this.updateListeners();
    }

    public void updateListeners() {
        for (PowerupListener powerupListener : listeners) {
            powerupListener.update(this);
        }
    }

    public String getColor() {
        return color;
    }

    public double getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(double time) {
        this.maxTime = time;
    }
}
