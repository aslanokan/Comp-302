package domain.behaviors.alien;

import domain.Time;
import domain.behaviors.movement.MoveBehavior;
import domain.behaviors.movement.NoMovementBehavior;
import domain.controllers.PlayGameHandler;
import domain.models.alien.Alien;
import domain.scenes.GameScene;

import java.util.Random;

public class RepairingAlienBehavior implements AlienBehavior {
    private int numberOfBricks;
    private Time time;
    private double brickPlaceTime;
    private GameScene gameScene;
    private MoveBehavior moveBehavior;
    private Alien alien;
    private int rand;

    public RepairingAlienBehavior(Alien al) {
        time = Time.getInstance();
        gameScene = PlayGameHandler.getInstance().getGameScene();
        rand = new Random().nextInt(10);
        numberOfBricks = rand;
        alien = al;
        moveBehavior = new NoMovementBehavior(alien.getX(), alien.getY());
    }

    @Override
    public void behave() {
        double currentTime = time.time();
        if (currentTime - brickPlaceTime > 5000 && numberOfBricks > 0) {
            brickPlaceTime = currentTime;
            addBricks();
            numberOfBricks--;
        }
        if (numberOfBricks == 0) {
            gameScene.deleteAlienWithoutDying(alien);
        }
        moveBehavior.move();
    }

    @Override
    public void reverse() {
        moveBehavior.reverse();
        moveBehavior.move();
    }

    @Override
    public void setIsExtend(boolean bool) {

    }

    public void addBricks() {
        gameScene.addRandomBrick();
    }
}
