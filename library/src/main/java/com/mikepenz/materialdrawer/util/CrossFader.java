package com.mikepenz.materialdrawer.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mikepenz.materialize.util.UIUtils;

/**
 * Created by mikepenz on 15.07.15.
 */
public class CrossFader {

    public CrossFader() {

    }

    private LinearLayout mCrossFadeContainer;
    private FrameLayout mCrossFadePanel;

    public CrossFader withStructure(View container, View panel) {
        this.mCrossFadeContainer = (LinearLayout) container;
        this.mCrossFadePanel = (FrameLayout) panel;

        return this;
    }

    public CrossFader withGeneratedStructure(View content, View first, View second) {
        ViewGroup container = ((ViewGroup) content.getParent());

        //remove the content from it's parent
        container.removeView(content);

        //create the crossFader container
        mCrossFadeContainer = new LinearLayout(content.getContext());
        mCrossFadeContainer.setOrientation(LinearLayout.HORIZONTAL);
        container.addView(mCrossFadeContainer, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //create panel
        mCrossFadePanel = new FrameLayout(content.getContext());
        mCrossFadeContainer.addView(mCrossFadePanel, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //add content to the panel
        mCrossFadePanel.addView(first, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mCrossFadePanel.addView(second, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //add back main content
        mCrossFadeContainer.addView(content, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return this;
    }

    private int mCrossFadePanelWidthFirst;
    private int mCrossFadePanelWidthSecond;

    public CrossFader withPanelWidthsDp(int first, int second) {
        mCrossFadePanelWidthFirst = (int) UIUtils.convertDpToPixel(first, mCrossFadeContainer.getContext());
        mCrossFadePanelWidthSecond = (int) UIUtils.convertDpToPixel(second, mCrossFadeContainer.getContext());
        return this;
    }

    private int mDuration = 500;

    public CrossFader withDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    private View mCrossFadePanelFirst;
    private View mCrossFadePanelSecond;

    public CrossFader build() {
        return build(false);
    }

    public CrossFader build(boolean reverted) {
        mCrossFadePanelFirst = mCrossFadePanel.getChildAt(0);
        mCrossFadePanelSecond = mCrossFadePanel.getChildAt(1);

        if (!reverted) {
            mCrossFadePanelSecond.setVisibility(View.GONE);
        } else {
            mCrossFadePanelFirst.setVisibility(View.GONE);
            mCrossFaded = true;
        }

        ViewGroup.LayoutParams layoutParams = mCrossFadePanel.getLayoutParams();
        if (!reverted) {
            layoutParams.width = mCrossFadePanelWidthFirst;
        } else {
            layoutParams.width = mCrossFadePanelWidthSecond;
        }
        mCrossFadePanel.setLayoutParams(layoutParams);

        return this;
    }

    private boolean mCrossFaded = false;

    public boolean isCrossFaded() {
        return mCrossFaded;
    }

    public void crossFade() {
        animateCrossFade(mCrossFaded);
        mCrossFaded = !mCrossFaded;
    }

    protected void animateCrossFade(boolean back) {
        View tempFirst = mCrossFadePanelFirst;
        View tempSecond = mCrossFadePanelSecond;
        int tempResultWidth = mCrossFadePanelWidthFirst;

        if (!back) {
            tempFirst = mCrossFadePanelSecond;
            tempSecond = mCrossFadePanelFirst;
            tempResultWidth = mCrossFadePanelWidthSecond;
        }

        final View first = tempFirst;
        final View second = tempSecond;
        final int resultWidth = tempResultWidth;

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        if (Build.VERSION.SDK_INT > 11) {
            first.setAlpha(0f);
        }
        first.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        if (Build.VERSION.SDK_INT > 11) {
            first.animate()
                    .alpha(1f)
                    .setDuration(mDuration)
                    .setListener(null);
        }

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        if (Build.VERSION.SDK_INT > 11) {
            second.animate()
                    .alpha(0f)
                    .setDuration(mDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            second.setVisibility(View.GONE);
                        }
                    });
        } else {
            second.setVisibility(View.GONE);
        }

        ResizeWidthAnimation anim = new ResizeWidthAnimation(mCrossFadePanel, resultWidth);
        anim.setDuration(mDuration);
        mCrossFadePanel.startAnimation(anim);
    }


    class ResizeWidthAnimation extends Animation {
        private int mWidth;
        private int mStartWidth;
        private View mView;

        public ResizeWidthAnimation(View view, int width) {
            mView = view;
            mWidth = width;
            mStartWidth = view.getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            mView.getLayoutParams().width = mStartWidth + (int) ((mWidth - mStartWidth) * interpolatedTime);
            mView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }

        @Override
        protected void finalize() throws Throwable {
            mView = null;
            super.finalize();
        }
    }
}
