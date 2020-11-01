package domain.mongo;

import domain.Time;
import domain.models.Ball;
import domain.models.Grid;
import domain.models.Paddle;
import domain.models.alien.Alien;
import domain.models.brick.Brick;
import domain.models.brick.WrapperBrick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameData {
    private String name;
    private List<Brick> bricks;
    private List<Alien> aliens;
    private HashMap<String, Boolean> currentAliens;
    private List<Ball> balls;
    private Object[][] grid;
    private Paddle paddle;
    private double score;
    private int life;

    public GameData(String name, List<Brick> bricks, List<Alien> aliens, HashMap<String, Boolean> currentAliens, List<Ball> balls, Object[][] grid, Paddle paddle, double score, int life) {
        this.name = name;
        this.bricks = bricks;
        this.aliens = aliens;
        this.currentAliens = currentAliens;
        this.balls = balls;
        this.grid = grid;
        this.paddle = paddle;
        this.score = score;
        this.life = life;
    }

    static public GameData unParse(String text) {
        String name = null;
        List<Brick> bricks = new ArrayList<>();
        List<Alien> aliens = new ArrayList<>();
        HashMap<String, Boolean> currentAliens = new HashMap<>();
        List<Ball> balls = new ArrayList<>();
        Object[][] grid;
        Paddle paddle = null;
        double score = 0;
        int life = 0;
        double time = 0;

        for (String line : text.split("\n")) {
            String[] splitted = line.split(":");
            if (splitted[0].equals("name")) {
                name = splitted[1];
            } else if (splitted[0].equals("Brick")) {
                bricks.add(Brick.unParse(splitted));
            } else if (splitted[0].equals("Alien")) {
                aliens.add(Alien.unParse(splitted));
            } else if (splitted[0].equals("CurrentAlien")) {
                String alienName = splitted[1];
                boolean status = Boolean.parseBoolean(splitted[2]);
                currentAliens.put(alienName, status);
            } else if (splitted[0].equals("Ball")) {
                balls.add(Ball.unParse(splitted));
            } else if (splitted[0].equals("Paddle")) {
                paddle = Paddle.unParse(splitted);
            } else if (splitted[0].equals("score")) {
                score = Double.parseDouble(splitted[1]);
            } else if (splitted[0].equals("life")) {
                life = Integer.parseInt(splitted[1]);
            } else if (splitted[0].equals("time")) {
                time = Double.parseDouble(splitted[1]);
            }
        }
        grid = Grid.convertBricksToGrid(bricks);
        Time.getInstance().setTime(time);
        return new GameData(name, bricks, aliens, currentAliens, balls, grid, paddle, score, life);
    }

    public String parse() {
        String parsed = "";
        parsed += "name:" + name + "\n";
        for (Brick brick : this.bricks) {
            parsed += brick.getType().equals("WrapperBrick") ? ((WrapperBrick) brick).parseWB() + "\n" : brick.parse() + "\n";
        }
        for (Ball ball : this.balls) {
            parsed += ball.parse() + "\n";
        }
        for (Alien alien : this.aliens) {
            parsed += alien.parse() + "\n";
        }
        parsed += parseCurrentAliens(currentAliens);
        parsed += paddle.parse() + "\n";
        parsed += "score:" + score + "\n";
        parsed += "life:" + life + "\n";
        parsed += "time:" + Time.getInstance().time() + "\n";
        return parsed;
    }

    public String getName() {
        return name;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public List<Alien> getAliens() {
        return aliens;
    }

    public HashMap<String, Boolean> getCurrentAliens() {
        return currentAliens;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public Object[][] getGrid() {
        return grid;
    }

    public double getScore() {
        return score;
    }

    public int getLife() {
        return life;
    }

    private String parseCurrentAliens(HashMap<String, Boolean> currentAliens) {
        String parsed = "";
        for (int i = 0; i < currentAliens.size(); i++) {
            parsed += "CurrentAlien:" + currentAliens.keySet().toArray()[i] + ":" + currentAliens.get(currentAliens.keySet().toArray()[i]) + "\n";
        }
        return parsed;
    }

}