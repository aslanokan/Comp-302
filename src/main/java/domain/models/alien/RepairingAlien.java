package domain.models.alien;

import domain.behaviors.alien.RepairingAlienBehavior;

public class RepairingAlien extends Alien {

    public RepairingAlien(double x, double y) {
        super(x, y);
        super.type = "RepairingAlien";
        setAlienBehavior(new RepairingAlienBehavior(this));
    }

    public RepairingAlien(double x, double y, double velocityX, double velocityY, int id) {
        super(x, y, velocityX, velocityY, id);
        super.type = "RepairingAlien";
        setAlienBehavior(new RepairingAlienBehavior(this));
    }

    @Override
    public void extendBehavior() {
        setAlienBehavior(new RepairingAlienBehavior(this));
    }


}
