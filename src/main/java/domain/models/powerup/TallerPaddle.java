package domain.models.powerup;

import domain.Constants;

public class TallerPaddle extends Powerup {
    public TallerPaddle(double x, double y) {
        super(x, y);
        super.setMaxTime(Constants.tallerPaddleMaxTime);
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
        return Constants.PowerupType.TALLER_PADDLE;
    }
}
