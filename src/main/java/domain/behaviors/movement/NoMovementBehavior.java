package domain.behaviors.movement;

public class NoMovementBehavior implements MoveBehavior {
    private double x;
    private double y;
    private double velocityX = 0;
    private double velocityY = 0;

    public NoMovementBehavior(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void move() {

    }

    @Override
    public void reverse() {

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
