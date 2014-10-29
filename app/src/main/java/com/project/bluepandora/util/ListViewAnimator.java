package com.project.bluepandora.util;

import java.util.List;

import com.project.bluepandora.blooddonation.adapter.FeedListAdapter;
import com.project.bluepandora.blooddonation.adapter.FeedListAdapter.ViewHolder;
import com.project.bluepandora.blooddonation.data.Item;

import android.R.interpolator;
import android.content.Context;
import android.view.animation.Transformation;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

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
