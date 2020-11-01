package domain.behaviors.alien;

import domain.Time;
import domain.behaviors.movement.MoveBehavior;
import domain.behaviors.movement.NoMovementBehavior;
import domain.controllers.PlayGameHandler;
import domain.models.alien.Alien;
import domain.scenes.GameScene;

public class ConfusedAlienBehavior implements AlienBehavior {
    private MoveBehavior moveBehavior;
    private Alien alien;
    private Time time = Time.getInstance();
    private double creationTime;
    private GameScene gameScene;

    public ConfusedAlienBehavior(Alien al) {
        alien = al;
        creationTime = time.time();
        gameScene = PlayGameHandler.getInstance().getGameScene();
        moveBehavior = new NoMovementBehavior(alien.getX(), al.getY());
    }

    @Override
    public void behave() {
        moveBehavior.move();
        double currentTime = time.time();
        if (currentTime - creationTime > 5000){
            gameScene.deleteAlienWithoutDying(alien);
        }
    }

    @Override
    public void reverse() {
        moveBehavior.reverse();
        moveBehavior.move();
    }

    @Override
    public void setIsExtend(boolean bool) {

    }

}
