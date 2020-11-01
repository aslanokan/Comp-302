import domain.controllers.PlayGameHandler;
import domain.managers.CollisionManager;
import domain.models.Ball;
import domain.models.Paddle;
import domain.scenes.GameScene;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CollisionManagerTest {
    PlayGameHandler pgh;
    GameScene gs;
    CollisionManager cm;
    Paddle paddle;
    Ball ball;


    @Before
    public void setUp() {
        pgh = PlayGameHandler.getInstance();
        gs = pgh.getGameScene();
        cm = gs.getCollisionManager();

        gs.initPaddle();
        gs.initBall();

        paddle = pgh.getPaddle();
        ball = pgh.getBall();
    }

    @Test
    public void checkBallCollision() {

    }

    @Test
    public void checkBallCollisionWestBound() {
        ball.setY(200);
        ball.setX(0);
        ball.setVelocityX(-5);
        cm.checkBallCollision();
        assertTrue(ball.getVelocityX() >= 0);
        assertTrue(ball.getX() > 0);
    }

    @Test
    public void checkBallCollisionEastBound() {
        ball.setY(200);
        ball.setX(500);
        ball.setVelocityX(5);
        cm.checkBallCollision();
        assertTrue(ball.getVelocityX() <= 0);
        assertTrue(ball.getX() < 500);
    }

    @Test
    public void checkBallCollisionNorthBound() {
        ball.setX(200);
        ball.setY(0);
        ball.setVelocityY(-5);
        cm.checkBallCollision();
        assertTrue(ball.getVelocityY() >= 0);
        assertTrue(ball.getY() >= 0);
    }

    @Test
    public void checkBallCollisionSouthBound() {
        ball.setX(200);
        ball.setY(500);
        ball.setVelocityY(5);
        cm.checkBallCollision();
        assertTrue(ball.getVelocityY() <= 0);
        assertTrue(ball.getY() <= 500);
    }

    @Test
    public void checkBallCollisionPaddle() {

    }

    @Test
    public void testLimitPaddleEast() {
        paddle.setX(500);
        cm.limitPaddle();
        assertTrue(paddle.getX() <= 500 - paddle.getWidth());
    }

    @Test
    public void testLimitPaddleWest() {
        paddle.setX(-90);
        cm.limitPaddle();
        assertTrue(paddle.getX() == 0);
    }

}