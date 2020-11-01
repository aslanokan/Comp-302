package domain.models.powerup;

import domain.Constants;

public class DestructiveLaserGun extends Powerup {

    public DestructiveLaserGun(double x, double y) {
        super(x, y);
        isStackable = false;
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public Constants.PowerupType getType() {
        return Constants.PowerupType.DESTRUCTIVE_LASER_GUN;
    }
}
