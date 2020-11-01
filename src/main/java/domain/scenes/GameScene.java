package domain.scenes;

import domain.*;
import domain.Constants.PaddleRotation;
import domain.Constants;
import domain.Time;
import domain.factories.AlienFactory;
import domain.factories.BrickFactory;
import domain.listeners.GameListener;
import domain.managers.CollisionManager;
import domain.managers.PaddleManager;
import domain.managers.PowerupManager;
import domain.models.Ball;
import domain.models.Paddle;
import domain.models.alien.Alien;
import domain.models.brick.Brick;
import domain.models.brick.WrapperBrick;
import domain.mongo.Database;
import domain.mongo.GameData;
import domain.mongo.SaveLoadAdapter;

import java.util.*;

public class GameScene {
    private CollisionManager collisionManager;// = new CollisionManager(this);
    private SaveLoadAdapter saveLoadAdapter = SaveLoadAdapter.getInstance();
    private PowerupManager powerupManager;// = new PowerupManager(this);
    private Random rgen = new Random();
    private PaddleManager paddleManager;
    private List<GameListener> listeners = new LinkedList<>();
    private List<Alien> currentAliens = new LinkedList<Alien>();
    private List<Ball> currentBalls = new LinkedList<Ball>();
    private List<Brick> bricks = new LinkedList<Brick>();
    private Object[][] grid;


    private String paddleDirection = "";
    private boolean gameIsActive = false;
    private double initialBrickSize;
    private Paddle paddle;
    private Ball ball;
    private String name;
    private double currentPlayerScore;
    private int life;
    private boolean isGameOver;

    private boolean collaboratingDiedBefore = false;

    private HashMap<String, Boolean> aliens = new HashMap<String, Boolean>() {
        {
            put("CollaboratingAlien", false);
            put("ProtectingAlien", false);
            put("RepairingAlien", false);
            put("DrunkAlien", false);
        }
    };

    public GameScene() {
        powerupManager = new PowerupManager(this);
        collisionManager = new CollisionManager(this);
        paddleManager = new PaddleManager();
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public void setBricks(List<Brick> bricks) {
        this.bricks = bricks;
    }

    public List<Alien> getAliens() {
        return currentAliens;
    }

    public void deleteObjectFromGrid(Object o) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (o.equals(grid[i][j])) {
                    grid[i][j] = null;
                }
            }
        }
    }

    public String randomNonCollaboratingAlien() {
        String[] alienTypes = new String[]{"RepairingAlien", "ProtectingAlien", "DrunkAlien"};
        int rand = rgen.nextInt(alienTypes.length);
        String alienType = alienTypes[rand];
        return alienType;
    }

    public void deleteBrick(Brick brick) {
        deleteObjectFromGrid(brick);
        if (brick.getClass() == WrapperBrick.class) {
            String alienType = ((WrapperBrick) brick).getAlienType();
            if (((WrapperBrick) brick).getPowerupType() != null) {
                powerupManager.createPowerup(((WrapperBrick) brick).getPowerupType(), brick.getX(), brick.getY());
            } else if (alienType != null) {
                if (alienType.equals("CollaboratingAlien") && collaboratingDiedBefore) {
                    alienType = randomNonCollaboratingAlien();
                }
                this.addAlien(alienType);
            }
        }
        bricks.remove(brick);
        brick.delete();

        if (brick.getType().equals("MineBrick")) {
            List<Brick> bricksToDelete = new ArrayList<>();
            for (Brick brick2 : bricks) {
                double dist = calculateDistanceBetweenBricks(brick, brick2);
                if (brick != brick2 && dist <= Constants.paddleInitialWidth) {//2 * paddle.getInitialWidth()) {
                    bricksToDelete.add(brick2);
                }
            }
            for (Brick brick1 : bricksToDelete) {
                deleteObjectFromGrid(brick1);
                bricks.remove(brick1);
                brick1.delete();
            }
            for (Brick brick1 : bricksToDelete) {
                deleteBrick(brick1);
            }
        }
        updateScore();
    }

    public void deleteAlien(Alien alien) {
        deleteObjectFromGrid(alien);
        currentAliens.remove(alien);
        aliens.put(alien.getType(), false);
        if (alien.getType().equals("CollaboratingAlien")) {
            collaboratingDiedBefore = true;
        }
        alien.delete();
    }

    public void deleteAlienWithoutDying(Alien alien) {
        deleteObjectFromGrid(alien);
        currentAliens.remove(alien);
        aliens.put(alien.getType(), false);
        if (alien.getType().equals("CollaboratingAlien")) {
            collaboratingDiedBefore = true;
        }
        alien.delete();
    }


    /**
     * @param b1 Brick 1
     * @param b2 Brick 2
     * @return the distance between the centers of these bricks
     */
    public double calculateDistanceBetweenBricks(Brick b1, Brick b2) {
        double x1 = b1.getX() + b1.getWidth() / 2;
        double y1 = b1.getY() + b1.getHeight() / 2;

        double x2 = b2.getX() + b2.getWidth() / 2;
        double y2 = b2.getY() + b2.getHeight() / 2;
        return Math.sqrt((x1 - x2) * (x1 - x2)
                + ((y1 - y2) * (y1 - y2)));
    }

    private void updatePaddle() {
        paddleManager.updatePaddleMovement(paddle);
        paddleManager.updatePaddleRotation(paddle);
    }

    public void movePaddle(Constants.PaddleDirection direction) {
        paddleManager.setPaddleDirection(direction);
    }

    public void stopMovePaddle(Constants.PaddleDirection direction) {
        paddleManager.stopMovePaddle(direction);
    }

    public void activatePowerup(Constants.PowerupType powerupType) {
        powerupManager.activatePowerup(powerupType);
    }

    public void rotatePaddle(PaddleRotation rotation) {
        paddleManager.rotatePaddle(rotation);
    }

    public void stopRotatePaddle(PaddleRotation rotation) {
        paddleManager.stopRotatePaddle(rotation);
    }

    public void initGame(String name, List<Brick> bricks, Object[][] grid) {
        this.name = name;
        this.currentPlayerScore = 0;
        this.life = 3;
        this.bricks = bricks;
        this.grid = grid;
        this.initialBrickSize = bricks.size();

        initPaddle();
        initBall();

        initThread();
    }

    public Alien initAlien(String alienType) {
        int row = grid.length - 1;
        int col = rgen.nextInt(grid[row].length - 2);
        Alien alien = addAlienToGameScene(col, row, alienType);
        return alien;
    }

    public void initBall() {
        double x = paddle.getX() + paddle.getWidth() / 2 - Ball.getBallDimension();
        double y = paddle.getY() - Ball.getBallDimension();
        // random velocity assignment
        double velocityX = .1;
        velocityX *= rgen.nextBoolean() ? 1 : -1;
        double velocitY = -.15;
        ball = new Ball(velocityX, velocitY, x, y);
        currentBalls.add(ball);
    }

    public void initPaddle() {
        double x = Constants.gameSceneWidth * .45;
        double y = Constants.gameSceneHeight - Constants.paddleHeight - Constants.brickSeparation * 5 - Constants.controlBarHeight;
        paddle = new Paddle(x, y);
    }

    private boolean checkFinishStats() {
        if (bricks.size() == 0) {
            BrickingBadApplication.getUser().compareResult((int) currentPlayerScore);
            Object[][] dataArray = Database.getInstance().getHighScoreTable();
            listeners.stream().forEach(listener -> listener.gameFinished("win", currentPlayerScore, dataArray));
            isGameOver = true;
            return true;
        }
        return false;
    }

    private void initThread() {
        Time time = Time.getInstance();
        Thread gameLoopThread = new Thread() {
            public void run() {
                while (!isGameOver) {
                    if (gameIsActive) {
                        time.update();
                        powerupManager.updatePowerupTimes(time.deltaTime());
                        powerupManager.checkPowerupActivity();

                        // move everything that has to move
                        updatePaddle();
                        for (Brick b : bricks) {
                            b.performMove();
                        }

                        if (powerupManager.isActive(Constants.PowerupType.MAGNET)) {
                            // TODO rotated paddle magnet ball location set
                            double x = paddle.getX() + (paddle.getWidth() - Ball.getBallDimension()) / 2;
                            double y = paddle.getY() - Ball.getBallDimension();
                            currentBalls.get(0).setLocation(x, y);
                        } else {
                            for (Ball ball : currentBalls) {
                                ball.setX(ball.getX() + ball.getVelocityX() * time.deltaTime());
                                ball.setY(ball.getY() + ball.getVelocityY() * time.deltaTime());
                            }
                        }
                        currentBalls.stream().forEach(Ball::updateListeners);
                        synchronized (currentAliens) {
                            try {
                                for (int i = 0; i < currentAliens.size(); i++) {
                                    Alien al = currentAliens.get(i);
                                    al.behave();
                                }

                                collisionManager.checkAlienCollisions();
                            } catch (ConcurrentModificationException e) {
//                                System.err.println();
                            }
                        }
                        collisionManager.checkPaddleCollision();
                        collisionManager.checkBallCollision();
                        collisionManager.checkBrickCollisions();

                        checkFinishStats();
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        };

        gameLoopThread.start();
    }

    public List<GameListener> getListeners() {
        return listeners;
    }

    public void ballLost(Ball lostBall) {
        powerupManager.ballLost(lostBall);

        if (currentBalls.size() == 1) {
            decreaseLife();
            if (life == 0) {
                toggleGameIsActive();
                BrickingBadApplication.getUser().compareResult((int) currentPlayerScore);
                Object[][] dataArray = Database.getInstance().getHighScoreTable();
                listeners.stream().forEach(listener -> listener.gameFinished("lose", currentPlayerScore, dataArray));
                isGameOver = true;

            } else {
                currentBalls.remove(0);
                for (GameListener gameListener : listeners) {
                    gameListener.ballLost(0);
                }
                initBall();
                for (GameListener gameListener : listeners) {
                    gameListener.newBall(currentBalls.get(0));
                }
                toggleGameIsActive();
            }
        } else {
            int index = currentBalls.indexOf(lostBall);
            currentBalls.remove(lostBall);
            for (GameListener gameListener : listeners) {
                gameListener.ballLost(index);
            }
        }
    }

    public boolean initExistingGame(String name) {
        if (loadGame(name)) {
            gameIsActive = true;
            initThread();
            return true;
        } else {
            return false;
        }
    }

    public void saveCurrentGame() {
        GameData saveData = new GameData(name, bricks, currentAliens, aliens, currentBalls, grid, paddle, currentPlayerScore, life);
        saveLoadAdapter.saveGame(saveData);
    }

    public boolean loadGame(String name) {
        GameData data = saveLoadAdapter.loadGame(name);
        if (data != null) {
            this.name = data.getName();
            this.bricks = data.getBricks();
            this.currentAliens = data.getAliens();
            this.aliens = data.getCurrentAliens();
            this.currentBalls = data.getBalls();
            this.grid = data.getGrid();
            this.paddle = data.getPaddle();
            this.currentPlayerScore = data.getScore();
            this.life = data.getLife();
            return true;
        }
        return false;
    }

    public List<Brick> getRandomRow() {
        int randomInt = rgen.nextInt(grid.length - 2);
        List<Brick> brickrow = new ArrayList<>();
        Object[] row = grid[randomInt];
        boolean hasBrickRow = false;
        int count = 0;
        while (!hasBrickRow) {
            randomInt = rgen.nextInt(grid.length - 2);
            row = grid[randomInt];
            hasBrickRow = checkHasBrick(row);
            count++;
            if (count == 20) break;
        }
        for (Object o : row) {
            if (o instanceof Brick)
                brickrow.add((Brick) o);
        }
        return brickrow;
    }

    public boolean checkHasBrick(Object[] row) {
        boolean hasBrick = false;
        for (int i = 0; i < row.length; i++) {
            if (row[i] instanceof Brick) {
                hasBrick = true;
            }
        }
        return hasBrick;
    }

    public Ball getBall() {
//        return ball;
        return currentBalls.get(0);
    }

    public Paddle getPaddle() {
        return this.paddle;
    }

    public boolean isGameActive() {
        return gameIsActive;
    }

    public void setGameIsActive(boolean state) {
        this.gameIsActive = state;

        if (gameIsActive) {
            Time.getInstance().unPause();
        } else {
            Time.getInstance().pause();
        }
    }

    public void toggleGameIsActive() {
        this.gameIsActive = !gameIsActive;

        if (gameIsActive) {
            Time.getInstance().unPause();
        } else {
            Time.getInstance().pause();
        }
    }

    public double getBrickRatio() {
        double currentSize = bricks.size();
        double ratio = currentSize / initialBrickSize;
        return ratio;
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    public void subscribe(GameListener o) {
        listeners.add(o);
    }

    public void addRandomBrick() {
        int rowInt = rgen.nextInt(grid.length - 2);
        int colInt = rgen.nextInt(grid[rowInt].length);
        Object loc = grid[rowInt][colInt];
        boolean emptyLoc = (loc == null);
        while (!emptyLoc) {
            rowInt = rgen.nextInt(grid.length - 2);
            colInt = rgen.nextInt(grid[rowInt].length);
            loc = grid[rowInt][colInt];
            emptyLoc = (loc == null);
        }

        double x = colInt * Constants.gridWidth;
        double y = rowInt * Constants.gridHeight;
        Brick brick = BrickFactory.getInstance().createBrick("SimpleBrick", x, y);
        double xCoor = x + (Constants.gridWidth - brick.getWidth()) / 2;
        double yCoor = y + (Constants.gridHeight - brick.getHeight()) / 2;
        brick.setLocation(xCoor, yCoor);
        grid[rowInt][colInt] = brick;
        bricks.add(brick);
        publishBrickEvent(brick);
    }

    public Alien addAlienToGameScene(int colInt, int rowInt, String type) {
        Alien alien = null;
        // try to place alien
        for (int i=0; i< 100; i++) {
            double x = colInt * Constants.gridWidth;
            double y = rowInt * Constants.gridHeight;
            alien = AlienFactory.getInstance().createAlien(type, x, y);
            double xCoor = colInt * Constants.gridWidth + (Constants.gridWidth - alien.getWidth()) / 2;
            double yCoor = rowInt * Constants.gridHeight + (Constants.gridHeight - alien.getHeight()) / 2;

            alien.setLocation(xCoor, yCoor);
            if (collisionManager.canPlaceAlien(alien)) {
                grid[rowInt][colInt] = alien;
                currentAliens.add(alien);
                return alien;
            }
        }
//        System.err.println("Could not place alien properly!");
        grid[rowInt][colInt] = alien;
        currentAliens.add(alien);
        return alien;
    }

    public List<Ball> getCurrentBalls() {
        return currentBalls;
    }

    public void setCurrentBalls(LinkedList<Ball> balls) {
        this.currentBalls = balls;
    }

    public void addAlien(String alienType) {
        if (aliens.get(alienType)) {
            for (Alien al : currentAliens) {
                al.extendBehavior();
            }
        } else {
            aliens.put(alienType, true);
            Alien alien = initAlien(alienType);
            publishAlienEvent(alien);
        }
    }

    public void publishAlienEvent(Alien alien) {
        for (GameListener gl : listeners) {
            gl.addAlien(alien);
        }
    }

    public void publishBrickEvent(Brick brick) {
        for (GameListener gl : listeners) {
            gl.addBrick(brick);
        }
    }

    public void updateScore() {
        double second = Time.getInstance().time() / 1000;
        currentPlayerScore += 300 / second;
        publishScore(currentPlayerScore);
    }

    public void decreaseLife() {
/**
 *  @reqires: life > 0
 *  @modifies: life
 *  @effects: the life of the player decrement by 1
 */
        if (life == 0) {
            String error = "Cannot decrease the life";
            System.out.println(error);
        } else {
            life--;
            publishLife(life);
        }
    }


    public void publishScore(double score) {
        for (GameListener gl : listeners) {
            gl.updateScore(score);
        }
    }

    public void publishLife(int life) {
        for (GameListener gl : listeners) {
            gl.updateLife(life);
        }
    }

    public PowerupManager getPowerupManager() {
        return powerupManager;
    }
}
