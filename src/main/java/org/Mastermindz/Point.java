package org.Mastermindz;

public class Point implements Comparable<Point> {
    private int x, y, fScore, gScore, state;
    private Point parent;

    public Point(int x, int y) {
        this(x, y, 0, 0, 0, null);
    }

    public Point(int x, int y, int fScore, int gScore, int state, Point parent) {
        this.x = x;
        this.y = y;
        this.fScore = fScore;
        this.gScore = gScore;
        this.state = state;
        this.parent = parent;
    }

    @Override
    public int compareTo(Point other) {
        return Integer.compare(this.fScore, other.fScore);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getGScore() { return gScore; }
    public int getState() { return state; }
    public Point getParent() { return parent; }
    public void setGScore(int gScore) { this.gScore = gScore; }
    public void setFScore(int fScore) { this.fScore = fScore; }
    public void setParent(Point parent) { this.parent = parent; }
    public void setState(int state) { this.state = state; }
}
