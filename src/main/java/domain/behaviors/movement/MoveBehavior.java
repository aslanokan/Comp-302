package domain.behaviors.movement;

public interface MoveBehavior {
    void move();

    void reverse();

    double getX();

    void setX(double x);

    double getY();

    void setY(double y);

    double getVelocityX();

    void setVelocityX(double x);

    double getVelocityY();

    void setVelocityY(double y);
}
