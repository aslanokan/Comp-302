package domain;

import java.awt.*;
import java.util.HashMap;

public class Constants {
    public static final boolean prod = true;
    public static final String database = "local";

    public static final double gameSceneWidth = 500;
    public static final double gameSceneHeight = 600;
    public static final double controlBarHeight = 100;
    public static final double controlBarWidth = 300;
    public static final double simpleBrickWidth = 30;
    public static final double simpleBrickHeight = 15;
    public static final double mineBrickDiameter = 15;

    public static final HashMap<String, Integer> brickRequirements = new HashMap<String, Integer>() {{
        put("MineBrick", 5);
        put("HalfMetalBrick", 10);
        put("WrapperBrick", 10);
        put("SimpleBrick", 75);
    }};

    public static final HashMap<String, String> colors = new HashMap<String, String>() {{
        put("MineBrick", "Green");
        put("HalfMetalBrick", "Red");
        put("WrapperBrick", "Yellow");
        put("SimpleBrick", "Blue");
        put("Paddle", "Red");
        put("Ball", "Red");
        put("Powerup", "CYAN");
    }};
    public static final double brickMovabilityRate = 0.1;
    public static final double brickSeparation = 2;

    public static final double paddleHeight = 20;
    public static final double paddleUndoRotationAngle = 45; // per second towards horizontal position
    public static final double paddleRotationChangeRate = 20; //implemented later
    public static final double paddleMaxPositiveRotationAngle = 45;
    public static final double paddleMaxNegativeRotationAngle = -45;
    public static final double paddleInitialWidth = 50;
    public static double paddleSpeed = 175;

    public static final double alienSpeed = 10;
    public static final double alienWidth = 20;
    public static final double alienHeight = 20;

    public static final double gridWidth = 5 + Constants.simpleBrickWidth;
    public static final double gridHeight = 5 + Constants.simpleBrickHeight;
    public static final int gridRows = 15;
    public static final int gridCols = (int) (Constants.gameSceneWidth / (Constants.gridWidth + Constants.brickSeparation));

    public static final int powerupWidth = (int) simpleBrickHeight;
    public static final int powerupHeight = powerupWidth;

    public static final int powerupVelocityX = 0;
    public static final int powerupVelocityY = 30;
    public static final int destructiveLaserGunShots = 5;
    public static final double laserVelocityX = 0;
    public static final double laserVelocityY = -55;
    public static final double laserDiameter = 8;
    public static final int waitForLaserGun = 1;
    public static final double tallerPaddleMaxTime = 30000;
    public static final double chemicalBallMaxTime = 60000;
    public static final double fireballMaxDistBetweenBricks = Math.sqrt((Constants.gridWidth * Constants.gridWidth) +
            (Constants.gridHeight * Constants.gridHeight));
    public static final int newBallAngle = 36;
    public static HashMap<PowerupType, Boolean> powerupStackability = new HashMap<PowerupType, Boolean>() {{
        put(PowerupType.TALLER_PADDLE, true);
        put(PowerupType.CHEMICAL_BALL, true);
        put(PowerupType.MAGNET, true);
        put(PowerupType.DESTRUCTIVE_LASER_GUN, false);
        put(PowerupType.FIREBALL, false);
        put(PowerupType.GANG_OF_BALLS, false);
    }};


    public enum PaddleRotation {
        POSITIVE_ROTATION,
        NEGATIVE_ROTATION,
        UNDOING_POSITIVE_ROTATION,
        UNDOING_NEGATIVE_ROTATION,
        IDLE_ROTATION;

    }

    public enum PaddleDirection {
        EAST, WEST, NONE;

    }

    public enum DestructiveLaserGunState {
        NOT_ACTIVE, FIRE, WAIT;

    }

    public enum PowerupType {
        TALLER_PADDLE, MAGNET, DESTRUCTIVE_LASER_GUN,
        FIREBALL, CHEMICAL_BALL, GANG_OF_BALLS;
    }

    public static final HashMap<String, PowerupType> stringToPowerupType = new HashMap<String, PowerupType>() {{
        put("TALLER_PADDLE", PowerupType.TALLER_PADDLE);
        put("MAGNET", PowerupType.MAGNET);
        put("DESTRUCTIVE_LASER_GUN", PowerupType.DESTRUCTIVE_LASER_GUN);
        put("FIREBALL", PowerupType.FIREBALL);
        put("CHEMICAL_BALL", PowerupType.CHEMICAL_BALL);
        put("GANG_OF_BALLS", PowerupType.GANG_OF_BALLS);
    }};

    public enum MagnetState {
        NOT_INITIALIZED, RESERVED, USED
    }

    public static final HashMap<String, Color> UIcolors = new HashMap<String, Color>() {{
        put("Yellow", Color.YELLOW);
        put("Green", Color.GREEN);
        put("Blue", Color.BLUE);
        put("Red", Color.RED);
    }};

    public enum gameState {
        login,
        buildMode,
        playMode,
        menu
    }
}
