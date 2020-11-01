package domain.factories;


import domain.Constants;
import domain.models.powerup.*;

public class PowerupFactory {
    public static PowerupFactory powerupFactory;

    private PowerupFactory() {

    }

    public static PowerupFactory getInstance() {
        if (powerupFactory == null) {
            powerupFactory = new PowerupFactory();
        }
        return powerupFactory;
    }

    public Powerup createPowerup(Constants.PowerupType powerupType, double x, double y) {
        switch (powerupType) {
            case CHEMICAL_BALL:
                return new ChemicalBall(x, y);
            case DESTRUCTIVE_LASER_GUN:
                return new DestructiveLaserGun(x, y);
            case FIREBALL:
                return new FireBall(x, y);
            case GANG_OF_BALLS:
                return new GangOfBalls(x, y);
            case MAGNET:
                return new Magnet(x, y);
            case TALLER_PADDLE:
                return new TallerPaddle(x, y);
        }
        return null;
    }
}
