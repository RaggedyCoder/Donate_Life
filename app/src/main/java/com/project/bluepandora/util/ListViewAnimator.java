package com.project.bluepandora.util;

import android.R.interpolator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;

import com.project.bluepandora.donatelife.adapter.FeedListAdapter;
import com.project.bluepandora.donatelife.adapter.FeedListAdapter.ViewHolder;
import com.project.bluepandora.donatelife.data.Item;

import java.util.List;

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
public abstract class ListViewAnimator {

    private static final int ANIMATION_DURATION = 750;

    public ListViewAnimator() {
    }

    public static void deleteCell(final View v, final int index,
                                  final List<Item> mAnimList,
                                  final FeedListAdapter mMyAnimListAdapter, final Context context) {
        AnimationListener al = new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                ViewHolder vh = (ViewHolder) v.getTag();
                vh.needInflate = true;
                mAnimList.remove(index);
                mMyAnimListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // v.requestLayout();
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        };
        collapse(v, al, context);
    }

    private static void collapse(final View v, AnimationListener al,
                                 Context context) {
        final int initialHeight = v.getHeight();
        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime,
                                               Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight
                            - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        if (al != null) {
            anim.setAnimationListener(al);
        }
        anim.setInterpolator(context, interpolator.decelerate_quint);
        anim.setDuration(ANIMATION_DURATION);
        v.startAnimation(anim);
    }
}
