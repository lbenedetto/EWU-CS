package com.benedetto.lars.ashman.enums;

public enum Direction {
    UP(3), DOWN(1), LEFT(2), RIGHT(0);
    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Direction getOpposite(Direction d) {
        switch (d) {
            case UP: return DOWN;
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
        }
        return DOWN;
    }

    public static Direction getPerpendicular(Direction d) {
        switch (d) {
            case UP: return RIGHT;
            case DOWN: return LEFT;
            case LEFT: return UP;
            case RIGHT: return DOWN;
        }
        return DOWN;
    }

}