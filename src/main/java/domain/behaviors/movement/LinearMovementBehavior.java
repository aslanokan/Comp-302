package domain.behaviors.movement;

import domain.Time;

public class LinearMovementBehavior implements MoveBehavior {
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;

    public LinearMovementBehavior(double x, double y, double velocityX, double velocityY) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }


    @Override
    public void move() {
        Time time = Time.getInstance();
        double newX = getX() + getVelocityX() * time.deltaTime() * 1E-3;
        double newY = getY() + getVelocityY() * time.deltaTime() * 1E-3;
        setX(newX);
        setY(newY);
    }

    @Override
    public void reverse() {
        setVelocityX(getVelocityX() * -1);
        setVelocityY(getVelocityY() * -1);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double getVelocityX() {
        return velocityX;
    }

    @Override
    public void setVelocityX(double x) {
        velocityX = x;
    }

    @Override
    public double getVelocityY() {
        return velocityY;
    }

    @Override
    public void setVelocityY(double y) {
        velocityY = y;
    }
}
