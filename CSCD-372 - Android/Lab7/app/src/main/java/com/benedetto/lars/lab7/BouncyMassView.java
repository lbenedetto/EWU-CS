package com.benedetto.lars.lab7;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

public class BouncyMassView extends View {
    float displacement = 0;
    double stiffness = 1.5;
    int coils = 11;
    String shape = "Rectangle";
    final float SCALE = 25;
    final double GRAVITY = 9.80665;
    final int MASS = 1;
    final int LOGICAL_HEIGHT = 800;
    final int LOGICAL_WIDTH = 600;
    final Paint paint = new Paint();
    double acceleration;
    float velocity;

    private static final int TIMER_MSEC = 25;
    private boolean mIsRunning = false;
    private Handler mHandler;
    private Runnable mTimer;

    public BouncyMassView(Context context) {
        super(context);
        init();
    }

    public void setDisplacement(float displacement) {
        this.displacement = displacement;
        velocity = 0;
    }

    public void setStiffness(double stiffness) {
        this.stiffness = stiffness;
        velocity = 0;
    }

    public void setCoils(int coils) {
        this.coils = coils;
        velocity = 0;
    }

    public void setShape(String shape) {
        this.shape = shape;
        velocity = 0;
    }

    public BouncyMassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BouncyMassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        displacement = 0;
        velocity = 0;
        mHandler = new Handler();
        mTimer = new Runnable() {
            @Override
            public void run() {
                onTimer();
                if (mIsRunning) mHandler.postDelayed(this, TIMER_MSEC);
            }
        };
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
    }

    public void pausePlay() {
        if (mIsRunning) {
            mIsRunning = false;
            mHandler.removeCallbacks(mTimer);
        } else {
            mIsRunning = true;
            mHandler.postDelayed(mTimer, TIMER_MSEC);
        }

    }

    protected void onTimer() {
        acceleration = GRAVITY - ((stiffness * displacement) / MASS);
        velocity += acceleration * TIMER_MSEC / 1000f;
        displacement += velocity * TIMER_MSEC / 1000f;
        invalidate();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        float yLogical = (displacement * SCALE + LOGICAL_HEIGHT / 2);
        float width = canvas.getWidth();
        float height = canvas.getHeight();
        canvas.scale(width / LOGICAL_WIDTH, height / LOGICAL_HEIGHT);
        canvas.translate(300, 0);
        canvas.save();
        RectF rectF;
        switch (shape) {
            case "Circle":
                rectF = new RectF(-55, yLogical, 55, yLogical + 110);
                canvas.drawOval(rectF, paint);
                break;
            case "Picture":
                Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.baby);
                canvas.drawBitmap(icon, -135, yLogical, paint);
                break;
            case "Rectangle":
                rectF = new RectF(-55, yLogical, 55, yLogical + 60);
                canvas.drawRect(rectF, paint);
                break;
            case "Rounded Rectangle":
                rectF = new RectF(-55, yLogical, 55, yLogical + 60);
                canvas.drawRoundRect(rectF, 10, 10, paint);

        }
        float radius = yLogical / (coils + 1);
        float diameter = radius * 2;
        for (int i = 0; i < coils; i++) {
            canvas.drawOval(new RectF(-20, yLogical - diameter, 20, yLogical), paint);
            yLogical -= radius;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Ratio of 8:6
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        int aspectRatioHeight = 8;
        int aspectRatioWidth = 6;
        int calculatedHeight = maxWidth * aspectRatioHeight / aspectRatioWidth;
        int finalWidth, finalHeight;
        if (calculatedHeight > maxHeight) {
            finalWidth = maxHeight * aspectRatioWidth / aspectRatioHeight;
            finalHeight = maxHeight;
        } else {
            finalWidth = maxWidth;
            finalHeight = calculatedHeight;
        }
        setMeasuredDimension(finalWidth, finalHeight);

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        pausePlay();
    }

}