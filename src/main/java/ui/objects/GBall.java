package ui.objects;

import domain.listeners.BallListener;
import domain.models.Ball;
import ui.Drawable;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class GBall extends Ellipse2D.Double implements BallListener, Drawable {
    private Ball ball;

    public GBall(Ball ball) {
        super();
        this.ball = ball;
        ball.addObserver(this);
    }


    public void update(Object o) {
        this.ball = (Ball) o;
    }

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void setFrame(double x, double y, double w, double h) {

    }

    @Override
    public Rectangle2D getBounds2D() {
        return null;
    }

    @Override
    public void draw(Graphics g) {
        double outlineWidth = 0.1;

        if (ball.chemicalBallActive()) {
            g.setColor(Color.GREEN);
            g.fillOval(
                    (int) (ball.getX() - Ball.getBallDimension() * (1+outlineWidth)),
                    (int) (ball.getY() - Ball.getBallDimension() * (1+outlineWidth)),
                    (int) (Ball.getBallDimension() * 2 * (1+2*outlineWidth)),
                    (int) (Ball.getBallDimension() * 2 * (1+2*outlineWidth)));
        }

        g.setColor(Color.RED);
        g.fillOval(
                (int) (ball.getX() - Ball.getBallDimension()),
                (int) (ball.getY() - Ball.getBallDimension()),
                (int) Ball.getBallDimension() * 2,
                (int) Ball.getBallDimension() * 2);

        if (ball.isFireBallActive()) {
            g.setColor(Color.ORANGE);
            g.fillOval(
                    (int) (ball.getX() - Ball.getBallDimension() * (1-outlineWidth)),
                    (int) (ball.getY() - Ball.getBallDimension() * (1-outlineWidth)),
                    (int) (Ball.getBallDimension() * 2 * (1-2*outlineWidth)),
                    (int) (Ball.getBallDimension() * 2 * (1-2*outlineWidth)));
        }
    }
}
