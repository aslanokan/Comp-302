package domain.controllers;

import domain.Constants;
import domain.listeners.GameListener;
import domain.managers.PowerupManager;
import domain.models.Ball;
import domain.models.Paddle;
import domain.models.alien.Alien;
import domain.models.brick.Brick;
import domain.models.powerup.Powerup;
import domain.scenes.GameScene;

import java.util.List;

public class PlayGameHandler {
    private static PlayGameHandler playGameHandler;
    private GameScene gameScene;
    private PowerupManager powerupManager;

    private PlayGameHandler() {
        this.gameScene = new GameScene();
        this.powerupManager = gameScene.getPowerupManager();
    }

    public static PlayGameHandler getInstance() {
        if (playGameHandler == null) {
            playGameHandler = new PlayGameHandler();
        }
        return playGameHandler;
    }

    public void movePaddle(Constants.PaddleDirection direction) {
        /*
         * Takes in an Enum direction and calls the movePaddle function of gameScene to move the paddle.
         * Enum handling will be done later
         */
        gameScene.movePaddle(direction);
    }

    public void rotatePaddle(Constants.PaddleRotation rotation) {
        /*
         * Takes in a String (or Enum) rotation and calls the rotatePaddle function of gameScene to rotate the paddle.
         */
        gameScene.rotatePaddle(rotation);
    }

    public void activatePowerup(Constants.PowerupType powerupType) {
        /*
         * Takes in a String (or Enum) which represents the powerup that should be activated and calls the function of gameScene to activate the powerup.
         */
        powerupManager.activatePowerup(powerupType);
    }

//    public void stackPowerup(Powerup powerup) {
//        /*
//         * Takes in a String (or Enum) which represents the powerup that should be stacked and calls the function of gameScene to stack the powerup.
//         */
//        gameScene.stackPowerup(powerup);
//    }

    public GameScene getGameScene() {
        return gameScene;
    }

    public PowerupManager getPowerupManager() {
        return powerupManager;
    }

    public Ball getBall() {
        return gameScene.getBall();
    }

    public List<Ball> getCurrentBalls() {
        return gameScene.getCurrentBalls();
    }

    public Paddle getPaddle() {
        return gameScene.getPaddle();
    }

    public List<Brick> getBricks() {
        return gameScene.getBricks();
    }

    public List<Alien> getAliens() {
        return gameScene.getAliens();
    }

    public void initDomain(String name, List<Brick> bricks, Object[][] grid) {
        gameScene.initGame(name, bricks, grid);
    }

    public void pauseGame() {
        gameScene.toggleGameIsActive();
    }

    public boolean isGameActive() {
        return gameScene.isGameActive();
    }

    public void setGameIsActive(boolean state) {
        gameScene.setGameIsActive(state);
    }

    public void saveCurrentGame() {
        gameScene.saveCurrentGame();
    }

    public void stopMovePaddle(Constants.PaddleDirection direction) {
        gameScene.stopMovePaddle(direction);
    }

    public void stopRotatePaddle(Constants.PaddleRotation rotation) {
        gameScene.stopRotatePaddle(rotation);
    }

    public boolean initExistingGame(String name) {
        return gameScene.initExistingGame(name);
    }

    public void releaseBall() {
        powerupManager.deactivatePowerup(Constants.PowerupType.MAGNET);
    }

    public void subscribeToGameEvents(GameListener o) {
        gameScene.subscribe(o);
        //powerupManager.subscribe(o);
    }

    public List<Powerup> getActivatedPowerups() {
        return powerupManager.getActivatedPowerups();
    }

}
