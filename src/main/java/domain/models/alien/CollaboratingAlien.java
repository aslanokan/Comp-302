package domain.models.alien;


import domain.behaviors.alien.CollaborativeAlienBehavior;

public class CollaboratingAlien extends Alien {

    public CollaboratingAlien(double x, double y) {
        super(x, y);
        super.type = "CollabratingAlien";
        setAlienBehavior(new CollaborativeAlienBehavior(this));
    }

    public CollaboratingAlien(double x, double y, double velocityX, double velocityY, int id) {
        super(x, y, velocityX, velocityY, id);
        super.type = "CollabratingAlien";
        setAlienBehavior(new CollaborativeAlienBehavior(this));
    }

    @Override
    public void extendBehavior() {
        getAlienBehavior().setIsExtend(true);
        setAlienBehavior(new CollaborativeAlienBehavior(this));
    }
}
