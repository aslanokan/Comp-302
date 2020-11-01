package ui;

import domain.Constants;
import domain.controllers.BuildGameHandler;
import domain.controllers.PlayGameHandler;
import ui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BrickingBadFrame extends JFrame {

    static Constants.gameState gameState;

    private GameScenePanel gameScenePanel;
    private ControlPanel controlPanel;
    private BuildScenePanel buildScenePanel;
    private MainMenuPanel mainMenuPanel;
    private LoadMenuPanel loadMenuPanel;
    private HelpScreenPanel helpScreenPanel;
    private GameOverPanel gameOverPanel;
    private LoginPanel loginPanel;
    private Container app = this.getContentPane();

    private PlayGameHandler playGameHandler = PlayGameHandler.getInstance();
    private BuildGameHandler buildGameHandler = BuildGameHandler.getInstance();
    private PreBuildScenePanel preBuildScenePanel;

    public BrickingBadFrame() {
        setSize((int) Constants.gameSceneWidth, (int) Constants.gameSceneHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public void openLoginPanel() {
        if (loginPanel == null) {
            loginPanel = new LoginPanel(this);
            this.add(loginPanel, BorderLayout.CENTER);
            pack();
        } else {
            loginPanel.setVisible(true);
        }
        gameState = Constants.gameState.login;
    }

    public void openMenuPanel() {
        if (mainMenuPanel == null) {
            mainMenuPanel = new MainMenuPanel(this);
            this.add(mainMenuPanel, BorderLayout.CENTER);
            pack();
        } else {
            mainMenuPanel.setVisible(true);
        }
        gameState = Constants.gameState.menu;
    }

    public void openPreBuildScenePanel() {
        if (preBuildScenePanel == null) {
            preBuildScenePanel = new PreBuildScenePanel(this);
            this.add(preBuildScenePanel, BorderLayout.CENTER);
            pack();
        } else {
            preBuildScenePanel.setVisible(true);
        }
        gameState = Constants.gameState.buildMode;
    }

    // opens the buildScenePanel
    public void openBuildScenePanel(String gameName, int simpleBrickCount, int mineBrickCount, int wrapperBrickCount, int halfMetalBrickCount) {
        buildGameHandler.initBuildMode(gameName, simpleBrickCount, mineBrickCount, wrapperBrickCount, halfMetalBrickCount);
        if (buildScenePanel == null) {
            buildScenePanel = new BuildScenePanel();
            controlPanel = new ControlPanel(this);

            app.add(buildScenePanel, BorderLayout.CENTER);
            app.add(controlPanel, BorderLayout.PAGE_START);
            buildScenePanel.setVisible(true);
            controlPanel.setVisible(true);
            buildScenePanel.requestFocusInWindow();
        } else {
            buildScenePanel.requestFocusInWindow();
        }
    }

    public void openLoadGamePanel() {
        if(loadMenuPanel == null) {
            loadMenuPanel = new LoadMenuPanel(this);
            app.add(loadMenuPanel, BorderLayout.CENTER);
        }
        loadMenuPanel.setVisible(true);
        pack();
    }

    public void openHelpScreenPanel() {
        if(helpScreenPanel == null) {
            helpScreenPanel = new HelpScreenPanel(this);
            app.add(helpScreenPanel, BorderLayout.CENTER);
        }
        helpScreenPanel.setVisible(true);
        pack();
    }

    public void closePanel(JPanel panel) {
        panel.setVisible(false);
    }

    public void closeCurrentGamePanel() {
        if (gameState == Constants.gameState.buildMode) {
            buildScenePanel.setVisible(false);
        } else if (gameState == Constants.gameState.playMode) {
            gameScenePanel.setVisible(false);
        }
    }

    public void activatePlayMode() {
        if (gameScenePanel == null) {
            buildGameHandler.changeGameMode();

            closePanel(buildScenePanel);
            controlPanel.changeToPlayMode();
            gameScenePanel = new GameScenePanel(this);
            app.add(gameScenePanel, BorderLayout.CENTER);
            gameScenePanel.setVisible(true);
            gameScenePanel.requestFocusInWindow();

            gameState = Constants.gameState.playMode;
        } else {
            gameScenePanel.requestFocusInWindow();
            gameState = Constants.gameState.playMode;
        }
    }

    // loads a map and it is called from the loadMenuPanel
    public void openBuildScenePanel(String mapName) {
        buildScenePanel = new BuildScenePanel();
        buildScenePanel.repaint();
        app.add(buildScenePanel, BorderLayout.CENTER);
        buildScenePanel.setVisible(true);

        controlPanel = new ControlPanel(this);
        app.add(controlPanel, BorderLayout.PAGE_START);
        controlPanel.setVisible(true);

        buildScenePanel.requestFocusInWindow();
        gameState = Constants.gameState.buildMode;
    }

    public void openGameOver(String message, Object[][] scoreTable, double score) {
        if (gameOverPanel == null) {
            gameOverPanel = new GameOverPanel(this, scoreTable, score, message);
            app.add(gameOverPanel, BorderLayout.PAGE_START);
            controlPanel.setVisible(false);
        }
    }

    // loads a game and it is called from the loadMenuPanel
    public void openGameScenePanel() {
        gameScenePanel = new GameScenePanel(this);
        gameScenePanel.repaint();
        add(gameScenePanel, BorderLayout.CENTER);
        gameScenePanel.setVisible(true);

        controlPanel = new ControlPanel(this);
        controlPanel.changeToPlayMode();
        add(controlPanel, BorderLayout.PAGE_START);
        controlPanel.setVisible(true);

        gameScenePanel.requestFocusInWindow();
    }

    public void save() {
        if (gameState == Constants.gameState.buildMode) {
            buildGameHandler.saveCurrentMap();
        } else if (gameState == Constants.gameState.playMode) {
            playGameHandler.saveCurrentGame();
        }
    }

    public boolean loadExistingMap(String name) {
        return buildGameHandler.loadMap(name);
    }

    public boolean loadExistingGame(String name) {
        return playGameHandler.initExistingGame(name);
    }
    // shuts down the application

    public void quitGame() {
        System.exit(0);
    }
    // Updates the displayed score

    public void updateScore(double score) {
        controlPanel.updateScore(score);
    }
    // Updates the displayed life

    public void updateLife(int life) {
        controlPanel.updateLife(life);
    }

    public void addBrickOfType(String brickType) {
        buildGameHandler.addBrickOfType(brickType);
    }

    public void requestFocusInBuildScenePanel() {
        buildScenePanel.requestFocusInWindow();
    }

    public void stackedPowerupsUpdated(List<String> stackedPowerupList) {
        String powerups = "";
        for (String str : stackedPowerupList) {
            powerups += str + " ";
        }
        controlPanel.stackedPowerupsUpdated(powerups);
    }

    public void requestFocusInGameScenePanel() {
        gameScenePanel.requestFocusInWindow();
    }
}
