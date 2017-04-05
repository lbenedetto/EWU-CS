package com.benedetto.lars.ashman.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;

import com.benedetto.lars.ashman.MazeView;
import com.benedetto.lars.ashman.R;

public class Ashman extends Entity {
    private double frame = 0.0;
    private final Bitmap[] ashmanFrame;
    private final MediaPlayer cakeSound;

    public Ashman(Context context, MazeView mazeView) {
        super(mazeView);
        X = 0;
        Y = 0;
        ashmanFrame = new Bitmap[2];
        ashmanFrame[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.ashman_open);
        ashmanFrame[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.ashman_closed);
        cakeSound = MediaPlayer.create(context, R.raw.cake_sound);
        INVINCIBLE = false;
    }

    public void draw(Canvas canvas, Paint paint, Matrix transformer) {
        transformer.reset();
        Bitmap ashmanBitmap = ashmanFrame[((int) frame) % 2];
        transformer.postRotate(90 * directionActual.getValue(), ashmanBitmap.getWidth() / 2f, ashmanBitmap.getWidth() / 2f);
        float scale = (tileSize*.8f) / ashmanFrame[0].getWidth();
        transformer.postScale(scale, scale);
        transformer.postTranslate(X, Y);
        canvas.drawBitmap(ashmanBitmap, transformer, paint);
        frame += .1;
    }

    @Override
    public int getWidth() {
        return ashmanFrame[0].getWidth();
    }

    private void playSound() {
        cakeSound.start();
    }

    @Override
    public void hitWall() {
        //Do nothing
    }

    @Override
    public void modify() {
        mazeView.maze[Yindex][Xindex] = "00";
        mazeView.decrementCakes();
        playSound();
    }
}
