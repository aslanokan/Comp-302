package domain.behaviors.alien;

import domain.controllers.PlayGameHandler;
import domain.models.alien.Alien;
import domain.scenes.GameScene;

public class CompositeDrunkAlienBehavior extends CompositeAlienBehavior {

    double brickRatio;
    double prevBrickRatio = 1.0;
    GameScene gameScene = PlayGameHandler.getInstance().getGameScene();
    private Alien alien;

    public CompositeDrunkAlienBehavior(Alien al) {
        alien = al;
        brickRatio = gameScene.getBrickRatio();
        if (brickRatio > 0.7) {
            behaviorList.add(new CollaborativeAlienBehavior(alien));
        } else if (brickRatio > 0.6) {
            behaviorList.add(new ConfusedAlienBehavior(alien));
        } else if (brickRatio > 0.5) {
            behaviorList.add(new RepairingAlienBehavior(alien));
        } else if (brickRatio > 0.4) {
            behaviorList.add(new ProtectiveAlienBehavior(alien));
        } else if (brickRatio > 0.3) {
            behaviorList.add(new ConfusedAlienBehavior(alien));
        } else {
            behaviorList.add(new RepairingAlienBehavior(alien));
            behaviorList.add(new ProtectiveAlienBehavior(alien));
        }
    }

    @Override
    public void behave() {
        performBehavior();
    }

    public void performBehavior() {
        brickRatio = gameScene.getBrickRatio();
        changeBehaviour();
        for (AlienBehavior behavior : behaviorList) {
            behavior.behave();
        }
        prevBrickRatio = brickRatio;
    }

    public void changeBehaviour() {
        if (prevBrickRatio > 0.7 && brickRatio < 0.7) {
            behaviorList.clear();
            behaviorList.add(new ConfusedAlienBehavior(alien));
        } else if (prevBrickRatio > 0.6 && brickRatio < 0.6) {
            behaviorList.clear();
            behaviorList.add(new RepairingAlienBehavior(alien));
        } else if (prevBrickRatio > 0.5 && brickRatio < 0.5) {
            behaviorList.clear();
            behaviorList.add(new ProtectiveAlienBehavior(alien));
        } else if (prevBrickRatio > 0.4 && brickRatio < 0.4) {
            behaviorList.clear();
            behaviorList.add(new ConfusedAlienBehavior(alien));
        } else if (prevBrickRatio > 0.3 && brickRatio < 0.3) {
            behaviorList.clear();
            behaviorList.add(new RepairingAlienBehavior(alien));
            behaviorList.add(new ProtectiveAlienBehavior(alien));
        }
    }

    @Override
    public void reverse() {
        for (AlienBehavior behavior : behaviorList) {
            behavior.reverse();
        }
    }

    @Override
    public void setIsExtend(boolean bool) {

    }

}
