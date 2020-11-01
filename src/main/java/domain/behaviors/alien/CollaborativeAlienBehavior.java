package domain.behaviors.alien;

import domain.Time;
import domain.behaviors.movement.MoveBehavior;
import domain.behaviors.movement.NoMovementBehavior;
import domain.controllers.PlayGameHandler;
import domain.models.alien.Alien;
import domain.models.brick.Brick;
import domain.scenes.GameScene;

import java.util.List;

public class CollaborativeAlienBehavior implements AlienBehavior {

    private GameScene gameScene;
    private Time time;
    private double entranceTime;
    private boolean explodedARow = false;
    private Alien alien;
    private MoveBehavior moveBehaviour;
    private boolean isExtend = false;

    public CollaborativeAlienBehavior(Alien al) {
        gameScene = PlayGameHandler.getInstance().getGameScene();
        time = Time.getInstance();
        entranceTime = time.time();
        alien = al;
        moveBehaviour = new NoMovementBehavior(al.getX(), al.getY());
    }

    @Override
    public void behave() {
        double currentTime = time.time();
        if (currentTime - entranceTime > 5000 && !explodedARow) {
            List<Brick> randomRow = getRandomRow();
            destroyRow(randomRow);
            explodedARow = true;
            if (!isExtend) {
                gameScene.deleteAlienWithoutDying(alien);
            }
        }
        moveBehaviour.move();
    }

    @Override
    public void reverse() {
        moveBehaviour.reverse();
        moveBehaviour.move();
    }

    public void destroyRow(List<Brick> row) {
        for (Brick br : row) {
            gameScene.deleteBrick(br);
        }
    }

    public List<Brick> getRandomRow() {
        return gameScene.getRandomRow();
    }

    public void setIsExtend(boolean bool) {
        isExtend = bool;
    }
}
