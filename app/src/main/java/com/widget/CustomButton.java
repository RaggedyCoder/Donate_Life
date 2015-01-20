package com.widget;

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

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RemoteViews;

import com.widget.helper.CustomFontHelper;

@RemoteViews.RemoteView
public class CustomButton extends Button {


    public CustomButton(Context context) {
        super(context);

        init(null, null);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);

    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        CustomFontHelper.setCustomFont(this, context, attrs);
    }

    @Override
    public boolean isInEditMode() {

        return true;
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
    }

}
