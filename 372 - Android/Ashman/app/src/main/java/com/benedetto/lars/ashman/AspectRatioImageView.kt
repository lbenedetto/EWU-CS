package com.benedetto.lars.ashman

import android.content.Context
import android.util.AttributeSet

/**
 * Created by Paul on 11/11/2015
 * The purpose of this class is as a simple placeholder in a Relative Layout.
 * You can then align the edges of other View objects to it.
 * It knows only how to use as much space as its container allocates while enforcing an aspect ratio.
 * You'll need an attrs.xml file in your res/values directory that looks like this:
 *
 *
 * <declare-styleable name="AspectRatioImageView">
 * <attr name="aspectRatio" format="float"></attr>
</declare-styleable> *
 *
 *
 * You can change the aspect ratio in XML as follows (0.5 is the default):
 * <com.blah.blah.blah.AspectRatioImageView custom:aspectRatio="0.5f" android:layout_width="fill_parent" android:layout_height="fill_parent" android:(etc)></com.blah.blah.blah.AspectRatioImageView>
 */
class AspectRatioImageView : android.support.v7.widget.AppCompatImageView {
    private var mAspectRatio: Float = 0.toFloat()  // width divided by height

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        getAttrs(context, attrs)
    }

    private fun getAttrs(context: Context, attrs: AttributeSet) {
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.AspectRatioImageView,
                0, 0)
        try {
            mAspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspectRatio, 1.0f)
        } finally {
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)

        val calcWidth = (parentHeight.toFloat() * mAspectRatio).toInt()
        val calcHeight = (parentWidth.toFloat() / mAspectRatio).toInt()

        val finalWidth: Int
        val finalHeight: Int

        if (calcHeight > parentHeight) {
            finalWidth = calcWidth
            finalHeight = parentHeight
        } else {
            finalWidth = parentWidth
            finalHeight = calcHeight
        }

        setMeasuredDimension(finalWidth, finalHeight)
    }
}