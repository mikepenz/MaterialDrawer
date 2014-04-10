/*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Thanks to romannurik!
 * https://gist.github.com/romannurik/8919163
 */

package com.tundem.holokitkatdrawer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.tundem.holokitkatdrawer.R;

/**
 * A layout that draws something in the insets passed to {@link #fitSystemWindows(android.graphics.Rect)}, i.e. the area above UI chrome
 * (status and navigation bars, overlay action bars).
 */
public class DrawInsetsFrameLayout extends FrameLayout {
    private Drawable mInsetBackground;

    private Rect mInsets;
    private Rect mTempRect = new Rect();
    private OnInsetsCallback mOnInsetsCallback;

    public DrawInsetsFrameLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public DrawInsetsFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public DrawInsetsFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.DrawInsetsFrameLayout, defStyle, 0);
        if (a == null) {
            return;
        }
        mInsetBackground = a.getDrawable(R.styleable.DrawInsetsFrameLayout_insetBackground);
        a.recycle();

        setWillNotDraw(true);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        mInsets = new Rect(insets);
        setWillNotDraw(mInsetBackground == null);
        postInvalidateOnAnimation();
        if (mOnInsetsCallback != null) {
            mOnInsetsCallback.onInsetsChanged(insets);
        }
        return true; // consume insets
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        if (mInsets != null && mInsetBackground != null) {
            // Top
            mTempRect.set(0, 0, width, mInsets.top);
            mInsetBackground.setBounds(mTempRect);
            mInsetBackground.draw(canvas);

            // Bottom
            mTempRect.set(0, height - mInsets.bottom, width, height);
            mInsetBackground.setBounds(mTempRect);
            mInsetBackground.draw(canvas);

            // Left
            mTempRect.set(0, mInsets.top, mInsets.left, height - mInsets.bottom);
            mInsetBackground.setBounds(mTempRect);
            mInsetBackground.draw(canvas);

            // Right
            mTempRect.set(width - mInsets.right, mInsets.top, width, height - mInsets.bottom);
            mInsetBackground.setBounds(mTempRect);
            mInsetBackground.draw(canvas);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mInsetBackground != null) {
            mInsetBackground.setCallback(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mInsetBackground != null) {
            mInsetBackground.setCallback(null);
        }
    }

    /**
     * Allows the calling container to specify a callback for custom processing when insets change (i.e. when
     * {@link #fitSystemWindows(android.graphics.Rect)} is called. This is useful for setting padding on UI elements based on
     * UI chrome insets (e.g. a Google Map or a ListView). When using with ListView or GridView, remember to set
     * clipToPadding to false.
     */
    public void setOnInsetsCallback(OnInsetsCallback onInsetsCallback) {
        mOnInsetsCallback = onInsetsCallback;
    }

    public static interface OnInsetsCallback {
        public void onInsetsChanged(Rect insets);
    }
}

