package domain.listeners;

import domain.models.Ball;
import domain.models.alien.Alien;
import domain.models.brick.Brick;
import domain.models.powerup.Powerup;
import domain.models.powerup.objects.Laser;

import java.util.LinkedList;
import java.util.List;

public interface GameListener {
    /**
     * When the destructive laser gun is activated,
     * it publishes the 2 laser objects
     * so that the corresponding UI components can be created
     *
     * @param laser1
     * @param laser2
     */
    void activateDestructiveLaserGun(Laser laser1, Laser laser2);

    /**
     * @param laser1ID the id of the laser object which leaves the scene
     */
    void removeLaserGun(int laser1ID);

    void addPowerup(Powerup powerup);

    void addAlien(Alien alien);

    void addBrick(Brick brick);

    void updateScore(double score);

    void updateLife(int life);

    void activateGangOfBalls(LinkedList<Ball> balls);

    void ballLost(int index);

    void newBall(Ball ball);

    void stackedPowerupsUpdated(List<String> stackedPowerupList);

    void gameFinished(String message, double score, Object[][] dataArray);
}
