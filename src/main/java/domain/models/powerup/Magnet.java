package domain.models.powerup;

import domain.Constants;

public class Magnet extends Powerup {

    public Magnet(double x, double y) {
        super(x, y);
        isStackable = true;
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public Constants.PowerupType getType() {
        return Constants.PowerupType.MAGNET;
    }
}
