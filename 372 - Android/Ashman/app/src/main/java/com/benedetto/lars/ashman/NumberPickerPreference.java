package com.benedetto.lars.ashman;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

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


public class NumberPickerPreference extends DialogPreference {
    // private static final String androidns="http://schemas.android.com/apk/res/android";

    // range
    private int MAX_VALUE = 100;
    private int MIN_VALUE = 0;

    // circular behavior
    private boolean WRAP_SELECTOR_WHEEL = true;

    private NumberPicker picker;
    private int value;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);  // added by PHS
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);  // added by PHS
    }

    /***/
    // added by PHS: this approach supports arbitrary types, but requires an attrs.xml file
    // to define custom attributes (custom: instead of android:) in preferences.xml
    // attrs.xml goes in the values directory and looks like this:
    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.NumberPickerPreference,
                0, 0);
        try {
            MAX_VALUE = a.getInt(R.styleable.NumberPickerPreference_max, 100);
            MIN_VALUE = a.getInt(R.styleable.NumberPickerPreference_min, 0);
            WRAP_SELECTOR_WHEEL = a.getBoolean(R.styleable.NumberPickerPreference_wrap, true);
        } finally {
            a.recycle();
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
     ***/

    @Override
    protected View onCreateDialogView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        picker = new NumberPicker(getContext());
        picker.setLayoutParams(layoutParams);

        FrameLayout dialogView = new FrameLayout(getContext());
        dialogView.addView(picker);

        return dialogView;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        picker.setMinValue(MIN_VALUE);
        picker.setMaxValue(MAX_VALUE);
        picker.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        picker.setValue(getValue());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            picker.clearFocus();
            int newValue = picker.getValue();
            if (callChangeListener(newValue)) {
                setValue(newValue);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, MIN_VALUE);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(MIN_VALUE) : (Integer) defaultValue);
    }

    private void setValue(int value) {
        this.value = value;
        persistInt(this.value);
    }

    private int getValue() {
        return this.value;
    }
}