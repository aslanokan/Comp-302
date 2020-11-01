package ui.objects;

import domain.Constants;
import domain.listeners.PowerupListener;
import domain.models.powerup.Powerup;
import ui.Drawable;

import java.awt.*;

public class GPowerup extends Rectangle implements Drawable, PowerupListener {
    String color;

    public GPowerup(Powerup powerup) {
        super((int) powerup.getX(),
                (int) powerup.getY(),
                Constants.powerupWidth,
                Constants.powerupHeight);
        this.color = Constants.colors.get("Powerup");
        powerup.subscribe(this);
    }

    public GPowerup(double x, double y, double width, double height) {
        super((int) x,
                (int) y,
                (int) width,
                (int) height);
        this.color = Constants.colors.get("Powerup");
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = (int) x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = (int) y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = (int) width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = (int) height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        color = color;
    }

    @Override
    public void update(Powerup powerup) {
        this.setX(powerup.getX());
        this.setY(powerup.getY());
    }

    @Override
    public void draw(Graphics g) {
        g.fillRect(x, y, width, height);
    }
}

