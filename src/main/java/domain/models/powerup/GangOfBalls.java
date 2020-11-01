package domain.models.powerup;

import domain.Constants;

public class GangOfBalls extends Powerup {
    public GangOfBalls(double x, double y) {
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
        return Constants.PowerupType.GANG_OF_BALLS;
    }
}
