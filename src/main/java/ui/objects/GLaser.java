package ui.objects;

import domain.Constants;
import domain.listeners.LaserListener;
import domain.models.powerup.objects.Laser;
import ui.Drawable;

import java.awt.*;

public class GLaser implements Drawable, LaserListener {
    private Laser laser;
    private int id;

    public GLaser(Laser laser) {
        this.laser = laser;
        this.id = laser.getId();
        laser.addObserver(this);
    }

    public int getId() {
        return id;
    }

    @Override
    public void update(Object o) {

    }


    @Override
    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval((int) laser.getX(),
                (int) laser.getY(),
                (int) Constants.laserDiameter,
                (int) Constants.laserDiameter);
    }
}
