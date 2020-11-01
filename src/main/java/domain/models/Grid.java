package domain.models;

import domain.Constants;
import domain.models.brick.Brick;

import java.util.List;

public class Grid {
    private static Object[][] grid;

    private static GridCoordinate convertBrickLocationToGridLocation(Brick brick) {
        int col = (int) (brick.getX() / Constants.gridWidth);
        int row = (int) (brick.getY() / Constants.gridHeight);
        GridCoordinate coordinate = new GridCoordinate(row, col);
        brick.setGridCoordinate(coordinate);
        return coordinate;
    }

    public static Object[][] convertBricksToGrid(List<Brick> bricks) {
        int totalCols = Constants.gridCols;
        int totalRows = Constants.gridRows;
        grid = new Object[totalRows][totalCols + 1];

        for (Brick brick : bricks) {
            GridCoordinate coordinate = convertBrickLocationToGridLocation(brick);
            grid[coordinate.getRow()][coordinate.getCol()] = brick;
        }
        return grid;
    }
}
