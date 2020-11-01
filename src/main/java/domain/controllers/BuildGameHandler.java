package domain.controllers;

import domain.listeners.BuildSceneListener;
import domain.models.Vector;
import domain.models.brick.Brick;
import domain.scenes.BuildScene;
import ui.panels.BuildScenePanel;

import java.util.List;


public class BuildGameHandler {
    private PlayGameHandler playGameHandler = PlayGameHandler.getInstance();
    private BuildScene buildScene;

    private static BuildGameHandler buildGameHandler = new BuildGameHandler();
    private BuildGameHandler() {
        buildScene = new BuildScene();
    }

    public static BuildGameHandler getInstance() {
        return buildGameHandler;
    }

    public void initBuildMode(String name, int simpleBrickCount, int mineBrickCount, int wrapperBrickCount, int halfMetalBrickCount) {
        buildScene.initBuildMode(name, simpleBrickCount, mineBrickCount, wrapperBrickCount, halfMetalBrickCount);
    }

    public void changeGameMode() {
        List<Brick> bricks = buildScene.getBricks();
        String name = buildScene.getName();
        Object[][] grid = buildScene.getGrid();
        playGameHandler.initDomain(name, bricks, grid);
    }

    public void dropBrick(Vector vector) {
        buildScene.changeBrickLocation(vector);
    }

    public void selectBrick(double x, double y) {
        Brick brick = buildScene.getBrickAt(x, y);
        buildScene.setSelectedBrick(brick);
    }

    public void deleteBrick() {
        buildScene.deleteBrick();
    }

    public List<Brick> getBricks() {
        return buildScene.getBricks();
    }

    public void saveCurrentMap() {
        buildScene.saveCurrentMap();
    }

    public boolean loadMap(String name) {
        return buildScene.loadMap(name);
    }

    public boolean verifyBrickNumbers(int simpleBrick, int mineBrick, int wrapperBrick, int halfMetalBrick) {
        return simpleBrick >= 75 && mineBrick >= 5 && wrapperBrick >= 10 && halfMetalBrick >= 10 && (simpleBrick + mineBrick + wrapperBrick + halfMetalBrick) < 150;
    }

    public void addBrickOfType(String type) {
        buildScene.addBrickOfType(type);
    }

    public void subscribeToBuildSceneEvents(BuildSceneListener buildSceneListener) {
        buildScene.subscribeToBuildSceneEvents(buildSceneListener);
    }
}
