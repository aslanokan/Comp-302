package domain.managers;

import domain.Constants;
import domain.Time;
import domain.behaviors.alien.CompositeAlienBehavior;
import domain.behaviors.alien.ProtectiveAlienBehavior;
import domain.controllers.PlayGameHandler;
import domain.models.Ball;
import domain.models.Paddle;
import domain.models.alien.Alien;
import domain.models.alien.DrunkAlien;
import domain.models.brick.Brick;
import domain.models.brick.HalfMetalBrick;
import domain.models.powerup.Powerup;
import domain.models.powerup.objects.Laser;
import domain.scenes.GameScene;

import java.util.HashSet;
import java.util.List;

import static domain.models.brick.HalfMetalBrick.HalfMetalState.CRACKED;

public class CollisionManager {

    private static Time time = Time.getInstance();
    private double boundX;
    private double boundY;
    private double boundW;
    private double boundH;
    private GameScene scene;
    private PowerupManager powerupManager;

    // get reference to GameScene on creation
    public CollisionManager(GameScene scene) {
        this.scene = scene;
        powerupManager = scene.getPowerupManager();
        //  get these from the scene
        boundX = 0;
        boundY = 0;
        boundW = Constants.gameSceneWidth;
        boundH = Constants.gameSceneHeight - Constants.controlBarHeight;
    }

    // TODO implement alien collisions
    //      alien - boundary collisions
    //      alien - ball collisions
    //      alien - alien collision

    public boolean canPlaceBrick(Brick brick) {
        PlayGameHandler pgh = PlayGameHandler.getInstance();
        Ball ball = pgh.getBall();
        List<Brick> bricks = pgh.getBricks();
        List<Alien> aliens = pgh.getAliens();

        if (isBallBrickColliding(ball, brick)) {
            return false;
        }

        for (Brick other : bricks) {
            if (isBrickBrickColliding(brick, other)) {
                return false;
            }
        }

        for (Alien alien : aliens) {
            if (isAlienBrickColliding(alien, brick)) {
                return false;
            }
        }

        return true;
    }

    public boolean canPlaceAlien(Alien alien) {
        PlayGameHandler pgh = PlayGameHandler.getInstance();
        Ball ball = pgh.getBall();
        List<Brick> bricks = pgh.getBricks();
        List<Alien> aliens = pgh.getAliens();

        if (isAlienBallColliding(ball, alien)) {
            return false;
        }

        for (Brick brick : bricks) {
            if (isAlienBrickColliding(alien, brick)) {
                return false;
            }
        }

        for (Alien other : aliens) {
            if (isAlienAlienColliding(other, alien)) {
                return false;
            }
        }

        return true;
    }

    public void checkAlienCollisions() {
        PlayGameHandler pgh = PlayGameHandler.getInstance();
        List<Ball> balls = pgh.getCurrentBalls();
        List<Alien> aliens = pgh.getAliens();

        HashSet<Alien> toDelete = new HashSet<>();
        for (Alien alien : aliens) {
            // check bounds
            if (isAlienBoundsColliding(alien)) {
                alien.reverse();
            }

            // check ball
            for (Ball ball : balls) {
                if (isAlienBallColliding(ball, alien)) {
                    if (alien.getAlienBehavior() instanceof ProtectiveAlienBehavior) {
                        if (ball.getY() < alien.getY()) {
                            toDelete.add(alien);
                        }
                    } else if (alien instanceof DrunkAlien && (((CompositeAlienBehavior) (alien.getAlienBehavior())).getAlienBehaviorList().stream().anyMatch(behavior -> behavior instanceof ProtectiveAlienBehavior))) {
                        if (ball.getY() < alien.getY()) {
                            toDelete.add(alien);
                        }
                    } else {
                        toDelete.add(alien);
                    }

                    if (!ball.chemicalBallActive()) {
                        setNewBallDirection(ball, alien);
                    }
                }
            }
        }

        // check other aliens
        for (int i = 0; i < aliens.size() - 1; i++) {
            Alien a1 = aliens.get(i);
            for (int j = i + 1; j < aliens.size(); j++) {
                Alien a2 = aliens.get(j);
                if (isAlienAlienColliding(a1, a2)) {
                    a1.reverse();
                    a2.reverse();
                }
            }
        }

        // delete aliens
        for (Alien a : toDelete) {
            scene.deleteAlien(a);
        }
    }

    private boolean isAlienBoundsColliding(Alien a) {
        double ax = a.getX();
        double ay = a.getY();
        double aw = a.getWidth();
        double ah = a.getHeight();

        return ax < boundX
                || ax + aw > boundX + boundW
                || ay < boundY
                || ay + ah > boundY + boundH;
    }

    private boolean isAlienAlienColliding(Alien a1, Alien a2) {
        double a1x = a1.getX();
        double a1y = a1.getY();
        double a1w = a1.getWidth();
        double a1h = a1.getHeight();

        double a2x = a2.getX();
        double a2y = a2.getY();
        double a2w = a2.getWidth();
        double a2h = a2.getHeight();

        return isRectRectColliding(
                a1x, a1y, a1w, a1h,
                a2x, a2y, a2w, a2h
        );
    }

    private boolean isAlienBallColliding(Ball ball, Alien alien) {
        return isCircleRectColliding(
                ball.getX(),
                ball.getY(),
                Ball.getBallDimension(),
                alien.getX(),
                alien.getY(),
                alien.getWidth(),
                alien.getHeight()
        );
    }

    public void checkPaddleCollision() {
        limitPaddle();

        PlayGameHandler pgh = PlayGameHandler.getInstance();
        Paddle paddle = pgh.getPaddle();

        List<Powerup> powerups = powerupManager.getRevealedPowerups();
        for (Powerup p : powerups) {
            if (isPowerUpPaddleColliding(p, paddle)) {
                powerupManager.powerupCatched(p);
                p.setY(Constants.gameSceneHeight);
                p.updateListeners();
            }
        }
    }

    public void checkBallCollision() {
        PlayGameHandler pgh = PlayGameHandler.getInstance();
        List<Brick> bricks = pgh.getBricks();
        List<Ball> balls = pgh.getCurrentBalls();
        Paddle paddle = pgh.getPaddle();

        HashSet<Ball> lostBalls = new HashSet<>();
        for (Ball ball : balls) {
            //  Bounds collisions
            if (isBallBoundsColliding(ball)) {
                if (ball.getY() + 2 * Ball.getBallDimension() > boundY + boundH) {
                    lostBalls.add(ball);
                }
                stepBallBack(ball);
                setNewBallDirectionBounds(ball);
            }

            // Paddle collision
            if (isBallPaddleColliding(ball, paddle)) {
                stepBallBack(ball);
                setNewBallDirection(ball, paddle);
            }

            // Brick - Ball collisions
            HashSet<Brick> bricksToDelete = new HashSet<>();

            for (Brick brick : bricks) {
                boolean isColliding = isBallBrickColliding(ball, brick);
                // TODO may need a better check mechanism for the MineBrick
                if (checkLasers(brick)) {
                    bricksToDelete.add(brick);
                }
                if (isColliding) {
                    stepBallBack(ball);
                    if (!brick.getType().equals("HalfMetalBrick")) {
                        bricksToDelete.add(brick);
                    } else if (ball.getY() < brick.getY()) {
                        bricksToDelete.add(brick);
                    }

                    if (ball.isFireBallActive()) {
                        System.out.println("fireballll");
                        for (Brick other : bricks) {
                            double dist = scene.calculateDistanceBetweenBricks(brick, other);
                            if (brick != other &&
                                    dist <= Constants.fireballMaxDistBetweenBricks
                                    && !other.getType().equals("HalfMetalBrick")
                            ) {
                                bricksToDelete.add(other);
                            }
                        }

                        if (brick.getType().equals("HalfMetalBrick")) {
                            if (ball.getY() >= brick.getY()) {

                                HalfMetalBrick b = ((HalfMetalBrick) brick);
                                switch (b.halfMetalState) {
                                    case SOLID:
                                        b.halfMetalState = CRACKED;
                                        b.updateListeners();
                                        break;
                                    case CRACKED:
                                        bricksToDelete.add(b);
                                        break;
                                    default:
                                        System.err.println("unknown half metal brick state");
                                }
                            }
                        }
                    }

                    if (!ball.chemicalBallActive()) {
                        setNewBallDirection(ball, brick);
                    }
                }
            }
            for (Brick brick : bricksToDelete) {
                scene.deleteBrick(brick);
            }
        }
        for (Ball ball : lostBalls) {
            scene.ballLost(ball);
        }
    }

    public void checkBrickCollisions() {
        PlayGameHandler pgh = PlayGameHandler.getInstance();
        List<Brick> bricks = pgh.getBricks();

        // Brick - Brick Collisions
        for (int i = 0; i < bricks.size() - 1; i++) {
            Brick brick = bricks.get(i);

            for (int j = i + 1; j < bricks.size(); j++) {
                Brick other = bricks.get(j);
                boolean isColliding = isBrickBrickColliding(brick, other);
                if (isColliding) {
                    // reverse direction
                    brick.getMoveBehavior().reverse();
                    brick.getMoveBehavior().move();

                    other.getMoveBehavior().reverse();
                    brick.getMoveBehavior().move();
                }
            }
        }

        for (Brick brick : bricks) {
            if (isBrickBoundsColliding(brick)) {
                brick.getMoveBehavior().reverse();
                brick.getMoveBehavior().move();
            }
        }
    }

    private void stepBrickBack(Brick b) {
        double dt = Time.getInstance().deltaTime() * 1e-3;
        b.setX(b.getX() - b.getVelocityX() * dt);
        b.setY(b.getY() - b.getVelocityY() * dt);
    }

    private boolean isBrickBrickColliding(Brick b1, Brick b2) {
        double b1x = b1.getX();
        double b1y = b1.getY();
        double b1w = b1.getWidth();
        double b1h = b1.getHeight();

        double b2x = b2.getX();
        double b2y = b2.getY();
        double b2w = b2.getWidth();
        double b2h = b2.getHeight();

        return isRectRectColliding(
                b1x, b1y, b1w, b1h,
                b2x, b2y, b2w, b2h
        );
    }

    private boolean isPowerUpPaddleColliding(Powerup powerup, Paddle paddle) {
        double pupx = powerup.getX();
        double pupy = powerup.getY();
        double pupw = powerup.getWidth();
        double puph = powerup.getHeight();

        double padx = paddle.getX();
        double pady = paddle.getY();
        double padw = paddle.getWidth();
        double padh = paddle.getHeight();

        return isRectRectColliding(
                pupx, pupy, pupw, puph,
                padx, pady, padw, padh
        );
    }

    private boolean isAlienBrickColliding(Alien alien, Brick brick) {
        double ax = alien.getX();
        double ay = alien.getY();
        double aw = alien.getWidth();
        double ah = alien.getHeight();

        double bx = brick.getX();
        double by = brick.getY();
        double bw = brick.getWidth();
        double bh = brick.getHeight();

        return isRectRectColliding(
                ax, ay, aw, ah,
                bx, by, bw, bh
        );
    }


    private boolean isBrickBoundsColliding(Brick b) {
        double bx = b.getX();
        double by = b.getY();
        double bw = b.getWidth();
        double bh = b.getHeight();

        return bx < boundX
                || bx + bw > boundX + boundW
                || by < boundY
                || by + bh > boundY + boundH;
    }

    public void limitPaddle() {
        Paddle paddle = PlayGameHandler.getInstance().getPaddle();
        paddle.setX(clamp(paddle.getX(), boundX, boundX + boundW - paddle.getWidth()));
    }

    private void setNewBallDirectionBounds(Ball ball) {
        double bx = ball.getX();
        double by = ball.getY();
        double br = Ball.getBallDimension();

        if (bx - br < boundX) {
            ball.setVelocityX(-ball.getVelocityX());
            ball.setX(boundX + br);
        } else if (bx + br > boundX + boundW) {
            ball.setVelocityX(-ball.getVelocityX());
            ball.setX(boundX + boundW - br);
        }

        if (by - br < boundY) {
            ball.setVelocityY(-ball.getVelocityY());
            ball.setY(boundY + br);
        } else if (by + br > boundY + boundH) {
            ball.setVelocityY(-ball.getVelocityY());
            ball.setY(boundY + boundH - br);
        }
    }

    private boolean isBallBoundsColliding(Ball ball) {
        double bx = ball.getX();
        double by = ball.getY();
        double br = Ball.getBallDimension();

        return bx - br < boundX
                || bx + br > boundX + boundW
                || by - br < boundY
                || by + br > boundY + boundH;

    }


    private boolean isBallPaddleColliding(Ball ball, Paddle paddle) {
        return isCircleRotatedRectColliding(
                ball.getX(),
                ball.getY(),
                Ball.getBallDimension(),
                paddle.getX(),
                paddle.getY(),
                paddle.getWidth(),
                paddle.getHeight(),
                paddle.getCurrentRotationAngle()
        );
    }

    private boolean isBallBrickColliding(Ball ball, Brick brick) {
        return isCircleRectColliding(
                ball.getX(),
                ball.getY(),
                Ball.getBallDimension(),
                brick.getX(),
                brick.getY(),
                brick.getWidth(),
                brick.getHeight()
        );
    }

    /**
     * @param cx x coordinate of the center of the circle
     * @param cy y coordinate of the center of the circle
     * @param cr radius of circle
     * @param rx x coordinate of top right corner of the rectangle
     * @param ry y coordinate of top right corner of the rectangle
     * @param rw width of rectangle
     * @param rh height of rectangle
     * @return true if the circle and the rectangle is colliding, false otherwise
     */
    private boolean isCircleRectColliding(double cx, double cy, double cr, double rx, double ry, double rw, double rh) {
        // get closest point of the circle on the rectangle
        double closestX = clamp(cx, rx, rx + rw);
        double closestY = clamp(cy, ry, ry + rh);

        // get the distance from the circle to the rectangle, squared
        double dx = closestX - cx;
        double dy = closestY - cy;
        double distToCenterSquared = dx * dx + dy * dy;

        // check whether this distance is smaller than the circle's radius
        return distToCenterSquared < cr * cr;
    }

    /**
     * @param b1x x coordinate of top right corner of the first rectangle
     * @param b1y y coordinate of top right corner of the first rectangle
     * @param b1w width of the first rectangle
     * @param b1h height of the first rectangle
     * @param b2x x coordinate of top right corner of the second rectangle
     * @param b2y y coordinate of top right corner of the second rectangle
     * @param b2w width of the second rectangle
     * @param b2h height of the second rectangle
     * @return whether the rectangles are overlapping or not
     */
    private boolean isRectRectColliding(double b1x, double b1y, double b1w, double b1h,
                                        double b2x, double b2y, double b2w, double b2h) {
        return b2x + b2w > b1x
                && b2x < b1x + b1w
                && b2y + b2h > b1y
                && b2y < b1y + b1h;
    }

    private boolean isCircleRotatedRectColliding(double cx, double cy, double cr, double rx, double ry, double rw, double rh, double theta) {
        double cosTheta = Math.cos(-Math.toRadians(theta));
        double sinTheta = Math.sin(-Math.toRadians(theta));
        double originX = rx + rw * 0.5;
        double originY = ry + rh * 0.5;

        double cxNew = cosTheta * (cx - originX) - sinTheta * (cy - originY) + originX;
        double cyNew = sinTheta * (cx - originX) + cosTheta * (cy - originY) + originY;

        return isCircleRectColliding(cxNew, cyNew, cr, rx, ry, rw, rh);
    }

    private void setNewBallDirection(Ball ball, Brick brick) {
        double brickX = brick.getX();
        double brickY = brick.getY();
        double brickW = brick.getWidth();
        double brickH = brick.getHeight();

        setNewBallDirection(ball, brickX, brickY, brickW, brickH);
    }

    private void setNewBallDirection(Ball ball, Paddle paddle) {
        double paddleX = paddle.getX();
        double paddleY = paddle.getY();
        double paddleW = paddle.getWidth();
        double paddleH = paddle.getHeight();
        double paddleRotation = paddle.getCurrentRotationAngle();

        setNewBallDirection(ball, paddleX, paddleY, paddleW, paddleH, paddleRotation);
    }

    private void setNewBallDirection(Ball ball, Alien alien) {
        setNewBallDirection(ball, alien.getX(), alien.getY(), alien.getWidth(), alien.getHeight());
    }

    private void setNewBallDirection(Ball ball, double rx, double ry, double rw, double rh) {
        setNewBallDirection(ball, rx, ry, rw, rh, 0);
    }

    private void setNewBallDirection(Ball ball, double rx, double ry, double rw, double rh, double rotation) {
        double ballX = ball.getX();
        double ballY = ball.getY();
        double ballR = Ball.getBallDimension();

        // n is the normal vector to the colliding point
        double a = 1.0 / Math.sqrt(2);
        double nx = 0;
        double ny = -1;
        if (ballX < rx && ballY < ry) {
            // upper left corner
            nx = -a;
            ny = -a;
        } else if (ballX > rx + rw && ballY < ry) {
            // upper right corner
            nx = a;
            ny = -a;
        } else if (ballX < rx && ballY > ry + rh) {
            // bottom left corner
            nx = -a;
            ny = a;
        } else if (ballX > rx + rw && ballY > ry + rh) {
            // bottom right corner
            nx = a;
            ny = a;
        } else if (ballX < rx) {
            // ball is on the left
            nx = -1;
            ny = 0;
            if (Math.abs(rotation) < 1) ball.setX(rx - ballR);
        } else if (ballX > rx + rw) {
            // ball is on the right
            nx = 1;
            ny = 0;
            if (Math.abs(rotation) < 1) ball.setX(rx + rw + ballR);
        } else if (ballY < ry) {
            // ball is above
            nx = 0;
            ny = -1;
            if (Math.abs(rotation) < 1) ball.setY(ry - ballR);
        } else if (ballY > ry + rh) {
            // ball is below
            nx = 0;
            ny = 1;
            if (Math.abs(rotation) < 1) ball.setY(ry + rh + ballR);
        } else {
//            System.err.println("unexpected collision case");
        }

        double c = Math.cos(Math.toRadians(rotation));
        double s = Math.sin(Math.toRadians(rotation));
        // rotate the normal by rotation degrees
        double nxNew = nx * c - ny * s;
        double nyNew = nx * s + ny * c;
        nx = nxNew;
        ny = nyNew;

        // get reflected velocity
        double nDotV = dot(ball.getVelocityX(), ball.getVelocityY(), nx, ny);
        double newVelX = ball.getVelocityX() - 2 * nDotV * nx;
        double newVelY = ball.getVelocityY() - 2 * nDotV * ny;

        // set new velocity
        ball.setVelocityX(newVelX);
        ball.setVelocityY(newVelY);
    }

    public boolean checkLasers(Brick brick) {
        if (powerupManager.getLasers() != null) {
            for (Laser laser : powerupManager.getLasers()) {
                if ((brick.getX() - laser.getWidth() <= laser.getX() && laser.getX() <= brick.getX() + brick.getWidth() + laser.getWidth()) &&
                        (brick.getY() <= laser.getY() && laser.getY() <= brick.getY() + brick.getHeight())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void stepBallBack(Ball ball) {
        double deltaTime = Time.getInstance().deltaTime() * 1e-3;
        ball.setX(ball.getX() - ball.getVelocityX() * deltaTime);
        ball.setY(ball.getY() - ball.getVelocityY() * deltaTime);

    }

    //// utility functions ////

    /**
     * @param t   Value to clamp
     * @param min Lower bound
     * @param max Upper bound
     * @return t clamped between min and max
     */
    private double clamp(double t, double min, double max) {
        return Math.min(max, Math.max(min, t));
    }

    /**
     * @return dot product of two vectors
     */
    private double dot(double x1, double y1, double x2, double y2) {
        return x1 * x2 + y1 * y2;
    }
}
