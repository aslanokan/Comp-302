package domain.behaviors.alien;

import domain.Constants;
import domain.behaviors.movement.LinearMovementBehavior;
import domain.behaviors.movement.MoveBehavior;
import domain.models.alien.Alien;

public class ProtectiveAlienBehavior implements AlienBehavior {
    private Alien alien;
    private MoveBehavior moveBehavior;

    public ProtectiveAlienBehavior(Alien al) {
        alien = al;
        moveBehavior = new LinearMovementBehavior(alien.getX(), alien.getY(), Constants.alienSpeed, 0);
    }

    @Override
    public void behave() {
        moveBehavior.move();
        alien.setLocation(moveBehavior.getX(), moveBehavior.getY());
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
