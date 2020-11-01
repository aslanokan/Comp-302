package domain.models;

public class Vector {
    private Double x;
    private Double y;

    public Vector(int x, int y) {
        this.x = Double.valueOf(x);
        this.y = Double.valueOf(y);
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Vector subtract(Vector vector) {
        return new Vector(this.x - vector.getX(), this.y - vector.getY());
    }
}

