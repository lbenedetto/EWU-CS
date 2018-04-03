package com.benedetto.lars.ashman.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.benedetto.lars.ashman.MazeView;
import com.benedetto.lars.ashman.R;
import com.benedetto.lars.ashman.enums.Direction;
import com.benedetto.lars.ashman.enums.GhostColor;

import java.util.Random;

import static com.benedetto.lars.ashman.enums.Direction.getOpposite;
import static com.benedetto.lars.ashman.enums.Direction.getPerpendicular;

public class Ghost extends Entity {
    private Bitmap ghost;

    public Ghost(Context context, GhostColor ghostColor, MazeView mazeView) {
        super(mazeView);
        X = 0;
        Y = 0;
        switch (ghostColor) {
            case BLUE:
                ghost = BitmapFactory.decodeResource(context.getResources(), R.drawable.ghost_blue);
                break;
            case ORANGE:
                ghost = BitmapFactory.decodeResource(context.getResources(), R.drawable.ghost_orange);
                break;
            case PINK:
                ghost = BitmapFactory.decodeResource(context.getResources(), R.drawable.ghost_pink);
                break;
            case RED:
                ghost = BitmapFactory.decodeResource(context.getResources(), R.drawable.ghost_red);
                break;
        }
        INVINCIBLE = true;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, Matrix transformer) {
        transformer.reset();
        float scale = (tileSize*.8f) / ghost.getWidth();
        transformer.postScale(scale, scale);
        transformer.postTranslate(X, Y);
        canvas.drawBitmap(ghost, transformer, paint);
    }

    @Override
    public int getWidth() {
        return ghost.getWidth();
    }

    @Override
    public void hitWall() {
        Random random = new Random();
        int rand = random.nextInt(5);//rand range 0-4 (inclusive)
        Direction newDirection = directionActual;
        Direction perpendicularDirection = getPerpendicular(directionActual);
        while (!isDirectionAllowed(newDirection)) {
            if (rand < 2) {//rand 0 or 1
                newDirection = perpendicularDirection;
            } else if (rand < 4) {//rand 2 or 3
                newDirection = getOpposite(perpendicularDirection);
            } else {//rand 4. 1 in 5 chance to turn around
                newDirection = getOpposite(directionActual);
            }
            perpendicularDirection = getOpposite(perpendicularDirection);
            rand = random.nextInt(5);
        }
        setDirection(newDirection);
    }

    @Override
    public void modify() {

    }

    private boolean isDirectionAllowed(Direction direction) {
        switch (direction) {
            case UP:
                return !isInWall(false, false, 0, -1);
            case DOWN:
                return !isInWall(false, true, 0, 1);
            case LEFT:
                return !isInWall(false, false, -1, 0);
            case RIGHT:
                return !isInWall(false, true, 1, 0);
        }
        return false;
    }

    @Override
    public void setTileSize(float tileSize) {
        this.tileSize = tileSize;
        Random rand = new Random();
        Xindex = rand.nextInt(14);
        Yindex = rand.nextInt(14);
        while ((Yindex < 6 && Xindex < 6) || isWall(getWallType(Yindex, Xindex))) {
            Xindex = rand.nextInt(14);
            Yindex = rand.nextInt(14);
        }
        Y = Yindex * tileSize;
        X = Xindex * tileSize;
    }
}
