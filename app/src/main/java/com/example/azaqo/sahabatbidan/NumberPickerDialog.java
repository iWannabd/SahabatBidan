package com.example.azaqo.sahabatbidan;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by azaqo on 4/4/2016.
 */
public class NumberPickerDialog extends DialogPreference {

    int CurNum = 0;
    NumberPicker np;

    public NumberPickerDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    @Override
    protected View onCreateDialogView() {
        np = new NumberPicker(getContext());
        return (np);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        np.setMaxValue(100);
        np.setMinValue(0);
        np.setValue(CurNum);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            np.clearFocus();
            int newValue = np.getValue();
            if (callChangeListener(newValue)){
                setValue(newValue);
            }
        }
        super.onDialogClosed(positiveResult);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(0) : (Integer) defaultValue);
    }


    public void setValue(int value) {
        this.CurNum = value;
        persistInt(this.CurNum);
    }
}
