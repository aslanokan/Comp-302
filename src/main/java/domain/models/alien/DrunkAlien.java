package domain.models.alien;

import domain.behaviors.alien.CompositeAlienBehavior;
import domain.behaviors.alien.CompositeDrunkAlienBehavior;

public class DrunkAlien extends Alien {

    public DrunkAlien(double x, double y) {
        super(x, y);
        super.type = "DrunkAlien";
        CompositeAlienBehavior behavior = new CompositeDrunkAlienBehavior(this);
        setAlienBehavior(behavior);
    }

    public DrunkAlien(double x, double y, double velocityX, double velocityY, int id) {
        super(x, y, velocityX, velocityY, id);
        super.type = "DrunkAlien";
        CompositeAlienBehavior behavior = new CompositeDrunkAlienBehavior(this);
        setAlienBehavior(behavior);
    }

    public void extendBehavior() {
        CompositeAlienBehavior behavior = new CompositeDrunkAlienBehavior(this);
        setAlienBehavior(behavior);
    }
}
