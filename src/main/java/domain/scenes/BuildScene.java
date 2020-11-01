package domain.scenes;

import domain.BrickingBadApplication;
import domain.Constants;
import domain.Player;
import domain.factories.BrickFactory;
import domain.listeners.BuildSceneListener;
import domain.models.GridCoordinate;
import domain.models.Vector;
import domain.models.brick.Brick;
import domain.models.brick.WrapperBrick;
import domain.mongo.MapData;
import domain.mongo.SaveLoadAdapter;

import java.util.*;

public class BuildScene {
    SaveLoadAdapter saveLoadAdapter = SaveLoadAdapter.getInstance();
    BrickFactory brickFactory = BrickFactory.getInstance();
    int simpleBricksNumber;
    int mineBricksNumber;
    int halfMetalBricksNumber;
    int wrapperBricksNumber;
    private Random rgen = new Random();
    private Object[][] grid;
    private List<Brick> bricks;
    private Brick selectedBrick;
    private int numberOfPowerup = 6;
    private int numberOfAlien = 3;
    private List<String> alienTypes = new ArrayList<String>(Arrays.asList("CollaboratingAlien", "RepairingAlien", "ProtectingAlien", "DrunkAlien"));
    private String name;
    private List<BuildSceneListener> listeners = new LinkedList<>();

    /**
     * BuildScene constructor
     */
    public BuildScene() {
        this.bricks = new ArrayList<Brick>();
        this.selectedBrick = null;
    }

    /**
     * @return the Brick instance at the location, if none return null
     */
    public Brick getBrickAt(double x, double y) {
        for (Brick brick : bricks) {
            double brickX = brick.getX();
            double brickY = brick.getY();
            double brickWidth = brick.getWidth();
            double brickHeight = brick.getHeight();
            if (brickX <= x && x <= brickX + brickWidth) {
                if (brickY <= y && y <= brickY + brickHeight) {
                    return brick;
                }
            }
        }
        return null;
    }

    public void initBuildMode(String name, int simpleBrickCount, int mineBrickCount, int wrapperBrickCount, int halfMetalBrickCount) {
        this.name = name;

        initBricks(simpleBrickCount, mineBrickCount, wrapperBrickCount, halfMetalBrickCount);
    }

    private String determineBrickTypeRandomly() {
        int total = simpleBricksNumber + halfMetalBricksNumber + mineBricksNumber + wrapperBricksNumber;
        double randomInt = rgen.nextInt(total);
        if (randomInt < simpleBricksNumber) {
            simpleBricksNumber--;
            return "SimpleBrick";
        } else if (randomInt < simpleBricksNumber + halfMetalBricksNumber) {
            halfMetalBricksNumber--;
            return "HalfMetalBrick";
        } else if (randomInt < simpleBricksNumber + halfMetalBricksNumber + mineBricksNumber) {
            mineBricksNumber--;
            return "MineBrick";
        } else {
            wrapperBricksNumber--;
            return "WrapperBrick";
        }
    }

    /**
     * //REQUIRES: selectedBrick is not null
     * //EFFECTS: removes the selectedBrick from its bricks list
     */
    public void deleteBrick() {
        int mines = (int) bricks.stream().filter(brick -> brick.getType() == "MineBrick").count();
        int simples = (int) bricks.stream().filter(brick -> brick.getType() == "SimpleBrick").count();
        int wrappers = (int) bricks.stream().filter(brick -> brick.getType() == "WrapperBrick").count();
        int halves = (int) bricks.stream().filter(brick -> brick.getType() == "HalfMetalBrick").count();

        if (selectedBrick != null) {
            boolean canBeDeleted = false;
            String brickType = selectedBrick.getType();
            switch (brickType) {
                case "MineBrick":
                    canBeDeleted = mines > Constants.brickRequirements.get(brickType);
                    break;
                case "SimpleBrick":
                    canBeDeleted = simples > Constants.brickRequirements.get(brickType);
                    break;
                case "HalfMetalBrick":
                    canBeDeleted = halves > Constants.brickRequirements.get(brickType);
                    break;
                case "WrapperBrick":
                    canBeDeleted = wrappers > Constants.brickRequirements.get(brickType);
                    break;
            }
            if (canBeDeleted) {
                bricks.remove(selectedBrick);
                selectedBrick.delete();
                grid[selectedBrick.getGridCoordinate().getRow()][selectedBrick.getGridCoordinate().getCol()] = null;
                selectedBrick = null;
            }
        }
    }

    /**
     * @param brick brick to be set as the SelectedBrick
     *              //MODIFIES: selectedBrick
     */
    public void setSelectedBrick(Brick brick) {
        selectedBrick = brick;
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
        String brickType = determineBrickTypeRandomly();
        Brick brick = brickFactory.createBrick(brickType, x, y);
        if (brickType == "WrapperBrick") {
            if (numberOfPowerup != 0) {
                Constants.PowerupType powerup = randomPowerup();
                ((WrapperBrick) brick).setPowerupType(powerup);
                numberOfPowerup--;
            } else if (numberOfAlien != 0) {
                String alienType = randomAlien();
            ((WrapperBrick) brick).setAlienType(alienType);
                numberOfAlien--;
            } else {
                int rand = new Random().nextInt(2);
                if (rand == 0) {
                    Constants.PowerupType powerup = randomPowerup();
                    ((WrapperBrick) brick).setPowerupType(powerup);
                } else {
                    String alienType = randomAlien();
                    ((WrapperBrick) brick).setAlienType(alienType);
                }
            }
        }
        double xCoor = x + (Constants.gridWidth - brick.getWidth()) / 2;
        double yCoor = y + (Constants.gridHeight - brick.getHeight()) / 2;
        brick.setLocation(xCoor, yCoor);
        brick.setGridCoordinate(new GridCoordinate(rowInt, colInt));
        grid[rowInt][colInt] = brick;
        bricks.add(brick);
    }

    /**
     * @return selectedBrick
     * //REQUIRES: selectedBrick != null
     */
    public void changeBrickLocation(Vector vector) {
        if (selectedBrick != null) {
            Double x = selectedBrick.getX() + Constants.simpleBrickWidth / 2 + vector.getX();
            Double y = selectedBrick.getY() + Constants.simpleBrickHeight / 2 + vector.getY();
            try {
                Vector position = locatePositionInGrid(x, y);
                if (position != null) {
                    if (selectedBrick.getType() == "MineBrick") {
                        selectedBrick.setLocation(position.getX() + (Constants.simpleBrickWidth - Constants.mineBrickDiameter) / 2, position.getY());
                    } else {
                        selectedBrick.setLocation(position.getX(), position.getY());
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
//                System.out.println("Cannot place brick." + e.toString());
            }
        }
    }

    private Vector locatePositionInGrid(Double x, Double y) throws ArrayIndexOutOfBoundsException {
        double xRemainder = x % Constants.gridWidth;
        double yRemainder = y % Constants.gridHeight;
        int col = (xRemainder < Constants.gridWidth / 4) ? (int) (x / Constants.gridWidth) : (int) (x / Constants.gridWidth);
        int row = (yRemainder < Constants.gridHeight / 4) ? (int) (y / Constants.gridHeight) : (int) (y / Constants.gridHeight);
        if (grid[row][col] == null) {
            grid[selectedBrick.getGridCoordinate().getRow()][selectedBrick.getGridCoordinate().getCol()] = null;
            selectedBrick.setGridCoordinate(new GridCoordinate(row, col));
            grid[row][col] = selectedBrick;
            return new Vector(col * Constants.gridWidth + Constants.brickSeparation, row * Constants.gridHeight + Constants.brickSeparation);
        } else {
            return null;
        }
    }

    public void initBricks(int simpleBrickCount, int mineBrickCount, int wrapperBrickCount, int halfMetalBrickCount) {
        simpleBricksNumber = simpleBrickCount;
        mineBricksNumber = mineBrickCount;
        halfMetalBricksNumber = halfMetalBrickCount;
        wrapperBricksNumber = wrapperBrickCount;
        int totalBricksNumber = simpleBricksNumber + halfMetalBricksNumber + mineBricksNumber + wrapperBricksNumber;
        // Used for grid structure
        int totalCols = (int) (Constants.gameSceneWidth / (Constants.gridWidth + Constants.brickSeparation));
        int totalRows = 2 * (int) Math.ceil(totalBricksNumber / totalCols) + 2;
        if(totalRows > Constants.gridRows){
            totalRows = Constants.gridRows;
        }
        grid = new Object[totalRows][totalCols];
        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < totalCols; col++) {
                if (bricks.size() == totalBricksNumber)
                    break;
                addRandomBrick();
            }
        }
    }

    public List<Brick> getBricks() {
        return this.bricks;
    }

    public String getName() {
        return this.name;
    }

    public void saveCurrentMap() {
        Player user = BrickingBadApplication.getUser();
        MapData saveData = new MapData(user, name, bricks);
        saveLoadAdapter.saveMap(saveData);
    }

    public boolean loadMap(String name) {
        MapData data = saveLoadAdapter.loadMap(name);
        if (data != null) {
            this.name = data.getName();
            this.bricks = data.getBricks();
            this.grid = data.getGrid();
            return true;
        } else {
            return false;
        }
    }

    public Object[][] getGrid() {
        return grid;
    }

    public Constants.PowerupType randomPowerup() {
        int rand = new Random().nextInt(Constants.PowerupType.values().length);
        Constants.PowerupType powerup = Constants.PowerupType.values()[rand];
        return powerup;
    }

    public String randomAlien() {
        int rand = new Random().nextInt(alienTypes.size());
        String alienType = alienTypes.get(rand);
        return alienType;
    }

    public void addBrickOfType(String brickType) {
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
        Brick brick = brickFactory.createBrick(brickType, x, y);
        if (brickType == "WrapperBrick") {
            if (numberOfPowerup != 0) {
                Constants.PowerupType powerup = randomPowerup();
                ((WrapperBrick) brick).setPowerupType(powerup);
                numberOfPowerup--;
            } else if (numberOfAlien != 0) {
                String alienType = randomAlien();
                ((WrapperBrick) brick).setAlienType(alienType);
                numberOfAlien--;
            } else {
                int rand = new Random().nextInt(2);
                if (rand == 0) {
                    Constants.PowerupType powerup = randomPowerup();
                    ((WrapperBrick) brick).setPowerupType(powerup);
                } else {
                    String alienType = randomAlien();
                    ((WrapperBrick) brick).setAlienType(alienType);
                }
            }
        }
        double xCoor = x + (Constants.gridWidth - brick.getWidth()) / 2;
        double yCoor = y + (Constants.gridHeight - brick.getHeight()) / 2;
        brick.setLocation(xCoor, yCoor);
        brick.setGridCoordinate(new GridCoordinate(rowInt, colInt));
        grid[rowInt][colInt] = brick;
        bricks.add(brick);
        listeners.stream().forEach(listener -> listener.brickAdded(brick));
    }

    public void subscribeToBuildSceneEvents(BuildSceneListener buildSceneListener) {
        this.listeners.add(buildSceneListener);
    }

    public Brick getSelectedBrick() {
        return selectedBrick;
    }

}
