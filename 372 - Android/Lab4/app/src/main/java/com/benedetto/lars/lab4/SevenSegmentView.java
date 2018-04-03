package com.benedetto.lars.lab4;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

public class SevenSegmentView extends View {
    int value;
    boolean[] segments;


    private void init(Context context, AttributeSet attrs) {
        segments = new boolean[7];
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SevenSegmentView, 0, 0);
        try {
            setValue(a.getInteger(R.styleable.SevenSegmentView_value, 0));
        } finally {
            a.recycle();
        }
    }

    public SevenSegmentView(Context context) {
        super(context);
        setValue(10);
    }

    public SevenSegmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SevenSegmentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public int getValue() {
        return value;
    }

    public void incrementValue(){
        value += 1;
        if(value == 11) value = 0;
        setValue(value);
    }
    public void setValue(int newValue) {
        if (newValue > -1 && newValue < 10) {
            value = newValue;
            segments[0] = value != 1 && value != 4;
            segments[1] = value != 5 && value != 6;
            segments[2] = value != 2;
            segments[3] = value != 1 && value != 4 && value != 7;
            segments[4] = value == 0 || value == 2 || value == 6 || value == 8;
            segments[5] = value != 1 && value != 2 && value != 3 && value != 7;
            segments[6] = value != 0 && value != 1 && value != 7;
        } else if (newValue == 10) {
            value = 10;
            segments = new boolean[7];
        }
        invalidate();
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Ratio of 5:9
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        int aspectRatioHeight = 9;
        int aspectRatioWidth = 5;
        int calculatedHeight = maxWidth * aspectRatioHeight / aspectRatioWidth;
        int finalWidth, finalHeight;
        if (calculatedHeight > maxHeight)
        {
            finalWidth = maxHeight * aspectRatioWidth / aspectRatioHeight;
            finalHeight = maxHeight;
        }
        else
        {
            finalWidth = maxWidth;
            finalHeight = calculatedHeight;
        }
        setMeasuredDimension(finalWidth,finalHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        this.setBackgroundColor(Color.BLACK);
        int on = Color.rgb(255, 0, 0);
        int off = Color.rgb(76, 0, 0);
        int margin = percent(10, width);
        int fullSize = percent(60, width);
        int offset = fullSize + percent(10, fullSize);
        drawHorizontalSegment(canvas, segments[0] ? on : off, margin, fullSize, 0);
        drawVerticalSegment(canvas, segments[1] ? on : off, margin, fullSize, 0, offset);
        drawVerticalSegment(canvas, segments[2] ? on : off, margin, fullSize, offset, offset);
        drawHorizontalSegment(canvas, segments[3] ? on : off, margin, fullSize, 2 * offset);
        drawVerticalSegment(canvas, segments[4] ? on : off, margin, fullSize, offset, 0);
        drawVerticalSegment(canvas, segments[5] ? on : off, margin, fullSize, 0, 0);
        drawHorizontalSegment(canvas, segments[6] ? on : off, margin, fullSize, offset);
    }

    private void drawHorizontalSegment(Canvas canvas, int color, int margin, int fullSize, int vOffset) {
        Paint p = new Paint();
        p.setColor(color);
        p.setStyle(Paint.Style.FILL);
        Path path = new Path();
        int ninety = percent(90, fullSize);
        int ten = percent(10, fullSize);
        int twenty = percent(20, fullSize);
        path.moveTo(margin + ten, margin + ten + vOffset);
        path.lineTo(margin + twenty, margin + vOffset);
        path.lineTo(margin + ninety + twenty, margin + vOffset);
        path.lineTo(margin + fullSize + twenty, margin + ten + vOffset);
        path.lineTo(margin + ninety + twenty, margin + twenty + vOffset);
        path.lineTo(margin + twenty, margin + twenty + vOffset);
        path.moveTo(margin + ten, margin + ten + vOffset);
        canvas.drawPath(path, p);
    }

    private void drawVerticalSegment(Canvas canvas, int color, int margin, int fullSize, int vOffset, int hOffset) {
        Paint p = new Paint();
        p.setColor(color);
        p.setStyle(Paint.Style.FILL);
        Path path = new Path();
        int ninety = percent(90, fullSize);
        int ten = percent(10, fullSize);
        int twenty = percent(20, fullSize);
        path.moveTo(margin + ten + hOffset, margin + ten + vOffset);
        path.lineTo(margin + hOffset, margin + twenty + vOffset);
        path.lineTo(margin + hOffset, margin + ninety + twenty + vOffset);
        path.lineTo(margin + ten + hOffset, margin + fullSize + twenty + vOffset);
        path.lineTo(margin + twenty + hOffset, margin + ninety + twenty + vOffset);
        path.lineTo(margin + twenty + hOffset, margin + twenty + vOffset);
        path.moveTo(margin + ten + hOffset, margin + ten + vOffset);
        canvas.drawPath(path, p);
    }

    private int percent(int percent, int value) {
        //percent(10%, of value)
        return (int) (value * (percent / 100.0f));
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        // put whatever the superclass wants to save in this bundle
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("value", value);
        // use bundle.putInt, putBoolean, putString, putFloat, putXxxArray,
        // putSerializable, putParcelable, etc. to store your state
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            // use bundle.getXxx to restore your state
            // now grab whatever the super class stored and pass it along
            state = bundle.getParcelable("instanceState");
            setValue(bundle.getInt("value"));
        }
        super.onRestoreInstanceState(state);
    }
}
