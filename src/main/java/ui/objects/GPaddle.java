package ui.objects;

import domain.listeners.PaddleListener;
import domain.models.Paddle;
import ui.Drawable;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class GPaddle extends Rectangle implements PaddleListener, Drawable {
    private Paddle paddle;

    public GPaddle(Paddle paddle) {
        super((int) paddle.getX(), (int) paddle.getY(), (int) paddle.getWidth(), (int) paddle.getHeight());
        paddle.addObserver(this);
        this.paddle = paddle;
    }

    public void update(Object paddle, int x, int y) {
        // System.out.println(((Paddle)paddle).getX());
        //int x = (int) (Paddle)paddle.getX();
        //int y = (int) (Paddle)paddle.getY();
        //System.out.println(x);
        //setlocation(x, y);

    }

    public void update(Object o, int x, int y, int width, int height) {
        setBounds(x, y, width, height);
    }

    @Override
    public void update(Object o) {

    }

    @Override
    public void draw(Graphics g) {
        // Save the rotation of g to old
        // Create rotated 2d graphics object from g
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        g2d.rotate(Math.toRadians(paddle.getCurrentRotationAngle()), paddle.getX() + paddle.getWidth() / 2, paddle.getY() + paddle.getHeight() / 2);
        g2d.setColor(Color.RED);
        g2d.fillRect(
                (int) paddle.getX(),
                (int) paddle.getY(),
                (int) paddle.getWidth(),
                (int) paddle.getHeight()
        );

        g2d.setTransform(old);
    }
}