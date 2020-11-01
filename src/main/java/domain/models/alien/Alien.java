package domain.models.alien;

import domain.Constants;
import domain.Time;
import domain.behaviors.alien.AlienBehavior;
import domain.listeners.AlienListener;

import java.util.ArrayList;

public abstract class Alien {
    private static int count = 0;
    protected String type;
    private double x;
    private double y;
    private double width = Constants.alienWidth;
    private double height = Constants.alienHeight;
    private double velocityX;
    private double velocityY;
    private int id;
    private AlienBehavior alienBehavior;
    private Time time;
    private ArrayList<AlienListener> listeners;

    public Alien(double x, double y, double velocityX, double velocityY) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        id = count;
        listeners = new ArrayList<AlienListener>();
        count++;
    }

    public Alien(double x, double y, double velocityX, double velocityY, int id) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.id = id;
        count++;

        listeners = new ArrayList<AlienListener>();
    }

    public Alien(double x, double y) {
        this(x, y, 0, 0);
    }

    public void addListener(AlienListener al) {
        listeners.add(al);
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setLocation(double xCoor, double yCoor) {
        this.x = xCoor;
        this.y = yCoor;
        publishEvent("setLocation");
    }

    public void behave() {
        alienBehavior.behave();
    }

    public void publishEvent(String name) {
        for (AlienListener al : listeners) {
            al.updateListener(this, name);
        }
    }

    public AlienBehavior getAlienBehavior() {
        return this.alienBehavior;
    }

    public void setAlienBehavior(AlienBehavior alienBehavior) {
        this.alienBehavior = alienBehavior;
    }

    public void delete() {
        publishEvent("delete");
    }

    public String getType() {
        return type;
    }

    public void reverse() {
        alienBehavior.reverse();
    }

    public abstract void extendBehavior();

    public String parse() {
        String result = "Alien";
        result += ":" + this.getX();
        result += ":" + this.getY();
        result += ":" + this.getVelocityX();
        result += ":" + this.getVelocityY();
        result += ":" + this.type;
        result += ":" + this.id;
        return result;
    }

    public static Alien unParse(String[] data) {
        double x = Double.parseDouble(data[1]);
        double y = Double.parseDouble(data[2]);
        double velocityX = Double.parseDouble(data[3]);
        double velocityY = Double.parseDouble(data[4]);
        String type = data[5];
        int id = Integer.parseInt(data[6]);

        switch (type) {

            case "ProtectingAlien":
                return new ProtectingAlien(x, y, velocityX, velocityY, id);
            case "RepairingAlien":
                return new RepairingAlien(x, y, velocityX, velocityY, id);
            case "CollaboratingAlien":
                return new CollaboratingAlien(x, y, velocityX, velocityY, id);
            case "DrunkAlien":
                return new DrunkAlien(x, y, velocityX, velocityY, id);
        }
        return null;
    }
}
