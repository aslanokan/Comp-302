package ui.objects;

import domain.models.alien.Alien;
import domain.models.alien.CollaboratingAlien;
import domain.models.alien.DrunkAlien;
import ui.Drawable;

import java.awt.*;

public class GAllien extends Rectangle implements Drawable {

    private String color;
    private int id;

    public GAllien(double x, double y, double width, double height) {
        super((int) x, (int) y, (int) width, (int) height);
    }

    public GAllien(Alien alien) {
        this(alien.getX(), alien.getY(), alien.getWidth(), alien.getHeight());
        if(alien instanceof DrunkAlien) {
            color = "Red";
        } else {
            color = "Green";
        }
        id = alien.getId();
    }

    public void setX(double x) {
        this.x = (int) x;
    }

    public void setY(double y) {
        this.y = (int) y;
    }

    public void setWidth(double width) {
        this.width = (int) width;
    }

    public void setHeight(double height) {
        this.height = (int) height;
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

    @Override
    public void draw(Graphics g) {
        g.fillRect(x, y, width, height);
    }

}
