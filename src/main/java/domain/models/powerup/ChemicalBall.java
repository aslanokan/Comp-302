package domain.models.powerup;

import domain.Constants;

public class ChemicalBall extends Powerup {

    public ChemicalBall(double x, double y) {
        super(x, y);
        super.setMaxTime(Constants.chemicalBallMaxTime);
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
        return Constants.PowerupType.CHEMICAL_BALL;
    }
}
