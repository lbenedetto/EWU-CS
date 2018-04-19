package com.benedetto.lars.ashman

import android.content.Context
import android.content.res.TypedArray
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.NumberPicker

// from:
// http://stackoverflow.com/questions/20758986/android-preferenceactivity-dialog-with-number-picker
//
/* Paul Schimpf Feb 2016
 * Added xml attributes for min, max and wraparound.
 * This now requires the following in an attrs.xml file:
 * -----------------------------------------------------
   <resources>
      <declare-styleable name="NumberPickerPreference">
         <attr name="max" format="integer" />
         <attr name="min" format="integer" />
         <attr name="wrap" format="boolean" />
      </declare-styleable>
   </resources>
 * ------------------------------------------------------
 * When specifying any of the above in your preferences.xml, use custom: instead of android:
 * For example:
 *      android:key="test"
 *      android:title="Test (20-30)"
 *      android:summary="25"
 *      android:defaultValue="25"
 *      custom:max="30"
 *      custom:min="20"
 *      custom:wrap="false"
 */


class NumberPickerPreference : DialogPreference {
    // private static final String androidns="http://schemas.android.com/apk/res/android";

    // range
    private var MAX_VALUE = 100
    private var MIN_VALUE = 0

    // circular behavior
    private var WRAP_SELECTOR_WHEEL = true

    private var picker: NumberPicker? = null
    private var value: Int = 0
        set(value) {
            field = value
            persistInt(this.value)
        }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getAttrs(context, attrs)  // added by PHS
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        getAttrs(context, attrs)  // added by PHS
    }

    /** */
    // added by PHS: this approach supports arbitrary types, but requires an attrs.xml file
    // to define custom attributes (custom: instead of android:) in preferences.xml
    // attrs.xml goes in the values directory and looks like this:
    private fun getAttrs(context: Context, attrs: AttributeSet) {
        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.NumberPickerPreference,
                0, 0)
        try {
            MAX_VALUE = a.getInt(R.styleable.NumberPickerPreference_max, 100)
            MIN_VALUE = a.getInt(R.styleable.NumberPickerPreference_min, 0)
            WRAP_SELECTOR_WHEEL = a.getBoolean(R.styleable.NumberPickerPreference_wrap, true)
        } finally {
            a.recycle()
        }
    }

    /***
     * // added by PHS: this approach doesn't require an attrs.xml file
     * private void getAttrs(AttributeSet attrs) {
     * MAX_VALUE = attrs.getAttributeResourceValue(androidns, "max", 100);
     * MIN_VALUE = attrs.getAttributeResourceValue(androidns, "min", 0);
     * WRAP_SELECTOR_WHEEL = (attrs.getAttributeResourceValue(androidns, "wrap", 0) == 0)
     * ? false : true ;
     * }
     */

    override fun onCreateDialogView(): View {
        val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER

        picker = NumberPicker(context)
        picker!!.layoutParams = layoutParams

        val dialogView = FrameLayout(context)
        dialogView.addView(picker)

        return dialogView
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)
        picker!!.minValue = MIN_VALUE
        picker!!.maxValue = MAX_VALUE
        picker!!.wrapSelectorWheel = WRAP_SELECTOR_WHEEL
        picker!!.value = value
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            picker!!.clearFocus()
            val newValue = picker!!.value
            if (callChangeListener(newValue)) {
                value = newValue
            }
        }
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return a.getInt(index, MIN_VALUE)
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any) {
        value = if (restorePersistedValue) getPersistedInt(MIN_VALUE) else defaultValue as Int
    }
}