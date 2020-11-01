package ui.panels;

import domain.Constants;
import domain.controllers.BuildGameHandler;
import domain.listeners.BrickListener;
import domain.listeners.BuildSceneListener;
import domain.models.brick.Brick;
import domain.models.Vector;
import ui.objects.brick.GBrick;
import ui.objects.factories.GBrickFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

// TODO when new brick added, need to refocus so that user can delete bricks
public class BuildScenePanel extends JPanel implements BrickListener, BuildSceneListener {
    private BuildGameHandler buildGameHandler = BuildGameHandler.getInstance();

    private List<GBrick> gbricks = new ArrayList<GBrick>();

    private boolean isDragging = false;
    private Vector initialMousePosition, finalMousePosition, distanceVector;

    public BuildScenePanel() {
        this.setPreferredSize(new Dimension((int) Constants.gameSceneWidth, (int) Constants.gameSceneHeight));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        requestFocusInWindow();
        initBuildMode();

        //KEY AND MOUSE LISTENERS
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_DELETE) {
                    buildGameHandler.deleteBrick();
                }
                if (key == KeyEvent.VK_BACK_SPACE) {
                    buildGameHandler.deleteBrick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                initialMousePosition = new Vector(e.getX(), e.getY());
                buildGameHandler.selectBrick(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDragging) {
                    finalMousePosition = new Vector(e.getX(), e.getY());
                    distanceVector = finalMousePosition.subtract(initialMousePosition);
                    buildGameHandler.dropBrick(distanceVector);
                    initialMousePosition = null;
                    finalMousePosition = null;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                isDragging = true;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (GBrick gbrick : gbricks) {
            g.setColor(Constants.UIcolors.get(gbrick.getColor()));
            gbrick.draw(g);
        }
    }

    public void initBuildMode() {
        gbricks = new ArrayList<GBrick>();
        List<Brick> bricks = buildGameHandler.getBricks();
        buildGameHandler.subscribeToBuildSceneEvents(this);
        if (bricks == null) {
            JOptionPane.showMessageDialog(this, "Error: Couldn't fetch map data.");
        } else {
            for (Brick brick : bricks) {
                GBrick gbrick = GBrickFactory.getInstance().createBrick(brick);
                gbricks.add(gbrick);
                brick.addListener(gbrick);
            }
            addSelfToBrickListeners();
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

    public void update(Object o) {
    }

    @Override
    public void update(int id) {
        for (int i = 0; i < gbricks.size(); i++) {
            GBrick gBrick = gbricks.get(i);
            if (gBrick.getId() == id) {
                gbricks.remove(i);
            }
        }
    }

    public void addSelfToBrickListeners() {
        for (Brick br : buildGameHandler.getBricks()) {
            br.addListener(this);
        }
    }

    @Override
    public void brickAdded(Brick brick) {
        GBrick gbrick = GBrickFactory.getInstance().createBrick(brick);
        gbricks.add(gbrick);
        brick.addListener(gbrick);
        brick.addListener(this);
    }
}
