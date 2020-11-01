package domain.managers;

import domain.Constants;
import domain.Time;
import domain.factories.PowerupFactory;
import domain.listeners.GameListener;
import domain.models.Ball;
import domain.models.powerup.Powerup;
import domain.models.powerup.objects.Laser;
import domain.scenes.GameScene;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

public class PowerupManager {
    private List<Powerup> activatedPowerups = new LinkedList<>();
    private List<Powerup> stackedPowerups = new LinkedList<>();
    private List<String> stackedPowerupsStrings = new ArrayList<>();
    private List<Powerup> revealedPowerups = new LinkedList<>();
    private List<Laser> activeLasers = new LinkedList<>();
    private Constants.DestructiveLaserGunState laserState = Constants.DestructiveLaserGunState.NOT_ACTIVE;
    private Constants.MagnetState magnetState;
    private int destructiveLaserGunShot = 0;
    private GameScene gameScene;

    public PowerupManager(GameScene gameScene) {
        this.gameScene = gameScene;
    }

    public void createPowerup(Constants.PowerupType powerupType, double x, double y) {
        Powerup powerup = PowerupFactory.getInstance().createPowerup(powerupType, x, y);
        revealPowerup(powerup);
    }

    public void revealPowerup(Powerup powerup) {
        revealedPowerups.add(powerup);
        gameScene.getListeners().stream().forEach(listener -> listener.addPowerup(powerup));
    }

    public void powerupCatched(Powerup powerup) {
        boolean stackable = Constants.powerupStackability.get(powerup.getType());
        if (stackable) {
            stackPowerup(powerup);
        } else {
            activatePowerup(powerup);
        }
    }

    public void activatePowerup(Powerup powerup) {
        if (activatedPowerups.stream().filter(p -> p.getType().equals(powerup.getType())).count() == 0) {
            this.activatedPowerups.add(powerup);
            this.adaptGame(powerup);
        } else {
            // TODO add a method to increase existing time of the powerup
            if (powerup.getType() == Constants.PowerupType.CHEMICAL_BALL) {
                powerup.setMaxTime(powerup.getMaxTime() + Constants.chemicalBallMaxTime);
            }
            if (powerup.getType() == Constants.PowerupType.TALLER_PADDLE) {
                powerup.setMaxTime(powerup.getMaxTime() + Constants.tallerPaddleMaxTime);
            }
//            System.out.println("you already have" + powerup.getType());
        }
    }

    public void activatePowerup(Constants.PowerupType powerupType) {
        for (Powerup p : stackedPowerups) {
            if (p.getType() == powerupType) {
                removePowerupFromStack(p);
                activatePowerup(p);
                break;
            }
        }
    }

    public void removePowerupFromStack(Powerup powerup) {
        stackedPowerups.remove(powerup);
        if (powerup.getType() == Constants.PowerupType.CHEMICAL_BALL) {
            stackedPowerupsStrings.remove("C");
        } else if (powerup.getType() == Constants.PowerupType.TALLER_PADDLE) {
            stackedPowerupsStrings.remove("T");
        } else if (powerup.getType() == Constants.PowerupType.MAGNET) {
            stackedPowerupsStrings.remove("M");
        }
        gameScene.getListeners().stream().forEach(listener -> listener.stackedPowerupsUpdated(stackedPowerupsStrings));
    }

    public void adaptGame(Powerup powerup) {
        Constants.PowerupType powerupType = powerup.getType();
        switch (powerupType) {
            case TALLER_PADDLE:
                activateTallerPaddle();
                break;
            case MAGNET:
                activateMagnet();
                break;
            case FIREBALL:
                activateFireBall();
                break;
            case CHEMICAL_BALL:
                activateChemicalBall();
                break;
            case GANG_OF_BALLS:
                activateGangOfBalls();
                break;
            case DESTRUCTIVE_LASER_GUN:
                activateDestructiveLaserGun();
                break;
        }
    }

    public void updatePowerupTimes(double deltaTime) {
        for (Powerup p : activatedPowerups) {
            p.increaseActiveTime(deltaTime);
        }
    }

    private void activateTallerPaddle() {
        gameScene.getPaddle().setWidth(gameScene.getPaddle().getWidth() * 2);
    }

    private void activateFireBall() {
        for (Ball ball : gameScene.getCurrentBalls()) {
            ball.activateFireBall(true);
        }
    }

    private void activateChemicalBall() {
        for (Ball ball : gameScene.getCurrentBalls()) {
            ball.activateChemicalBall(true);
        }
    }

    private void activateGangOfBalls() {
        Ball originalBall = gameScene.getBall();
        LinkedList<Ball> balls = new LinkedList<Ball>();
        balls.add(originalBall);
        double coordinateX = originalBall.getX();
        double coordinateY = originalBall.getY();
        double originalBallVelocityX = originalBall.getVelocityX();
        double originalBallVelocityY = originalBall.getVelocityY();
        for (int i = 1; i < 10; i++) {
            double velocityX = originalBallVelocityX * Math.cos(Constants.newBallAngle * i) - originalBallVelocityY * Math.sin(Constants.newBallAngle * i);
            double velocityY = originalBallVelocityY * Math.sin(Constants.newBallAngle * i) + originalBallVelocityY * Math.cos(Constants.newBallAngle * i);
            double mag = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
            velocityX = velocityX / mag * 0.14;
            velocityY = velocityY / mag * 0.14;
            Ball newBall = new Ball(velocityX, velocityY, coordinateX, coordinateY);
            balls.add(newBall);
        }
        gameScene.setCurrentBalls(balls);
        gameScene.getListeners().stream().forEach(listener -> listener.activateGangOfBalls(balls));
    }

    private void activateDestructiveLaserGun() {
        destructiveLaserGunShot = Constants.destructiveLaserGunShots;
        laserState = Constants.DestructiveLaserGunState.FIRE;
    }

    private void addLasers() {
        double x1 = gameScene.getPaddle().getX() - Constants.laserDiameter / 2;
        double y1 = gameScene.getPaddle().getY() - Constants.laserDiameter;

        double x2 = x1 + gameScene.getPaddle().getWidth() - Constants.laserDiameter / 2;
        double y2 = y1;
        Laser laser1 = new Laser(x1, y1);
        Laser laser2 = new Laser(x2, y2);
        activeLasers.add(laser1);
        activeLasers.add(laser2);
        destructiveLaserGunShot--;
        laserState = Constants.DestructiveLaserGunState.WAIT;

        for (GameListener gameListener : gameScene.getListeners()) {
            gameListener.activateDestructiveLaserGun(laser1, laser2);
        }
    }

    private void activateMagnet() {
        double x = gameScene.getPaddle().getX() + (gameScene.getPaddle().getWidth() - Ball.getBallDimension()) / 2;
        double y = gameScene.getPaddle().getY() - Ball.getBallDimension();
        gameScene.getBall().setLocation(x, y);
        magnetState = Constants.MagnetState.RESERVED;
    }

    private void deactivateMagnet() {
        magnetState = Constants.MagnetState.NOT_INITIALIZED;
    }

    public void stackPowerup(Powerup powerup) {
        stackedPowerups.add(powerup);
        if (powerup.getType() == Constants.PowerupType.CHEMICAL_BALL) {
            stackedPowerupsStrings.add("C");
        } else if (powerup.getType() == Constants.PowerupType.TALLER_PADDLE) {
            stackedPowerupsStrings.add("T");
        } else if (powerup.getType() == Constants.PowerupType.MAGNET) {
            stackedPowerupsStrings.add("M");
        }
        gameScene.getListeners().stream().forEach(listener -> listener.stackedPowerupsUpdated(stackedPowerupsStrings));
    }

    private void deactivateChemicalBall() {
        for (Ball ball : gameScene.getCurrentBalls()) {
            ball.activateChemicalBall(false);
        }
    }

    private void deactivateChemicalBall(Ball ball) {
        ball.activateChemicalBall(false);
    }

    private void deactivateFireBall() {
    }

    private void deactivateFireBall(Ball ball) {
        ball.activateFireBall(false);
    }

    private void deactivatDestructiveLaserGun() {
        laserState = Constants.DestructiveLaserGunState.NOT_ACTIVE;
    }

    private void deactivateTallerPaddle() {
        gameScene.getPaddle().setWidth(Constants.paddleInitialWidth);
    }

    public void checkPowerupActivity() {
        revealedPowerups.stream().forEach(powerup -> powerup.performMove());
        try {
            for (int i = 0; i < activatedPowerups.size(); i++) {
                Powerup powerup = activatedPowerups.get(i);
                if (powerup.getType() == Constants.PowerupType.TALLER_PADDLE &&
                        powerup.isActiveFor() >= powerup.getMaxTime()) {
                    deactivatePowerup(powerup);
                }

                if (powerup.getType() == Constants.PowerupType.MAGNET && magnetState == Constants.MagnetState.USED) {
                    deactivatePowerup(powerup);
                }

                if (powerup.getType() == Constants.PowerupType.CHEMICAL_BALL &&
                        powerup.isActiveFor() >= powerup.getMaxTime()) {
                    deactivatePowerup(powerup);
                }

                if (powerup.getType() == Constants.PowerupType.DESTRUCTIVE_LASER_GUN) {
                    // Waits to trigger new LaserGun
                    if (destructiveLaserGunShot > 0) {
                        if (powerup.isActiveFor() * 1E-3 > (Constants.destructiveLaserGunShots - destructiveLaserGunShot) *
                                Constants.waitForLaserGun) {
                            laserState = Constants.DestructiveLaserGunState.FIRE;
                        }
                        if (laserState == Constants.DestructiveLaserGunState.FIRE) {
                            addLasers();
                        }
                    } else {
                        deactivatePowerup(powerup);
                    }
                }
            }
            moveLasers();
        } catch (ConcurrentModificationException e) {
//            System.err.println("Something wrong with powerups");
        }
    }

    private void moveLasers() {
        for (int i = 0; i < activeLasers.size(); i++) {
            Laser laser = activeLasers.get(i);
            double x = laser.getX() + Constants.laserVelocityX * Time.getInstance().deltaTime() * 1E-3;
            double y = laser.getY() + Constants.laserVelocityY * Time.getInstance().deltaTime() * 1E-3;

            laser.setLocation(x, y);
            if (laser.getY() <= 0) {
                activeLasers.remove(laser);
                for (GameListener gameListener : gameScene.getListeners()) {
                    gameListener.removeLaserGun(laser.getId());
                }
            }
        }
    }

    public void deactivatePowerup(Constants.PowerupType powerupType) {
        Powerup powerup = null;
        for (Powerup powerup1 : activatedPowerups) {
            if (powerup1.getType() == powerupType) {
                powerup = powerup1;
            }
        }
        if (powerup != null) {
            deactivatePowerup(powerup);
        }
    }

    //public void subscribe(GameListener gameListener) {
      //  gameScene.getListeners().add(gameListener);
    //}

    public void deactivatePowerup(Powerup powerup) {
        activatedPowerups.remove(powerup);

        Constants.PowerupType powerupType = powerup.getType();
        switch (powerupType) {
            case DESTRUCTIVE_LASER_GUN:
                deactivatDestructiveLaserGun();
                break;
            case CHEMICAL_BALL:
                deactivateChemicalBall();
                break;
            case FIREBALL:
                deactivateFireBall();
                break;
            case MAGNET:
                deactivateMagnet();
                break;
            case TALLER_PADDLE:
                deactivateTallerPaddle();
                break;
        }
    }

    public boolean isActive(Constants.PowerupType powerupType) {
        return activatedPowerups.stream().filter(p -> p.getType() == powerupType).count() > 0;
    }

    public List<Laser> getLasers() {
        return activeLasers;
    }

    public List<Powerup> getActivatedPowerups() {
        return activatedPowerups;
    }

    public List<Powerup> getRevealedPowerups() {
        return revealedPowerups;
    }

    /**
     * @param lostBall the ball that was lost
     */
    public void ballLost(Ball lostBall) {
        // EFFECTS: deactivates the fireball and chemical ball
        deactivateChemicalBall(lostBall);
        deactivateFireBall(lostBall);
        boolean fireballStillActive = false;
        boolean chemicalBallStillActive = false;
        for (Ball ball : gameScene.getCurrentBalls()) {
            if (ball.isFireBallActive()) {
                fireballStillActive = true;
            }
            if (ball.chemicalBallActive()) {
                chemicalBallStillActive = true;
            }
        }
        if (!fireballStillActive) {
            activatedPowerups.remove(getPowerupByType(Constants.PowerupType.FIREBALL));
        }
        if (!chemicalBallStillActive) {
            activatedPowerups.remove(getPowerupByType(Constants.PowerupType.CHEMICAL_BALL));
        }
    }

    /**
     * @param type PowerupType
     * @return the active powerup with the matching type
     * if no such powerup exists return null
     */
    private Powerup getPowerupByType(Constants.PowerupType type) {
        for (Powerup powerup : activatedPowerups) {
            if (powerup.getType() == type) {
                return powerup;
            }
        }
        return null;
    }
}
