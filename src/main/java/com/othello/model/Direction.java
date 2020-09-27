package com.othello.model;

/**
 * The direction is used to make move
 */
public enum Direction {
    UP(-1, 0),
    DOWN(+1, 0),
    LEFT(0, -1),
    RIGHT(0, +1),
    UP_LEFT(-1, -1),
    DOWN_RIGHT(+1, +1),
    DOWN_LEFT(+1, -1),
    UP_RIGHT(-1, +1);

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private int x;
    private int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}