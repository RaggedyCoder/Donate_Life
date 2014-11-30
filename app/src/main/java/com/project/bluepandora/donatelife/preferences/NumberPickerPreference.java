package com.project.bluepandora.donatelife.preferences;

/*
 * Copyright (C) 2014 The Blue Pandora Project Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.project.bluepandora.donatelife.R;

import java.lang.reflect.Field;


public class NumberPickerPreference extends DialogPreference {
    private int mMin, mMax, mDefault;

    private String mMaxExternalKey, mMinExternalKey;

    private NumberPicker mNumberPicker;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
//        TypedArray dialogType = context.obtainStyledAttributes(attrs,
//                com.android.internal.R.styleable.DialogPreference, 0, 0);
        TypedArray numberPickerType = context.obtainStyledAttributes(attrs,
                R.styleable.NumberPickerPreference, 0, 0);
        mMaxExternalKey = numberPickerType.getString(R.styleable.NumberPickerPreference_maxExternal);
        mMinExternalKey = numberPickerType.getString(R.styleable.NumberPickerPreference_minExternal);

        mMax = numberPickerType.getInt(R.styleable.NumberPickerPreference_max, 100);
        mMin = numberPickerType.getInt(R.styleable.NumberPickerPreference_min, 10);

//        mDefault = dialogType.getInt(com.android.internal.R.styleable.Preference_defaultValue, mMin);
        mDefault = mMin;
//        dialogType.recycle();
        setSummary(getContext().getResources().getQuantityString(
                R.plurals.numbers,
                PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("pref_key_feed_show_limit", 43),
                PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("pref_key_feed_show_limit", 43)));
        numberPickerType.recycle();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected View onCreateDialogView() {
        int max = mMax;
        int min = mMin;

        // External values
        if (mMaxExternalKey != null) {
            max = getSharedPreferences().getInt(mMaxExternalKey, mMax);
        }
        if (mMinExternalKey != null) {
            min = getSharedPreferences().getInt(mMinExternalKey, mMin);
        }

        LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.number_picker_dialog, null, false);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);

        if (mNumberPicker == null) {
            throw new RuntimeException("mNumberPicker is null!");
        }

        // Initialize state
        mNumberPicker.setWrapSelectorWheel(false);
        mNumberPicker.setMaxValue(max);
        mNumberPicker.setMinValue(min);
        mNumberPicker.setValue(getPersistedInt(mDefault));
        // No keyboard popup
        disableTextInput(mNumberPicker);
//        EditText textInput = (EditText) mNumberPicker.findViewById(com.android.internal.R.id.numberpicker_input);
//        if (textInput != null) {
//            textInput.setCursorVisible(false);
//            textInput.setFocusable(false);
//            textInput.setFocusableInTouchMode(false);
//        }
        return view;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistInt(mNumberPicker.getValue());
            setSummary(getContext().getResources().getQuantityString(
                    R.plurals.numbers, mNumberPicker.getValue(), mNumberPicker.getValue()));
        }
    }

    /*
     * reflection of NumberPicker.java
     * verified in 4.1, 4.2
     * */
    private void disableTextInput(NumberPicker np) {
        if (np == null) return;
        Class<?> classType = np.getClass();
        Field inputTextField;
        try {
            inputTextField = classType.getDeclaredField("mInputText");
            inputTextField.setAccessible(true);
            EditText textInput = (EditText) inputTextField.get(np);
            if (textInput != null) {
                textInput.setCursorVisible(false);
                textInput.setFocusable(false);
                textInput.setFocusableInTouchMode(false);
            }
        } catch (Exception e) {
            Log.d("trebuchet", "NumberPickerPreference disableTextInput error", e);
        }
    }
}