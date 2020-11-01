package ui.panels;

import domain.Constants;
import domain.controllers.BuildGameHandler;
import domain.controllers.PlayGameHandler;
import domain.listeners.AlienListener;
import domain.listeners.BrickListener;
import domain.listeners.GameListener;
import domain.models.Ball;
import domain.models.Paddle;
import domain.models.alien.Alien;
import domain.models.brick.Brick;
import domain.models.powerup.Powerup;
import domain.models.powerup.objects.Laser;
import ui.BrickingBadFrame;
import ui.objects.*;
import ui.objects.GAllien;
import ui.objects.GBall;
import ui.objects.GLaser;
import ui.objects.GPaddle;
import ui.objects.GPowerup;
import ui.objects.brick.GBrick;
import ui.objects.factories.GBrickFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.*;

public class GameScenePanel extends JPanel implements BrickListener, GameListener, AlienListener {
    private Paddle paddle;
    private GPaddle gpaddle;
    private GBrickFactory gBrickFactory = GBrickFactory.getInstance();
    private List<GLaser> gLasers = new ArrayList<>();
    private List<GBrick> gBricks = new ArrayList<GBrick>();
    private List<GBall> currentGBalls = new LinkedList<GBall>();
    private List<GAllien> gAlliens = new ArrayList<>();
    private List<GPowerup> gPowerups = new ArrayList<>();

    private BrickingBadFrame brickingBadFrame;
    private PlayGameHandler playGameHandler = PlayGameHandler.getInstance();
    private BuildGameHandler buildGameHandler = BuildGameHandler.getInstance();
    private boolean playModeActive = false;
    private HashMap<String, Color> colors = new HashMap<String, Color>() {{
        put("Yellow", Color.YELLOW);
        put("Green", Color.GREEN);
        put("Blue", Color.BLUE);
        put("Red", Color.RED);
        put("CYAN", Color.CYAN);
    }};
    private boolean gameFinished = false;

    public GameScenePanel(BrickingBadFrame frame) {
        this.setPreferredSize(new Dimension((int) Constants.gameSceneWidth, 500));
        this.brickingBadFrame = frame;
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        initPlayMode();

        //KEY AND MOUSE LISTENERS
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (isGameActivated()) {
                    if (key == KeyEvent.VK_LEFT) {
                        playGameHandler.movePaddle(Constants.PaddleDirection.WEST);
                    }

                    if (key == KeyEvent.VK_RIGHT) {
                        playGameHandler.movePaddle(Constants.PaddleDirection.EAST);
                    }

                    if (key == KeyEvent.VK_A) {
                        playGameHandler.rotatePaddle(Constants.PaddleRotation.POSITIVE_ROTATION);
                    }

                    if (key == KeyEvent.VK_D) {
                        playGameHandler.rotatePaddle(Constants.PaddleRotation.NEGATIVE_ROTATION);
                    }

                    if (key == KeyEvent.VK_T) {
                        playGameHandler.activatePowerup(Constants.PowerupType.TALLER_PADDLE);
                    }

                    if (key == KeyEvent.VK_M) {
                        playGameHandler.activatePowerup(Constants.PowerupType.MAGNET);
                    }

                    if (key == KeyEvent.VK_C) {
                        playGameHandler.activatePowerup(Constants.PowerupType.CHEMICAL_BALL);
                    }
                }
                if (key == KeyEvent.VK_ESCAPE) {
                    playGameHandler.pauseGame();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_A) {
                    playGameHandler.stopRotatePaddle(Constants.PaddleRotation.POSITIVE_ROTATION);
                }

                if (key == KeyEvent.VK_D) {
                    playGameHandler.stopRotatePaddle(Constants.PaddleRotation.NEGATIVE_ROTATION);
                }

                if (key == KeyEvent.VK_LEFT) {
                    playGameHandler.stopMovePaddle(Constants.PaddleDirection.WEST);
                }

                if (key == KeyEvent.VK_RIGHT) {
                    playGameHandler.stopMovePaddle(Constants.PaddleDirection.EAST);
                }

            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!playGameHandler.isGameActive()) {
                    playGameHandler.pauseGame();
                }
                if (playGameHandler.isGameActive()) {
                    playGameHandler.releaseBall();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            for (GBall gball : currentGBalls) {
                gball.draw(g);
            }
        } catch (ConcurrentModificationException e) {

        }
        gpaddle.draw(g);

//      TODO: Add alliens to the draw method
        try {
            for (GAllien gAllien : gAlliens) {
                g.setColor(Color.GREEN);
                gAllien.draw(g);
            }
        } catch (ConcurrentModificationException e) {
//            System.err.println("Drawing error, GAlien deleting itself...");
        }


//      TODO Check this error
        try {
            for (GBrick gbrick : gBricks) {
                g.setColor(colors.get(gbrick.getColor()));
                gbrick.draw(g);
            }
        } catch (ConcurrentModificationException e) {
//            System.err.println("Drawing error, MineBrick deleting other bricks");
        }

        try {
            for (GLaser gLaser : gLasers) {
                gLaser.draw(g);
            }
        } catch (ConcurrentModificationException e) {

        }

        for (GPowerup gPowerup : gPowerups) {
            g.setColor(colors.get(gPowerup.getColor()));
            gPowerup.draw(g);
        }
    }

    public void initPlayMode() {
        playGameHandler.subscribeToGameEvents(this);

        for (Ball ball : playGameHandler.getCurrentBalls()) {
            currentGBalls.add(new GBall(ball));
        }
        paddle = playGameHandler.getPaddle();
        gpaddle = new GPaddle(paddle);

        gBricks = new ArrayList<>();
        for (Brick brick : playGameHandler.getBricks()) {
            GBrick gbrick = gBrickFactory.createBrick(brick);
            gBricks.add(gbrick);
        }
        addSelfToBrickListeners();

        gAlliens = new ArrayList<>();
        for (Alien alien : playGameHandler.getAliens()) {
            addAlien(alien);
        }
        addSelfToAlienListeners();

        for (Powerup powerup : playGameHandler.getActivatedPowerups()) {
            gPowerups.add(new GPowerup(powerup));
        }

        Thread animationThread = new Thread() {
            public void run() {
                while (true) {
                    repaint();
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        animationThread.start();
    }

    @Override
    public void update(Object o) {
        //brickListener update method
    }

    public boolean isGameActivated() {
        return playGameHandler.isGameActive();
    }

    @Override
    public void update(int id) {
        for (int i = 0; i < gBricks.size(); i++) {
            GBrick gBrick = gBricks.get(i);
            if (gBrick.getId() == id) {
                gBricks.remove(i);
            }
        }
    }

    public void addSelfToBrickListeners() {
        for (Brick br : playGameHandler.getBricks()) {
            br.addListener(this);
        }
    }


    public void addSelfToAlienListeners() {
        for (Alien al : playGameHandler.getAliens()) {
            al.addListener(this);
        }
    }

    @Override
    public void updateListener(Alien alien, String name) {
        if (name.equals("setLocation")) {
            for (GAllien al : gAlliens) {
                if (al.getId() == alien.getId()) {
                    double x = alien.getX();
                    double y = alien.getY();
                    al.setX(x);
                    al.setY(y);
                }
            }
        } else if (name.equals("delete")) {
            for (GAllien al : gAlliens) {
                if (al.getId() == alien.getId()) {
                    gAlliens.remove(al);
                }
            }
        }
    }

    @Override
    public void activateDestructiveLaserGun(Laser laser1, Laser laser2) {
        GLaser gLaser1 = new GLaser(laser1);
        GLaser gLaser2 = new GLaser(laser2);

        gLasers.add(gLaser1);
        gLasers.add(gLaser2);
    }

    @Override
    public void removeLaserGun(int laser1ID) {
        gLasers.removeIf(gl -> gl.getId() == laser1ID);
    }

    @Override
    public void addPowerup(Powerup powerup) {
        GPowerup gPowerup = new GPowerup(powerup);
        gPowerups.add(gPowerup);
    }

    @Override
    public void activateGangOfBalls(LinkedList<Ball> balls) {
        for (int i = 1; i < balls.size(); i++) {
            currentGBalls.add((new GBall(balls.get(i))));
        }
    }

    @Override
    public void ballLost(int index) {
        currentGBalls.remove(index);
    }

    @Override
    public void newBall(Ball ball) {
        currentGBalls.add(new GBall(ball));
    }

    @Override
    public void addAlien(Alien alien) {
        GAllien gAllien = new GAllien(alien);
        gAlliens.add(gAllien);
        alien.addListener(this);
    }

    @Override
    public void addBrick(Brick brick) {
        GBrick gBrick = gBrickFactory.createBrick(brick);
        gBricks.add(gBrick);
        brick.addListener(this);
    }

    @Override
    public void updateScore(double score) {
        brickingBadFrame.updateScore(score);
    }

    @Override
    public void updateLife(int life) {
        brickingBadFrame.updateLife(life);
    }

    @Override
    public void stackedPowerupsUpdated(List<String> stackedPowerupList) {
        brickingBadFrame.stackedPowerupsUpdated(stackedPowerupList);
    }

    @Override
    public void gameFinished(String message, double score, Object[][] dataArray) {

        brickingBadFrame.openGameOver(message, dataArray, score);
    }
}
