package domain.mongo;

import domain.BrickingBadApplication;
import domain.Player;
import domain.models.Grid;
import domain.models.brick.Brick;
import domain.models.brick.WrapperBrick;

import java.util.ArrayList;
import java.util.List;

public final class MapData {
    private Player user;
    private String name;
    private List<Brick> bricks;
    private Object[][] grid;

    public MapData(Player user, String name, List<Brick> bricks) {
        this.user = user;
        this.name = name;
        this.bricks = bricks;
    }

    public MapData(String name, List<Brick> bricks, Object[][] grid) {
        this.name = name;
        this.bricks = bricks;
        this.grid = grid;
    }


    public String parse() {
        String parsed = "";
        parsed += "player:" + user.getUsername();
        parsed += ":" + user.getPassword() + "\n";
        parsed += "name:" + name + "\n";
        for (Brick brick : this.bricks) {
            parsed += brick.getType().equals("WrapperBrick") ? ((WrapperBrick) brick).parseWB() + "\n" : brick.parse() + "\n";
        }
        return parsed;
    }

    static public MapData unParse(String text) {
        String username = null;
        String password = null;
        String name = null;
        List<Brick> bricks = new ArrayList<Brick>();
        Object[][] grid;

        for (String line : text.split("\n")) {
            String[] splitted = line.split(":");
            if (splitted[0].equals("Brick")) {
                bricks.add(Brick.unParse(splitted));
            } else if (splitted[0].equals("name")) {
                name = splitted[1];
            } else if (splitted[0].equals("player")) {
                username = splitted[1];
                password = splitted[2];
            }
        }
        grid = Grid.convertBricksToGrid(bricks);
        if (BrickingBadApplication.getUser().getUsername().equals(username) && BrickingBadApplication.getUser().getPassword().equals(password)) {
            return new MapData(name, bricks, grid);
        } else {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public Object[][] getGrid() {
        return grid;
    }
}