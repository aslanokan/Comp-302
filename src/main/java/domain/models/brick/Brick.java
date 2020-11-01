package domain.models.brick;

import domain.Constants;
import domain.behaviors.movement.MoveBehavior;
import domain.listeners.BrickListener;
import domain.models.GridCoordinate;

import java.util.ArrayList;

public abstract class Brick {
    private static int count;
    private static double speed = 10;//Paddle.getInitialWidth() / 4; // per second
    @Deprecated
    private double x;
    @Deprecated
    private double y;
    private double width;
    private double height;
    @Deprecated
    private double velocityX;
    @Deprecated
    private double velocityY;
    private String color;
    private int id;
    private boolean isMoveable;
    private String type;
    private ArrayList<BrickListener> listeners;
    private MoveBehavior moveBehavior;
    private GridCoordinate gridCoordinate;

    public Brick(double x, double y, String type) {
        this.x = x;
        this.y = y;
        if (Math.random() < Constants.brickMovabilityRate) {
            this.isMoveable = true;
            this.velocityX = speed;
            this.velocityY = 0;
        } else {
            this.isMoveable = false;
            this.velocityX = 0;
            this.velocityY = 0;
        }
        this.type = type;
        this.color = Constants.colors.get(type);
        listeners = new ArrayList<BrickListener>();
        this.id = count;
        count++;
    }

    public Brick(double x, double y, double width, double height, double velocityX, double velocityY, String color, boolean isMovable, String type, int id) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.color = color;
        this.isMoveable = isMovable;
        this.type = type;
        this.id = id;

        this.listeners = new ArrayList<BrickListener>();
    }

    public void delete() {
        for (BrickListener ls : listeners) {
            ls.update(this.id);
        }
    }

    public void updateListeners() {
        for (BrickListener ls : listeners) {
            ls.update(this);
        }
    }

    public void addListener(BrickListener ls) {
        listeners.add(ls);
    }


    public double getX() {
        if (moveBehavior != null) {
            return moveBehavior.getX();
        }
        return x;
    }

    public void setX(double x) {
        if (moveBehavior != null) {
            moveBehavior.setX(x);
        } else {
            this.x = x;
        }
    }

    public double getY() {
        if (moveBehavior != null) {
            return moveBehavior.getY();
        }
        return y;
    }

    public void setY(double y) {
        if (moveBehavior != null) {
            moveBehavior.setY(y);
        } else {
            this.y = y;
        }
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getVelocityX() {
        if (moveBehavior != null) {
            return moveBehavior.getVelocityX();
        }
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        if (moveBehavior != null) {
            this.moveBehavior.setVelocityX(velocityX);
        } else {
            this.velocityX = velocityX;
        }
    }

    public double getVelocityY() {
        if (moveBehavior != null) {
            return this.moveBehavior.getVelocityY();
        }
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        if (moveBehavior != null) {
            this.moveBehavior.setVelocityY(velocityY);
        } else {
            this.velocityY = velocityY;
        }
    }

    public void setLocation(double x, double y) {
        if (moveBehavior != null) {
            this.moveBehavior.setX(x);
            this.moveBehavior.setY(y);
        } else {
            this.x = x;
            this.y = y;
        }
        updateListeners();
    }

    public GridCoordinate getGridCoordinate() {
        return gridCoordinate;
    }

    public void setGridCoordinate(GridCoordinate gridCoordinate) {
        this.gridCoordinate = gridCoordinate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getMoveable() {
        return isMoveable;
    }

    public void setMoveable(boolean moveable) {
        isMoveable = moveable;
    }

    public MoveBehavior getMoveBehavior() {
        return moveBehavior;
    }

    public void setMoveBehavior(MoveBehavior moveBehavior) {
//        moveBehavior.setX(x);
//        moveBehavior.setY(y);
//        moveBehavior.setVelocityX(velocityX);
//        moveBehavior.setVelocityY(velocityY);

        this.moveBehavior = moveBehavior;
    }

    public void performMove() {
        this.moveBehavior.move();
        this.setLocation(moveBehavior.getX(), moveBehavior.getY());
    }

    public boolean getIsMoveable() {
        return isMoveable;
    }

    public String parse() {
        String result = "Brick";
        result += ":" + this.getX();
        result += ":" + this.getY();
        result += ":" + this.width;
        result += ":" + this.height;
        result += ":" + this.getVelocityX();
        result += ":" + this.getVelocityY();
        result += ":" + this.color;
        result += ":" + this.isMoveable;
        result += ":" + this.type;
        result += ":" + this.id;
        return result;
    }

    public static Brick unParse(String[] data) {
        double x = Double.parseDouble(data[1]);
        double y = Double.parseDouble(data[2]);
        double width = Double.parseDouble(data[3]);
        double height = Double.parseDouble(data[4]);
        double velocityX = Double.parseDouble(data[5]);
        double velocityY = Double.parseDouble(data[6]);
        String color = data[7];
        boolean isMovable = Boolean.parseBoolean(data[8]);
        String type = data[9];
        int id = Integer.parseInt(data[10]);
        String powerupType = null;
        String alienType = null;
        if (data.length > 11) {
            powerupType= data[11] != "null" ? data[11] : null;
            alienType = data[12] != "null" ? data[12] : null;
        }

        switch (type) {
            case "SimpleBrick":
                return new SimpleBrick(x, y, width, height, velocityX, velocityY, color, isMovable, type, id);
            case "MineBrick":
                return new MineBrick(x, y, width, height, velocityX, velocityY, color, isMovable, type, id);
            case "WrapperBrick":
                return new WrapperBrick(x, y, width, height, velocityX, velocityY, color, isMovable, type, id, powerupType, alienType);
            case "HalfMetalBrick":
                return new HalfMetalBrick(x, y, width, height, velocityX, velocityY, color, isMovable, type, id);
        }
        return null;
    }
}
