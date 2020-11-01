package domain.models.powerup.objects;

import domain.Constants;
import domain.listeners.LaserListener;

import java.util.ArrayList;

public class Laser {
    private static int count = 0;
    private int id;
    private double x;
    private double y;
    private double width = Constants.laserDiameter;
    private double height = Constants.laserDiameter;
    private double velocityX;
    private double velocityY;
    private ArrayList<LaserListener> listeners = new ArrayList<>();

    public Laser(double x, double y) {
        this.x = x;
        this.y = y;
        this.id = count;
        count++;
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
        updateListeners();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void updateListeners() {
        for (LaserListener ll : listeners) {
            ll.update(this);
        }
    }

    public void addObserver(LaserListener ll) {
        listeners.add(ll);
    }

    public int getId() {
        return id;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
