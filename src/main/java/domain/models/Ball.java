package domain.models;

import domain.listeners.BallListener;

import java.util.ArrayList;
import java.util.List;

public class Ball {
    private static final double ballDimension = 17 * 0.5;
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private double acceleration = 5;
    private List<BallListener> ballListeners = new ArrayList<BallListener>();
    // powerups
    private boolean fireBallActive;
    private boolean chemicalBallActive;


    public Ball(double velocityX, double velocityY, double x, double y) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.fireBallActive = false;
        this.chemicalBallActive = false;
    }

    public Ball(double x, double y, double velocityX, double velocityY, boolean fireBallActive, boolean chemicalBallActive) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.fireBallActive = fireBallActive;
        this.chemicalBallActive = chemicalBallActive;
    }

    public static double getBallDimension() {
        return ballDimension;
    }

    public boolean chemicalBallActive() {
        return chemicalBallActive;
    }

    public boolean isFireBallActive() {
        return fireBallActive;
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

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void activateChemicalBall(boolean chemicalBallActive) {
        this.chemicalBallActive = chemicalBallActive;
    }

    public void activateFireBall(boolean fireBallActive) {
        this.fireBallActive = fireBallActive;
    }

    public void addObserver(BallListener listener) {
        ballListeners.add(listener);
    }

    public void updateListeners() {
        for (BallListener listener : ballListeners) {
            listener.update(this);
        }
    }

    public void setLocation(double x, double y) {
        this.setX(x);
        this.setY(y);
    }

    public String parse() {
        String result = "Ball";
        result += ":" + this.x;
        result += ":" + this.y;
        result += ":" + this.velocityX;
        result += ":" + this.velocityY;
        result += ":" + this.fireBallActive;
        result += ":" + this.chemicalBallActive;
        return result;
    }

    public static Ball unParse(String[] data) {
        double x = Double.parseDouble(data[1]);
        double y = Double.parseDouble(data[2]);
        double velocityX = Double.parseDouble(data[3]);
        double velocityY = Double.parseDouble(data[4]);
        boolean fireBallActive = Boolean.parseBoolean(data[5]);
        boolean chemicalBallActive = Boolean.parseBoolean(data[6]);

        return new Ball(x, y, velocityX, velocityY, fireBallActive, chemicalBallActive);
    }
}
