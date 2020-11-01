package ui.objects.brick;

import domain.listeners.BrickListener;
import domain.models.brick.Brick;
import ui.Drawable;

public abstract class GBrick implements Drawable, BrickListener {
    private double x;
    private double y;
    private double width;
    private double height;
    private double velocityX;
    private String color;
    private int id;

    public GBrick(Brick brick) {
        this.x = brick.getX();
        this.y = brick.getY();
        this.width = brick.getWidth();
        this.height = brick.getHeight();
        color = brick.getColor();
        id = brick.getId();
        brick.addListener(this);
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
        return velocityX;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void update(Object o) {
        Brick b = (Brick) o;
        this.setX(b.getX());
        this.setY(b.getY());
    }

    public void update(int id) {
    }
}