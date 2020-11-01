package domain.models.alien;

import domain.behaviors.alien.ProtectiveAlienBehavior;

public class ProtectingAlien extends Alien {

    public ProtectingAlien(double x, double y) {
        super(x, y);
        super.type = "ProtectingAlien";
        setAlienBehavior(new ProtectiveAlienBehavior(this));
    }

    public ProtectingAlien(double x, double y, double velocityX, double velocityY, int id) {
        super(x, y, velocityX, velocityY, id);
        super.type = "ProtectingAlien";
        setAlienBehavior(new ProtectiveAlienBehavior(this));
    }

    public void extendBehavior() {
        setAlienBehavior(new ProtectiveAlienBehavior(this));
    }
}
