package domain.models;

import domain.Constants;
import domain.listeners.PaddleListener;

import java.util.ArrayList;
import java.util.List;

public class Paddle {
    private ArrayList<PaddleListener> listeners = new ArrayList<PaddleListener>();
    private double x;
    private double y;
    private double width;
    private double height;
    private double speed = Constants.paddleSpeed;
    private double velocityX = 0;
    private double velocityY = 0;
    private String color;
    private double currentRotationAngle;

    private List<PaddleListener> paddleListeners = new ArrayList<PaddleListener>();

    public Paddle(double x, double y) {
        this.width = Constants.paddleInitialWidth;
        this.height = Constants.paddleHeight;
        this.x = x;
        this.y = y;
        this.color = "Purple";
        this.currentRotationAngle = 0;
    }

    public Paddle(double x, double y, double width, double height, double velocityX, double velocityY, String color, double currentRotationAngle) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.width = width;
        this.height = height;
        this.color = color;
        this.currentRotationAngle = currentRotationAngle;
    }

    public void changeLocation(double x, double y) {
        // MODIFIES: paddle.location
        this.x = x;
        this.y = y;
        updateListeners();
    }

    /**
     * @param angle direction of rotation expressed as an enum
     *              //REQUIRES: 0 {@literal <} paddle.currentRotationAngle {@literal <} 45 if direction = "POSITIVE_ROTATION"
     *              -45 {@literal <} paddle.currentRotationAngle {@literal <} 0 if direction = "NEGATIVE_ROTATION"
     *              //MODIFIES: paddle.location
     */
    public void changeRotation(double angle) {
        this.currentRotationAngle = angle;
        updateListeners();
    }

    /**
     * //REQUIRES: currentRotationAngle != 0
     * //MODIFIES: currentRotationAngle at a constant rate (undoRotationAngle)
     * //EFFECTS: currentRotationAngle of the paddle is changed towards the horizontal position
     */
    public void undoRotation() {
        if (this.currentRotationAngle != 0) {
            // animation & physics to be implemented later

        }
        updateListeners();
    }

    public void addListener(PaddleListener ls) {
        listeners.add(ls);
    }

    public void publishMoveEvent(String name, double newX, double newY) {
        for (PaddleListener ls : listeners) {
            ls.update(newX);
        }
    }

    public void publishRotateEvent(double newAngle) {
        for (PaddleListener ls : listeners) {
            ls.update(newAngle);
        }
    }

    public double getHeight() {
        return height;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        updateListeners();
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        updateListeners();
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
        updateListeners();
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
        updateListeners();
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
        updateListeners();
    }

    public double getCurrentRotationAngle() {
        return currentRotationAngle;
    }

    public void updateListeners() {
        for (PaddleListener listener : paddleListeners) {
            listener.update(this.getX());
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void addObserver(PaddleListener listener) {
        paddleListeners.add(listener);
    }

    public String parse() {
        String result = "Paddle";
        result += ":" + this.x;
        result += ":" + this.y;
        result += ":" + this.width;
        result += ":" + this.height;
        result += ":" + this.velocityX;
        result += ":" + this.velocityY;
        result += ":" + this.color;
        result += ":" + this.currentRotationAngle;
        return result;
    }

    public static Paddle unParse(String[] data) {
        double x = Double.parseDouble(data[1]);
        double y = Double.parseDouble(data[2]);
        double width = Double.parseDouble(data[3]);
        double height = Double.parseDouble(data[4]);
        double velocityX = Double.parseDouble(data[5]);
        double velocityY = Double.parseDouble(data[6]);
        String color = data[7];
        double angle = Double.parseDouble(data[8]);

        return new Paddle(x, y, width, height, velocityX, velocityY, color, angle);
    }

    public double getSpeed() {
        return speed;
    }
}
