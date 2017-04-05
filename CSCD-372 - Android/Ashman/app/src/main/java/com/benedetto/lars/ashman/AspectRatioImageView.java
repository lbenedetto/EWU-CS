package com.benedetto.lars.ashman;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Paul on 11/11/2015
 * The purpose of this class is as a simple placeholder in a Relative Layout.
 * You can then align the edges of other View objects to it.
 * It knows only how to use as much space as its container allocates while enforcing an aspect ratio.
 * You'll need an attrs.xml file in your res/values directory that looks like this:
 * <p>
 * <declare-styleable name="AspectRatioImageView">
 * <attr name="aspectRatio" format="float" />
 * </declare-styleable>
 * <p>
 * You can change the aspect ratio in XML as follows (0.5 is the default):
 * <com.blah.blah.blah.AspectRatioImageView
 * custom:aspectRatio="0.5f"
 * android:layout_width="fill_parent"
 * android:layout_height="fill_parent"
 * android:(etc)
 * />
 **/
public class AspectRatioImageView extends ImageView {
    private float mAspectRatio;  // width divided by height

    public AspectRatioImageView(final Context context) {
        super(context);
    }

    public AspectRatioImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
    }

    public AspectRatioImageView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        getAttrs(context, attrs);
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AspectRatioImageView,
                0, 0);
        try {
            mAspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspectRatio, 1.0f);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        int calcWidth = (int) ((float) parentHeight * mAspectRatio);
        int calcHeight = (int) ((float) parentWidth / mAspectRatio);

        int finalWidth, finalHeight;

        if (calcHeight > parentHeight) {
            finalWidth = calcWidth;
            finalHeight = parentHeight;
        } else {
            finalWidth = parentWidth;
            finalHeight = calcHeight;
        }

        setMeasuredDimension(finalWidth, finalHeight);
    }
}