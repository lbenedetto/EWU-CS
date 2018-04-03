package com.benedetto.lars.ashman.entities;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.benedetto.lars.ashman.MazeView;
import com.benedetto.lars.ashman.enums.Direction;

import static com.benedetto.lars.ashman.enums.Direction.RIGHT;

public abstract class Entity {
    private static final int BLANK = 0;
    private static final int WALL_CAP = 1;
    private static final int WALL_CORNER = 2;
    private static final int WALL_INTERSECTION3 = 3;
    private static final int WALL_INTERSECTION4 = 4;
    private static final int WALL = 5;
    private static final int CAKE = 6;
    Direction directionActual;
    float X;
    float Y;
    public int Xindex;
    public int Yindex;
    private float velocity;
    public boolean INVINCIBLE;
    float tileSize;
    private int height;
    private int width;

    final MazeView mazeView;

    Entity(MazeView mazeView) {
        this.mazeView = mazeView;
        directionActual = RIGHT;
    }

    public abstract void draw(Canvas canvas, Paint paint, Matrix transformer);

    protected abstract int getWidth();

    protected abstract void hitWall();

    protected abstract void modify();

    public void setDirection(Direction direction) {
        directionActual = direction;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public void move() {
        switch (directionActual) {
            case UP:
                Y -= velocity;
                if (Y < 0) {
                    Y = 0;
                    hitWall();
                } else if (isInWall(true, false, 0, 0)) {
                    Y += velocity;
                    hitWall();
                }
                break;
            case DOWN:
                Y += velocity;
                if ((Y + tileSize) > height || isInWall(true, true, 0, 0)) {
                    Y -= velocity;
                    hitWall();
                }
                break;
            case LEFT:
                X -= velocity;
                if (X < 0) {
                    X = 0;
                    hitWall();
                } else if (isInWall(true, false, 0, 0)) {
                    X += velocity;
                    hitWall();
                }
                break;
            case RIGHT:
                X += velocity;
                if ((X + tileSize) > width * 14 || isInWall(true, true, 0, 0)) {
                    X -= velocity;
                    hitWall();
                }
                break;
        }
        Xindex = (int) (X / tileSize);
        Yindex = (int) (Y / tileSize);
    }

    int getWallType(int Y, int X) {
        try {
            return Integer.valueOf(mazeView.maze[Y][X].split("")[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return WALL;
        }
    }

    boolean isWall(int wallType) {
        switch (wallType) {
            case BLANK:
            case CAKE:
                return false;
            case WALL:
            case WALL_CAP:
            case WALL_CORNER:
            case WALL_INTERSECTION3:
            case WALL_INTERSECTION4:
                return true;
        }
        return true;
    }

    boolean isInWall(boolean modify, boolean bottomRight, int xOffset, int yOffset) {
        int buffer = getWidth();
        if (bottomRight) {
            Xindex = (int) ((X + buffer) / tileSize);
            Yindex = (int) ((Y + buffer) / tileSize);
        } else {
            Xindex = (int) (X / tileSize);
            Yindex = (int) (Y / tileSize);
        }
        int wallType = getWallType(Yindex + yOffset, Xindex + xOffset);
        if (wallType == CAKE && modify) modify();
        return isWall(wallType);
    }

    public void setTileSize(float tileSize) {
        this.tileSize = tileSize;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
