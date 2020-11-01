package domain.behaviors.movement;

import domain.Constants;
import domain.Time;

public class CircularMovementBehavior implements MoveBehavior {
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;

    private double centerX;
    private double centerY;
    private double radius;
    private double angle;
    private double angularVelocity;

    public CircularMovementBehavior(double x, double y, double velocityX, double velocityY) {
        this.x = x;
        this.y = y;
        this.velocityX = 0;
        this.velocityY = 0;

        this.centerX = x;
        this.radius = Constants.paddleInitialWidth * 1.5;
        this.centerY = y - this.radius;
        this.angle = 90;
        this.angularVelocity = 25;
    }

    @Override
    public void move() {
        Time time = Time.getInstance();
        angle += angularVelocity * time.deltaTime() * 1E-3;

        double newX = centerX + radius * Math.cos(Math.toRadians(angle));
        double newY = centerY + radius * Math.sin(Math.toRadians(angle));
        velocityX = newX - x;
        velocityY = newY - y;

        setX(newX);
        setY(newY);
    }

    @Override
    public void reverse() {
        angularVelocity *= -1;
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
    public void setVelocityX(double vx) {
        velocityX = vx;
    }

    @Override
    public double getVelocityY() {
        return velocityY;
    }

    @Override
    public void setVelocityY(double vy) {
        velocityY = vy;
    }
}
